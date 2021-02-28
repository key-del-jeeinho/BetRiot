package com.xylope.betriot.layer.service.user;

import com.xylope.betriot.exception.DataNotFoundException;
import com.xylope.betriot.exception.WrongRegisterProgressException;
import com.xylope.betriot.layer.dataaccess.SummonerAPI;
import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.service.SpecialEmote;
import com.xylope.betriot.layer.service.discord.listener.GuildMemberJoinListener;
import com.xylope.betriot.layer.service.discord.listener.PrivateMessageReactionAddListener;
import com.xylope.betriot.layer.service.discord.listener.PrivateMessageReceivedListener;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;

import java.awt.*;
import java.util.*;

public class UserRegisterService {
    @Getter
    private final UnRegisterUserSet unRegisterUserSet;
    @Setter
    private SummonerAPI summonerAPI;
    @Setter
    UserDao dao;

    public UserRegisterService(GuildMemberJoinListener guildMemberJoinListener,
                               PrivateMessageReactionAddListener privateMessageReactionAddListener,
                               PrivateMessageReceivedListener privateMessageReceivedListener) {
        unRegisterUserSet = new UnRegisterUserSet();

        guildMemberJoinListener.addListener(this::checkIsUserUnRegistered);
        privateMessageReactionAddListener.addListener(this::checkTermsAgree);
        privateMessageReceivedListener.addListener(this::authorizeRiotAccount);
    }

    public void registerUser(Guild guild, User user) {
        //Build Message
        PrivateChannel pc = user.openPrivateChannel().complete();

        //TODO 2021.02.27 | 추후 Property 사용할것 | Xylope
        String registerMessage = String.format(
                "%s님이 계신 BetRiot %s 서버는 벳라이엇봇이 적용된 서버입니다.\n" +
                "벳라이엇은 여러분의 LOL 경기를 디스코드에 중계하고, 다른 유저분들이 해당 경기에 포인트를 배팅할 수 있는 봇 입니다.\n",
                user.getAsMention(), guild.getName());
        String termsMessage =  String.format(
                "해당 봇에 가입하시려면 해당 약관에 %s 이모지를 달아주신 뒤, 자신의 롤 닉네임을 입력하여 가입절차를 진행해주세요.\n" +
                "해당 봇에 가입을 희망하지 않으신다면, %s 이모지를 달아주세요.\n" +
                "```이용약관입니다.```",
                SpecialEmote.TERMS_AGREE.getEmote(), SpecialEmote.TERMS_DISAGREE.getEmote());

        MessageEmbed content = new EmbedBuilder()
                .setColor(new Color(227, 39, 87))
                .setTitle("BetRiot 회원가입")
                .setDescription("리그로브레전드 배팅 봇 BetRiot 의 가입 안내입니다!")
                .addField("안녕하세요", registerMessage, false)
                .addField("이용약관", termsMessage, false)
                .setFooter("made by Xylope")
                .build();

        pc.sendMessage(content).queue((message)-> {
            message.addReaction(SpecialEmote.TERMS_AGREE.getEmote()).queue();
            message.addReaction(SpecialEmote.TERMS_DISAGREE.getEmote()).queue();
            prepareCheckTerm(user, message.getIdLong());
        });
    }

    public void prepareCheckTerm(User user, long messageId) {
        //Prepare to CheckTermStep
        UnRegisterUser unRegisterUser = new UnRegisterUser(user.getIdLong());
        unRegisterUser.nextStep();
        try {
            unRegisterUser.setTermsMessageId(messageId);
        } catch (WrongRegisterProgressException e) {
            System.out.println("Use setTermsMessageId method when RegisterProcess isn't " + RegisterProgress.CHECK_TERMS);
            e.printStackTrace();
        }
        unRegisterUserSet.add(unRegisterUser);
    }

    //RepeatListeners
    //Condition of Register (if User Join Guild which Applied BetRiot)
    public void checkIsUserUnRegistered(GuildMemberJoinEvent event) {
        if(event.getUser().isBot()) return;
        Guild guild = event.getGuild();
        User user = event.getUser();
        if(!dao.isUserExist(user.getIdLong())) {
            registerUser(guild, user);
        }
    }

    //CheckPrivateMessageReaction is Emote of TermAgree or TermDisagree
    public void checkTermsAgree(PrivateMessageReactionAddEvent event) {
        if(Objects.requireNonNull(event.getUser()).isBot()) return;
        long messageId = event.getMessageIdLong();
        User user = event.getChannel().getUser();

        boolean isTermsMessage =  unRegisterUserSet.isMessageTerms(messageId);
        UnRegisterUser target = null;

        if (isTermsMessage)
            target = unRegisterUserSet.getUserByTermsMessageId(messageId);

        //Find unRegisterUser at queue to termsMessageId

        //Check is Emote SpecialEmote.TERMS_XXX
        if(isTermsMessage) {
            String emote = event.getReactionEmote().getName();
            try {
                if (emote.equals(SpecialEmote.TERMS_AGREE.getEmote())) {
                    agreeTerms(target, user);
                } else if (emote.equals(SpecialEmote.TERMS_DISAGREE.getEmote())) {
                    disagreeTerms(target, user);
                }

            } catch (WrongRegisterProgressException e) {
                System.out.println("Use setTermsAgree method when RegisterProcess isn't " + RegisterProgress.CHECK_TERMS);
                PrivateChannel pc = user.openPrivateChannel().complete();
                pc.sendMessage("오류가 발생하였습니다! 다음 메세지를 관리자에게 보여주세요\n```" +
                "Use setTermsAgree method when RegisterProcess isn't " + RegisterProgress.CHECK_TERMS + "\n" +
                e.getMessage() + "```").queue();
                pc.close().complete();
            }
        }
    }

    private void agreeTerms(UnRegisterUser unRegisterUser, User user) throws WrongRegisterProgressException {
        unRegisterUser.setTermsAgree(true);

        PrivateChannel pc = user.openPrivateChannel().complete();
        pc.sendMessage("약관에 동의하셧습니다").queue();

        unRegisterUser.nextStep();
        pc.sendMessage("소환사님의 라이엇 닉네임을 입력해주세요!").queue();
    }

    private void disagreeTerms(UnRegisterUser unRegisterUser, User user) throws WrongRegisterProgressException {
        unRegisterUser.setTermsAgree(false);

        PrivateChannel pc = user.openPrivateChannel().complete();
        pc.sendMessage("약관에 비동의하셧습니다! 해당 봇이 적용된 서버에 다시 들어오시거나, 벳라이엇의 서비스를 이용하실경우 회원가입을 재진행 할 수 있습니다! 이용해주셔서 감사합니다 :)").queue();

        unRegisterUserSet.remove(unRegisterUser);
    }

    public void authorizeRiotAccount(PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        User user = event.getAuthor();
        Message message = event.getMessage();
        PrivateChannel pc = user.openPrivateChannel().complete();
        long discordId = user.getIdLong();

        if(unRegisterUserSet.isUserExistByDiscordId(discordId)) {
            UnRegisterUser unRegisterUser = unRegisterUserSet.getUserByDiscordId(discordId);
            if(unRegisterUser.getProgress().equals(RegisterProgress.RIOT_AUTHORIZE)) {
                String unreliableRiotId = message.getContentRaw();
                SummonerDto summonerDto = null;
                try {
                    summonerDto = summonerAPI.getByName(unreliableRiotId);
                } catch (DataNotFoundException e) {
                    pc.sendMessage("해당 이름을 가진 소환사를 찾을 수 없습니다! 다시시도해주세요!").queue();
                    pc.close().queue();
                    return;
                }
                MessageEmbed summonerProfileMessage = new EmbedBuilder()
                        .setColor(new Color(227, 39, 87))
                        //setImage()
                        .setTitle(summonerDto.getName() + "님 환영합니다!")
                        .addField("소환사님의 정보",
                                String.format("이름 : %s\n레벨 : %d\n계정 식별자 : %s",
                                        summonerDto.getName(),
                                        summonerDto.getSummonerLevel(),
                                        summonerDto.getAccountId()), true)
                        .build();
                pc.sendMessage(summonerProfileMessage).queue();
            }
        }
    }
}

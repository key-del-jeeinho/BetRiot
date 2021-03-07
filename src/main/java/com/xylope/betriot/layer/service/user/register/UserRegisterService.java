package com.xylope.betriot.layer.service.user.register;

import com.xylope.betriot.exception.DataNotFoundException;
import com.xylope.betriot.exception.WrongRegisterProgressException;
import com.xylope.betriot.exception.ZeroChampionMasteryPointException;
import com.xylope.betriot.layer.dataaccess.ChampionMasteryAPI;
import com.xylope.betriot.layer.dataaccess.DataDragonAPI;
import com.xylope.betriot.layer.dataaccess.SummonerAPI;
import com.xylope.betriot.layer.dataaccess.riotdata.ChampionMasteryDto;
import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.domain.vo.UserVO;
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
import java.util.List;

import static com.xylope.betriot.layer.service.SpecialEmote.*;
import static com.xylope.betriot.layer.service.user.register.RegisterProgress.CHECK_TERMS;
import static com.xylope.betriot.layer.service.user.register.RegisterProgress.RIOT_AUTHORIZE;

public class UserRegisterService {
    @Getter
    private final UnRegisterUserSet unRegisterUserSet;
    @Setter
    private SummonerAPI summonerAPI;
    @Setter
    private DataDragonAPI dataDragonAPI;
    @Setter
    private ChampionMasteryAPI championMasteryAPI;
    @Setter
    UserDao dao;
    Color defaultEmbedColor = new Color(227, 39, 87);

    public UserRegisterService(GuildMemberJoinListener guildMemberJoinListener,
                               PrivateMessageReactionAddListener privateMessageReactionAddListener,
                               PrivateMessageReceivedListener privateMessageReceivedListener) {
        unRegisterUserSet = new UnRegisterUserSet();

        //Add repeat listener
        guildMemberJoinListener.addListener(this::checkIsUserUnRegistered);
        privateMessageReactionAddListener.addListener(this::checkTermsAgree);
        privateMessageReactionAddListener.addListener(this::checkProfileIconChanged);
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
                TERMS_AGREE.getEmote(), TERMS_DISAGREE.getEmote());

        MessageEmbed content = new EmbedBuilder()
                .setColor(defaultEmbedColor)
                .setTitle("BetRiot 회원가입")
                .setDescription("리그로브레전드 배팅 봇 BetRiot 의 가입 안내입니다!")
                .addField("안녕하세요", registerMessage, false)
                .addField("이용약관", termsMessage, false)
                .setFooter("made by Xylope")
                .build();

        pc.sendMessage(content).queue((message)-> {
            message.addReaction(TERMS_AGREE.getEmote()).queue();
            message.addReaction(TERMS_DISAGREE.getEmote()).queue();
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
            System.out.println("Use setTermsMessageId method when RegisterProcess isn't " + CHECK_TERMS);
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
        UnRegisterUser target;

        if (isTermsMessage) {
            //Find unRegisterUser at queue to termsMessageId
            target = unRegisterUserSet.getUserByTermsMessageId(messageId);
            if (target.checkProgress(CHECK_TERMS)) {
                //Check is Emote SpecialEmote.TERMS_XXX
                String emote = event.getReactionEmote().getName();
                try {
                    if (emote.equals(TERMS_AGREE.getEmote())) {
                        agreeTerms(target, user);
                    } else if (emote.equals(TERMS_DISAGREE.getEmote())) {
                        disagreeTerms(target, user);
                    }

                } catch (WrongRegisterProgressException e) {
                    System.out.println("Use setTermsAgree method when RegisterProcess isn't " + CHECK_TERMS);
                    PrivateChannel pc = user.openPrivateChannel().complete();
                    pc.sendMessage("오류가 발생하였습니다! 다음 메세지를 관리자에게 보여주세요\n```" +
                            "Use setTermsAgree method when RegisterProcess isn't " + CHECK_TERMS + "\n" +
                            e.getMessage() + "```").queue();
                    pc.close().complete();
                }
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

    private void authorizeRiotAccount(PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        User user = event.getAuthor();
        Message message = event.getMessage();
        PrivateChannel pc = user.openPrivateChannel().complete();
        long discordId = user.getIdLong();

        //check user is exist in unRegisterUserSet
        if(unRegisterUserSet.isUserExistByDiscordId(discordId)) {
            UnRegisterUser unRegisterUser = unRegisterUserSet.getUserByDiscordId(discordId);
            //check User's progress is RIOT_NAME
            if(unRegisterUser.getProgress().equals(RegisterProgress.RIOT_NAME)) {
                String unreliableRiotId = message.getContentRaw();
                SummonerDto summoner;
                try {
                    summoner = summonerAPI.getByName(unreliableRiotId);
                } catch (DataNotFoundException e) {
                    pc.sendMessage("해당 이름을 가진 소환사를 찾을 수 없습니다! 다시시도해주세요!").queue();
                    pc.close().queue();
                    return;
                }
                MessageEmbed summonerProfileMessage;
                //insert UserInformation to summonerProfileMessage
                String thumbnailUrl;
                try {
                    ChampionMasteryDto mostChampion = championMasteryAPI.getChampionMasteryTopById(summoner.getId());
                    String mostChampionId = dataDragonAPI.getChampionIdByKey(mostChampion.getChampionId());
                    thumbnailUrl = dataDragonAPI.getChampionImageUrlById(mostChampionId);
                } catch (ZeroChampionMasteryPointException e) {
                    thumbnailUrl = dataDragonAPI.getProfileIconURL(summoner.getProfileIconId());
                }


                summonerProfileMessage = new EmbedBuilder()
                        .setColor(defaultEmbedColor)
                        .setThumbnail(thumbnailUrl)
                        .setTitle(summoner.getName() + "님 환영합니다!")
                        .addField("소환사님의 정보",
                                String.format("이름 : %s\n레벨 : %d",
                                        summoner.getName(),
                                        summoner.getSummonerLevel()), false)
                        .build();

                //send InformationMessage
                pc.sendMessage(summonerProfileMessage).queue();

                //prepare next step
                List<Integer> list = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));
                //If there is an existing summoner icon in the icon list for authentication,
                SummonerDto finalSummoner = summoner; //to Lambda
                list.removeIf(data -> (data == finalSummoner.getProfileIconId()));
                //get Random element at list for authorize
                int authorizeIconId = list.get(new Random().nextInt(list.size()));


                MessageEmbed riotAuthorizeMessage = new EmbedBuilder()
                        .setColor(defaultEmbedColor)
                        .setThumbnail(dataDragonAPI.getProfileIconURL(authorizeIconId))
                        .addField("라이엇 인증절차를 진행합니다!",
                                String.format(
                                "소환사님의 프로필 아이콘을 옆의 이미지처럼 바꾸어주세요!\n" +
                                "변경이 완료되면, %s 이모지를 달아주세요!", RIOT_CHANGE_ICON_DONE.getEmote()),
                                false)
                        .build();
                pc.sendMessage(riotAuthorizeMessage).queue((msg -> msg.addReaction(RIOT_CHANGE_ICON_DONE.getEmote()).queue()));
                try {
                    unRegisterUser.setAuthorizeIconId(authorizeIconId);
                } catch (WrongRegisterProgressException e) {
                    System.out.println("use setAuthorizeIconId method when RegisterProgress isn't" + RIOT_AUTHORIZE);
                    pc.sendMessage("오류가 발생하였습니다! 다음 메세지를 관리자에게 보여주세요\n```" +
                            "Use setAuthorizeIconId method when RegisterProcess isn't " + RIOT_AUTHORIZE + "\n" +
                            e.getMessage() + "```").queue();
                    pc.close().complete();
                    return;
                }

                try {
                    unRegisterUser.setRiotId(summoner.getId());
                } catch (WrongRegisterProgressException e) {
                    System.out.println("use setRiotId method when RegisterProgress isn't" + RIOT_AUTHORIZE);
                    pc.sendMessage("오류가 발생하였습니다! 다음 메세지를 관리자에게 보여주세요\n```" +
                            "Use setRiotId method when RegisterProcess isn't " + RIOT_AUTHORIZE + "\n" +
                            e.getMessage() + "```").queue();
                    pc.close().complete();
                    return;
                }

                //nextStep
                unRegisterUser.nextStep();
            }
        }
    }

    private void checkProfileIconChanged(PrivateMessageReactionAddEvent event) {
        if (Objects.requireNonNull(event.getUser()).isBot()) return;

        User user = event.getUser();
        assert user != null;
        PrivateChannel pc = user.openPrivateChannel().complete();
        long discordId = event.getUserIdLong();

        UnRegisterUser unRegisterUser = unRegisterUserSet.getUserByDiscordId(discordId);
        if (unRegisterUser.checkProgress(RIOT_AUTHORIZE)) {
            if (event.getReactionEmote().getName().equals(RIOT_CHANGE_ICON_DONE.getEmote())) {
                int authorizeIconId;
                try {
                    authorizeIconId = unRegisterUser.getAuthorizeIconId();
                } catch (WrongRegisterProgressException e) {
                    System.out.println("use getAuthorizeIconId method when RegisterProgress isn't" + RIOT_AUTHORIZE);
                    pc.sendMessage("오류가 발생하였습니다! 다음 메세지를 관리자에게 보여주세요\n```" +
                            "Use getAuthorizeIconId method when RegisterProcess isn't " + CHECK_TERMS + "\n" +
                            e.getMessage() + "```").queue();
                    pc.close().complete();
                    return;
                }

                try {
                    if (summonerAPI.getById(unRegisterUser.getRiotId()).getProfileIconId() == authorizeIconId) {
                        MessageEmbed messageEmbed = new EmbedBuilder()
                                .setColor(defaultEmbedColor)
                                .setTitle("축하드립니다!")
                                .addField(user.getName() + "님, 환영합니다!",
                                        "벳라이엇에 성공적으로 회원가입이 완료되었습니다!\n" +
                                                "이제 벳라이엇의 유저 서비스를 이용하실 수 있습니다!",
                                        false)
                                .build();
                        pc.sendMessage(messageEmbed).queue();

                        //prepare last step
                        unRegisterUser.nextStep();
                        registerComplete(unRegisterUser); //complete Register
                    } else {
                        pc.sendMessage("인증에 실패하였습니다! 재인증을 원하시면 라이엇 닉네임을 다시 입력해주세요!").queue();
                        unRegisterUser.cancelStep();
                    }
                } catch (WrongRegisterProgressException e) {
                    System.out.println("use getRiotId method when RegisterProgress isn't" + RIOT_AUTHORIZE);
                    pc.sendMessage("오류가 발생하였습니다! 다음 메세지를 관리자에게 보여주세요\n```" +
                            "Use getRiotId method when RegisterProcess isn't " + CHECK_TERMS + "\n" +
                            e.getMessage() + "```").queue();
                    pc.close().complete();
                }
            }
        }
    }

    private void registerComplete(UnRegisterUser unRegisterUser) {
        if(unRegisterUser.checkProgress(RegisterProgress.REGISTERED)) {
            UserVO user = new UserVO(unRegisterUser);
            dao.add(user);
        }
    }
}

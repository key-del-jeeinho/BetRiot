package com.xylope.betriot.layer.service.user.account.create;

import com.xylope.betriot.exception.DataNotFoundException;
import com.xylope.betriot.exception.WrongRegisterProgressException;
import com.xylope.betriot.layer.dataaccess.apis.riot.DataDragonAPI;
import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.logic.discord.listener.GuildMemberJoinListener;
import com.xylope.betriot.layer.logic.discord.listener.PrivateMessageReactionAddListener;
import com.xylope.betriot.layer.logic.discord.listener.PrivateMessageReceivedListener;
import com.xylope.betriot.layer.service.user.apis.UserSummonerAPI;
import com.xylope.betriot.layer.logic.discord.message.PrivateErrorMessageSender;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

import static com.xylope.betriot.layer.logic.discord.SpecialEmote.*;
import static com.xylope.betriot.layer.service.user.account.create.CreateAccountProgress.CHECK_TERMS;
import static com.xylope.betriot.layer.service.user.account.create.CreateAccountProgress.RIOT_AUTHORIZE;

public class CreateAccountService {
    Logger logger = LogManager.getLogger(this.getClass());
    private final BeforeCreateAccountUserSet beforeCreateAccountUserSet;
    @Setter
    private UserSummonerAPI summonerAPI;
    @Setter
    private DataDragonAPI dataDragonAPI;
    @Setter
    private PrivateErrorMessageSender errorMessageSender;
    @Setter
    UserDao dao;
    private static final Color DEFAULT_EMBED_COLOR = new Color(255, 200, 0);

    public CreateAccountService(GuildMemberJoinListener guildMemberJoinListener,
                                PrivateMessageReactionAddListener privateMessageReactionAddListener,
                                PrivateMessageReceivedListener privateMessageReceivedListener) {
        beforeCreateAccountUserSet = new BeforeCreateAccountUserSet();

        //Add repeat listener
        guildMemberJoinListener.addListener(this::checkIsUserUnRegistered);
        privateMessageReactionAddListener.addListener(this::checkTermsAgree);
        privateMessageReactionAddListener.addListener(this::checkProfileIconChanged);
        privateMessageReceivedListener.addListener(this::authorizeRiotAccount);
    }

    public void createUserAccount(Guild guild, User user) {
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
                .setColor(DEFAULT_EMBED_COLOR)
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
        BeforeCreateAccountUser beforeCreateAccountUser = new BeforeCreateAccountUser(user.getIdLong());
        beforeCreateAccountUser.nextStep(); //to CHECK_TERMS
        try {
            beforeCreateAccountUser.setTermsMessageId(messageId);
        } catch (WrongRegisterProgressException e) {
            String msg = "Use setTermsMessageId method when RegisterProcess isn't " + CHECK_TERMS + "\n" + e.getMessage();
            logger.log(Level.ERROR, msg);
            PrivateChannel pc = user.openPrivateChannel().complete();
            errorMessageSender.sendMessage(pc, msg);
            pc.close().queue();
        }
        beforeCreateAccountUserSet.add(beforeCreateAccountUser);
    }

    //RepeatListeners
    //Condition of Register (if User Join Guild which Applied BetRiot)
    public void checkIsUserUnRegistered(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        User user = event.getUser();
        if(!dao.isUserExist(user.getIdLong())) {
            createUserAccount(guild, user);
        }
    }

    //CheckPrivateMessageReaction is Emote of TermAgree or TermDisagree
    public void checkTermsAgree(PrivateMessageReactionAddEvent event) {
        long messageId = event.getMessageIdLong();
        User user = event.getChannel().getUser();

        boolean isTermsMessage =  beforeCreateAccountUserSet.isMessageTerms(messageId);
        BeforeCreateAccountUser target;

        if (isTermsMessage) {
            //Find unRegisterUser at queue to termsMessageId
            target = beforeCreateAccountUserSet.getUserByTermsMessageId(messageId);
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
                    String msg = "Use setTermsAgree method when RegisterProcess isn't " + CHECK_TERMS + "\n" + e.getMessage();
                    logger.log(Level.ERROR, msg);
                    PrivateChannel pc = user.openPrivateChannel().complete();
                    errorMessageSender.sendMessage(pc, msg);
                    pc.close().queue();
                }
            }
        }
    }

    private void agreeTerms(BeforeCreateAccountUser beforeCreateAccountUser, User user) throws WrongRegisterProgressException {
        beforeCreateAccountUser.setTermsAgree(true);

        PrivateChannel pc = user.openPrivateChannel().complete();
        pc.sendMessage("약관에 동의하셧습니다").queue();

        beforeCreateAccountUser.nextStep(); //to RIOT_NAME
        pc.sendMessage("소환사님의 라이엇 닉네임을 입력해주세요!").queue();
        pc.close().queue();
    }

    private void disagreeTerms(BeforeCreateAccountUser beforeCreateAccountUser, User user) throws WrongRegisterProgressException {
        beforeCreateAccountUser.setTermsAgree(false);

        PrivateChannel pc = user.openPrivateChannel().complete();
        pc.sendMessage("약관에 비동의하셧습니다! 해당 봇이 적용된 서버에 다시 들어오시거나, 뱃라이엇 회원가입 명령어를 통하여 회원가입을 재진행 하실 수 있습니다! 이용해주셔서 감사합니다 :)").queue();
        pc.close().queue();
        beforeCreateAccountUserSet.remove(beforeCreateAccountUser);
    }

    private void authorizeRiotAccount(PrivateMessageReceivedEvent event) {
        User user = event.getAuthor();
        Message message = event.getMessage();
        PrivateChannel pc = user.openPrivateChannel().complete();
        long discordId = user.getIdLong();

        //check user is exist in unRegisterUserSet
        if(beforeCreateAccountUserSet.isUserExistByDiscordId(discordId)) {
            BeforeCreateAccountUser beforeCreateAccountUser = beforeCreateAccountUserSet.getUserByDiscordId(discordId);
            //check User's progress is RIOT_NAME
            if(beforeCreateAccountUser.getProgress().equals(CreateAccountProgress.RIOT_NAME)) {
                String unreliableRiotId = message.getContentRaw();
                SummonerDto summoner;
                try {
                    if(checkRiotName(unreliableRiotId)) throw new DataNotFoundException();
                    summoner = summonerAPI.getByName(unreliableRiotId);
                } catch (DataNotFoundException e) {
                    pc.sendMessage("해당 이름을 가진 소환사를 찾을 수 없습니다! 다시시도해주세요!").queue();
                    pc.close().queue();
                    return;
                }
                //send InformationMessage
                MessageEmbed summonerProfileMessage = summonerAPI.getUserProfileMessage(summoner, DEFAULT_EMBED_COLOR);
                pc.sendMessage(summonerProfileMessage).queue();

                //prepare next step
                List<Integer> list = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));
                //If there is an existing summoner icon in the icon list for authentication,
                SummonerDto finalSummoner = summoner; //to Lambda
                list.removeIf(data -> (data == finalSummoner.getProfileIconId()));
                //get Random element at list for authorize
                int authorizeIconId = list.get(new Random().nextInt(list.size()));


                MessageEmbed riotAuthorizeMessage = new EmbedBuilder()
                        .setColor(DEFAULT_EMBED_COLOR)
                        .setThumbnail(dataDragonAPI.getProfileIconURL(authorizeIconId))
                        .addField("라이엇 인증절차를 진행합니다!",
                                String.format(
                                "소환사님의 프로필 아이콘을 옆의 이미지처럼 바꾸어주세요!\n" +
                                "변경이 완료되면, %s 이모지를 달아주세요!", RIOT_CHANGE_ICON_DONE.getEmote()),
                                false)
                        .build();
                pc.sendMessage(riotAuthorizeMessage).queue((msg -> {
                    msg.addReaction(RIOT_CHANGE_ICON_DONE.getEmote()).queue();
                    try {
                        beforeCreateAccountUser.setRiotMessageId(msg.getIdLong());
                    } catch (WrongRegisterProgressException e) {
                        errorMessageSender.sendMessage(pc, e.getMessage());
                        e.printStackTrace();
                    }
                }));
                try {
                    beforeCreateAccountUser.setAuthorizeIconId(authorizeIconId);
                } catch (WrongRegisterProgressException e) {
                    String msg = "Use setAuthorizeIconId method when RegisterProcess isn't " + RIOT_AUTHORIZE + "\n" + e.getMessage();
                    logger.log(Level.ERROR, msg);
                    errorMessageSender.sendMessage(pc, msg);
                    pc.close().queue();
                    return;
                }

                try {
                    beforeCreateAccountUser.setRiotId(summoner.getId());
                } catch (WrongRegisterProgressException e) {
                    String msg = "Use setRiotId method when RegisterProcess isn't " + RIOT_AUTHORIZE + "\n" + e.getMessage();
                    logger.log(Level.ERROR, msg);
                    errorMessageSender.sendMessage(pc, msg);
                    pc.close().queue();
                    return;
                }

                pc.close().queue();
                //to RIOT_AUTHORIZE
                beforeCreateAccountUser.nextStep();
            }
        }
    }

    private boolean checkRiotName(String name) {
        boolean result = false;
        if(3 <= name.length() && name.length() <= 16)
            result = !Pattern.matches("^[ㄱ-ㅎ가-힣a-zA-Z0-9]*$", name);
        return result;
    }

    private void checkProfileIconChanged(PrivateMessageReactionAddEvent event) {
        User user = event.getUser();
        assert user != null;
        PrivateChannel pc = user.openPrivateChannel().complete();
        long discordId = event.getUserIdLong();

        if(!beforeCreateAccountUserSet.isMessageRiot(event.getMessageIdLong())) return;

        BeforeCreateAccountUser beforeCreateAccountUser = beforeCreateAccountUserSet.getUserByDiscordId(discordId);
        if (beforeCreateAccountUser.checkProgress(RIOT_AUTHORIZE)) {
            if (event.getReactionEmote().getName().equals(RIOT_CHANGE_ICON_DONE.getEmote())) {
                int authorizeIconId;
                try {
                    authorizeIconId = beforeCreateAccountUser.getAuthorizeIconId();
                } catch (WrongRegisterProgressException e) {
                    String msg = "Use getAuthorizeIconId method when RegisterProcess isn't " + CHECK_TERMS + "\n" + e.getMessage();
                    logger.log(Level.ERROR, msg);
                    errorMessageSender.sendMessage(pc, msg);
                    pc.close().queue();
                    return;
                }

                try {
                    if (summonerAPI.getById(beforeCreateAccountUser.getRiotId()).getProfileIconId() == authorizeIconId) {
                        MessageEmbed messageEmbed = new EmbedBuilder()
                                .setColor(DEFAULT_EMBED_COLOR)
                                .setTitle("축하드립니다!")
                                .addField(user.getName() + "님, 환영합니다!",
                                        "벳라이엇에 성공적으로 회원가입이 완료되었습니다!\n" +
                                                "이제 벳라이엇의 유저 서비스를 이용하실 수 있습니다!",
                                        false)
                                .build();
                        pc.sendMessage(messageEmbed).queue();

                        //prepare last step
                        beforeCreateAccountUser.nextStep(); //to REGISTERED
                        registerComplete(beforeCreateAccountUser); //complete Register
                    } else {
                        pc.sendMessage("인증에 실패하였습니다! 재인증을 원하시면 라이엇 닉네임을 다시 입력해주세요!").queue();
                        beforeCreateAccountUser.cancelStep();
                    }
                } catch (WrongRegisterProgressException e) {
                    String msg = "Use getRiotId method when RegisterProcess isn't " + CHECK_TERMS + "\n" + e.getMessage();
                    logger.log(Level.ERROR, msg);
                    errorMessageSender.sendMessage(pc, msg);
                    pc.close().queue();
                    return;
                }
                pc.close().queue();
            }
        }
    }

    private void registerComplete(BeforeCreateAccountUser beforeCreateAccountUser) {
        if(beforeCreateAccountUser.checkProgress(CreateAccountProgress.REGISTERED)) {
            UserVO user = new UserVO(beforeCreateAccountUser);
            dao.add(user);
        }
    }
}

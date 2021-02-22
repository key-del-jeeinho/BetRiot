package com.xylope.betriot.layer.service.listener;

import com.xylope.betriot.layer.dataaccess.SummonerAPI;
import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import com.xylope.betriot.layer.discord.MessageIdCallback;
import com.xylope.betriot.layer.logic.dao.UserDao;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UserJoinListener extends ListenerAdapter {
    @Setter
    private UserDao dao;
    @Setter
    private SummonerAPI summonerAPI;
    private List<Long> signUpMessageIdQueue = new ArrayList<>();
    private List<User> riotAuthorizeUserQueue = new ArrayList<>();
    private static final String ACCEPT_EMOTE = "✅";
    private static final String DENIED_EMOTE = "❎";

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if(event.getUser().isBot()) return;

        User user = event.getUser();
        Guild server = event.getGuild();

        System.out.println("뉴비 등장!");
        long userId = user.getIdLong();
        if(!dao.isUserExist(userId)) {
            System.out.println("신규 유저");
            sendSignUpMessage(server, user);
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(event.getMessage().getContentRaw().equals("뱃라이엇 DB초기화")) {//DirtyCode to Test
            dao.removeAll();
            event.getChannel().sendMessage("데이터베이스가 전부 초기화되었습니다!").queue();
        }

        User user = event.getAuthor();
        Message message = event.getMessage();

        if(riotAuthorizeUserQueue.contains(user)) {
            String riotName = message.getContentRaw();
            SummonerDto dto = summonerAPI.getByName(riotName);
        }
    }

    @Override
    public void onPrivateMessageReactionAdd(@NotNull PrivateMessageReactionAddEvent event) {
        if(event.getUser().isBot()) return;
        long messageId = event.getMessageIdLong();
        User user = event.getChannel().getUser();
        if (signUpMessageIdQueue.contains(messageId)) {
            String emote = event.getReactionEmote().getName();
            boolean isPolicyEmote = true;
            System.out.println(emote);
            switch (emote) {
                case ACCEPT_EMOTE:
                    signUpUser(user);
                    break;
                case DENIED_EMOTE:
                    unSignUpUser(user);
                    break;
                default: isPolicyEmote = false;
            }
        }
    }

    public void signUpUser(User user) {
        sendPrivateMessage(user, user.getAsMention() + "뱃라이엇 약관에 동의하셧습니다");
        dao.add(new com.xylope.betriot.layer.logic.vo.User(user.getIdLong(), null, 10000));
        sendPrivateMessage(user, user.getName() + "님의 롤 닉네임을 입력해주세요!");
    }

    public void unSignUpUser(User user) {
        sendPrivateMessage(user, user.getAsMention() + "뱃라이엇 약관에 비동의하셧습니다. 뱃라이엇의 기능을 이용하실 수 없습니다");
        sendPrivateMessage(user, "언제든 서버에 나갔다 들어오시면 회원가입절차를 다시 진행할 수 있습니다!");
    }

    public void sendSignUpMessage(Guild server, User user) {
        String message = String.format(
                "%s님이 계신 %s서버는 벳라이엇봇이 적용된 서버입니다.\n" +
                        "벳라이엇은 여러분의 LOL 경기를 디스코드에 중계하고, 다른 유저분들이 해당 경기에 포인트를 배팅할 수 있는 봇 입니다.\n" +
                        "해당 봇에 가입하시려면 해당 약관에 %s 이모지를 달아주신 뒤, 자신의 롤 닉네임을 입력하여 가입절차를 진행해주세요.\n " +
                        "해당 봇에 가입을 희망하지 않으신다면, %s 이모지를 달아주세요.", user.getAsMention(), server.getName(), ACCEPT_EMOTE, DENIED_EMOTE
        );
        String policy = "이용약관입니다.";

        EmbedBuilder eb = new EmbedBuilder()
        .setTitle("BetRiot 회원가입", null)
        .setColor(new Color(0xF14668))
        .setDescription("리그오브레전드 배팅 봇 BetRiot 에 오신것을 환영합니다!")
        .addField("안녕하세요", message, false)
        .addField("이용약관", policy, false)
        .setFooter("made by Xylope", null);
        sendEmbedPrivateMessage(user, eb.build(),
                signUpMessageIdQueue::add,
                ACCEPT_EMOTE, DENIED_EMOTE);
    }

    public void sendEmbedPrivateMessage(User user, MessageEmbed content, String... reactions) {//추후 MessageSender 클래스를 생성해서 이동시킬것
        sendEmbedPrivateMessage(user, content, (discordId)->{}, reactions);
    }

    public void sendEmbedPrivateMessage(User user, MessageEmbed content, MessageIdCallback callback, String... reactions) {//추후 MessageSender 클래스를 생성해서 이동시킬것
        user.openPrivateChannel().queue((channel) -> channel.sendMessage(content).queue(message -> {
            callback.doSomething(message.getIdLong());
            for(String reaction :reactions)
                message.addReaction(reaction).queue();
        }));
    }

    public void sendPrivateMessage(User user, String content, String... reactions) {//추후 MessageSender 클래스를 생성해서 이동시킬것
        sendPrivateMessage(user, content, (discordId)->{}, reactions);
    }

    public void sendPrivateMessage(User user, String content, MessageIdCallback callback, String... reactions) {//추후 MessageSender 클래스를 생성해서 이동시킬것
        user.openPrivateChannel().queue((channel) -> channel.sendMessage(content).queue(message -> {
            callback.doSomething(message.getIdLong());
            for(String reaction :reactions)
                message.addReaction(reaction).queue();
        }));
    }
}

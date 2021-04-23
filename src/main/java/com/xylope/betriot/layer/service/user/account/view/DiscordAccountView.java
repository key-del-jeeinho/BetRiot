package com.xylope.betriot.layer.service.user.account.view;

import com.xylope.betriot.layer.logic.discord.SpecialEmote;
import com.xylope.betriot.layer.logic.discord.message.PrivateEmbedMessageSenderWithCallback;
import com.xylope.betriot.layer.logic.discord.message.PrivateMessageSender;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@AllArgsConstructor
public class DiscordAccountView implements AccountView{
    private static final Color COLOR = new Color(68, 82, 255);
    private final PrivateMessageSender<String> privateMessageSender;
    private final PrivateEmbedMessageSenderWithCallback privateEmbedMessageSenderWithCallback;
    private final String adminEmail;

    @Override
    public void sendRegisterView(PrivateChannel pc) {
        String welcomeMsg = String.format(
                "%s님이 계신 BetRiot BetRiot 개발 테스트 서버 서버는 벳라이엇봇이 적용된 서버입니다.\n" +
                        "벳라이엇은 여러분의 LOL 경기를 디스코드에 중계하고, 다른 유저분들이 해당 경기에 포인트를 배팅할 수 있는 봇 입니다."
                , pc.getUser().getAsMention()
        );
        MessageEmbed message = new EmbedBuilder()
                .setColor(COLOR)
                .setTitle("BetRiot 회원가입")
                .setDescription("리그오브레전드 배팅 봇 BetRiot 의 가입 안내입니다!")
                .addField("안녕하세요!", welcomeMsg, false)
                .build();
        privateEmbedMessageSenderWithCallback.sendMessage(pc, message, msg->{});
    }

    @Override
    public long sendPolicyView(PrivateChannel pc) {
        String policyFieldA = String.format(
                "이용 약관에 동의하여 해당 봇에 가입하시려면 해당 약관에 %s 이모지를 달아주신 뒤, 자신의 롤 닉네임을 입력하여 가입절차를 진행해주세요.\n" +
                        "이용 약관에 동의하지 않거나, 해당 봇에 가입을 희망하지 않으신다면, %s 이모지를 달아주세요.",
                SpecialEmote.TERMS_AGREE.getEmote(), SpecialEmote.TERMS_DISAGREE.getEmote());

        String policyFieldB = "이용약관입니다";

        MessageEmbed message = new EmbedBuilder()
                .setColor(COLOR)
                .setTitle("BetRiot 이용약관 안내")
                .addField("약관 동의/거부 방법", policyFieldA, false)
                .addField("이용약관", "```\n" + policyFieldB + "\n```", false)
                .setFooter(String.format("문의사항은 %s 로 말씀해주세요", adminEmail))
                .build();

        AtomicLong messageId = new AtomicLong(-1);
        privateEmbedMessageSenderWithCallback.sendMessage(pc, message,
                msg -> messageId.set(msg.getIdLong()));

        while(messageId.get() == -1); //람다 내의 처리가 전부 끝날때가지 기다린다.
        return messageId.get();
    }

    @Override
    public long sendAuthorizeRiotNameView(PrivateChannel pc) {
        MessageEmbed message = new EmbedBuilder()
                .setColor(COLOR)
                .setTitle(pc.getUser().getName() + "님의 라이엇 닉네임을 입력해주세요!")
                .build();
        AtomicLong messageId = new AtomicLong(-1);

        privateEmbedMessageSenderWithCallback.sendMessage(pc, message,
                (msg) -> messageId.set(msg.getIdLong()));
        while (messageId.get() == -1);

        return messageId.get();
    }

    @Override
    public long sendAuthorizeRiotAccountView(PrivateChannel pc, String iconUrl) {
        String description = String.format(
                "소환사 님의 프로필 아이콘을 옆의 이미지처럼 바꾸어 주세요!\n변경이 완료되면, %s이모지를 달아주세요!",
                SpecialEmote.RIOT_CHANGE_ICON_DONE.getEmote());
        MessageEmbed message = new EmbedBuilder()
                .setTitle("라이엇 인증 절차를 진행 합니다!")
                .setThumbnail(iconUrl)
                .addField("", description, false)
                .build();

        AtomicLong messageId = new AtomicLong(-1);

        privateEmbedMessageSenderWithCallback.sendMessage(pc, message,
                (msg) -> messageId.set(msg.getIdLong()));
        while (messageId.get() == -1);

        return messageId.get();
    }

    @Override
    public void sendUserAcceptPolicyView(PrivateChannel pc) {
        privateMessageSender.sendMessage(pc, String.format("이용 약관에 동의하셧습니다! (%s)", getPolicyTimeStamp()));
    }

    @Override
    public void sendUserDenyPolicyView(PrivateChannel pc) {

        privateMessageSender.sendMessage(pc, String.format("이용 약관에 비동의하셧습니다! (%s)", getPolicyTimeStamp()));
    }

    @Override
    public void sendRiotAuthorizeSucessView(PrivateChannel pc) {
        MessageEmbed message = new EmbedBuilder()
                .setTitle(pc.getUser().getName() + "님, 환영합니다!")
                .setColor(COLOR)
                .addField("회원가입이 완료되셧습니다!", "이제 뱃라이엇을 통해서 자신의 경기를 배팅하고, 배팅에 참여할 수 있습니다!\n 일확천금의 기회를 놓치지 마세요!\n행운을빕니다..", false)
                .setFooter("시작티어 : 아이언")
                .build();
        privateEmbedMessageSenderWithCallback.sendMessage(pc, message, msg->{});
    }

    @Override
    public void sendRiotAuthorizeFailureView(PrivateChannel pc) {
        String message = "라이엇 인증에 실패하셧습니다! 회원가입 절차를 처음부터 다시진행해주세요.";
        privateMessageSender.sendMessage(pc, message);
    }

    @Override
    public void sendRemoveAccountView(PrivateChannel pc) {
        String message = "회원탈퇴가 정상적으로 완료되었습니다!";
        privateMessageSender.sendMessage(pc, message);
    }

    private String getPolicyTimeStamp() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd - hh:mm");
        return format.format(date);
    }
}

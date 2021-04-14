package com.xylope.betriot.layer.service.user_v2.account.view;

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
public class AccountView {
    private static final Color COLOR = new Color(68, 82, 255);
    private final PrivateMessageSender<String> privateMessageSender;
    private final PrivateEmbedMessageSenderWithCallback privateEmbedMessageSenderWithCallback;
    private final String adminEmail;

    public long sendPolicyView(PrivateChannel pc) {
        String policyFieldA = String.format(
                "해당 봇에 가입하시려면 해당 약관에 %s 이모지를 달아주신 뒤, 자신의 롤 닉네임을 입력하여 가입절차를 진행해주세요.\n" +
                "해당 봇에 가입을 희망하지 않으신다면, %s 이모지를 달아주세요.",
                SpecialEmote.TERMS_AGREE.getEmote(), SpecialEmote.TERMS_DISAGREE.getEmote());

        String policyFieldB = String.format(
                ""
        );

        MessageEmbed message = new EmbedBuilder()
                .setColor(COLOR)
                .setTitle("BetRiot 이용약관 안내")
                .addField("이용약관", policyFieldA, false)
                .addField("", "```" + policyFieldB + "```", false)
                .setFooter(String.format("문의사항은 %s 로 말씀해주세요", adminEmail))
                .build();
        AtomicLong messageId = new AtomicLong();

        privateEmbedMessageSenderWithCallback.sendMessage(pc, message,
                (msg) -> messageId.set(msg.getIdLong()));

        return messageId.get();
    }

    public long sendAuthorizeRiotNameView(PrivateChannel pc) {
        MessageEmbed message = new EmbedBuilder()
                .setColor(COLOR)
                .setTitle(pc.getUser().getAsMention() + "님의 라이엇 닉네임을 입력해주세요!")
                .build();
        AtomicLong messageId = new AtomicLong();

        privateEmbedMessageSenderWithCallback.sendMessage(pc, message,
                (msg) -> messageId.set(msg.getIdLong()));

        return messageId.get();
    }

    public long sendAuthorizeRiotAccountView(PrivateChannel pc, String iconUrl) {
        String description = String.format(
                "소환사 님의 프로필 아이콘을 옆의 이미지처럼 바꾸어 주세요!\n변경이 완료되면, %s이모지를 달아주세요!",
                SpecialEmote.RIOT_CHANGE_ICON_DONE.getEmote());
        MessageEmbed message = new EmbedBuilder()
                .setTitle("라이엇 인증 절차를 진행 합니다!")
                .setThumbnail(iconUrl)
                .addField("", description, false)
                .build();

        AtomicLong messageId = new AtomicLong();

        privateEmbedMessageSenderWithCallback.sendMessage(pc, message,
                (msg) -> messageId.set(msg.getIdLong()));

        return messageId.get();
    }

    public void sendUserAcceptPolicyView(PrivateChannel pc) {
        privateMessageSender.sendMessage(pc, String.format("이용 약관에 동의하셧습니다! (%s)", getPolicyTimeStamp()));
    }

    public void sendUserDenyPolicyView(PrivateChannel pc) {

        privateMessageSender.sendMessage(pc, String.format("이용 약관에 비동의하셧습니다! (%s)", getPolicyTimeStamp()));
    }

    public void sendRegisterView(PrivateChannel pc) {
        String welcomeMsg = String.format(
                "%s님이 계신 BetRiot BetRiot 개발 테스트 서버 서버는 벳라이엇봇이 적용된 서버입니다.\n" +
                "벳라이엇은 여러분의 LOL 경기를 디스코드에 중계하고, 다른 유저분들이 해당 경기에 포인트를 배팅할 수 있는 봇 입니다."
                , pc.getUser().getAsMention()
        );
        MessageEmbed message = new EmbedBuilder()
                .setTitle("BetRiot 회원가입")
                .setDescription("리그오브레전드 배팅 봇 BetRiot 의 가입 안내입니다!")
                .addField("안녕하세요!", welcomeMsg, false)
                .build();
        privateEmbedMessageSenderWithCallback.sendMessage(pc, message, msg->{});
    }

    private String getPolicyTimeStamp() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd (hh:mm)");
        return format.format(date);
    }
}

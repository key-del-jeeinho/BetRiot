package com.xylope.betriot.layer.logic.discord.message;

import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class ChannelErrorMessageSender implements ChannelMessageSender<String>{
    @Setter
    private String adminEmail;

    private MessageEmbed getErrorMessage(String msg) {
        Color embedColor = new Color(232, 29, 53);
        return new EmbedBuilder()
                .setColor(embedColor)
                .setTitle(":/")
                .addField("오류가 발생하였습니다! 다음 메세지를 관리자 메일로 보내주세요", "```fix\n" + msg + "\n```", false)
                .setFooter("관리자 이메일 : " + adminEmail)
                .build();
    }

    @Override
    public void sendMessage(TextChannel channel, String msg) {
        channel.sendMessage(getErrorMessage(msg)).queue();
    }
}

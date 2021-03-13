package com.xylope.betriot.layer.service.command.custom.notice;

import com.xylope.betriot.ApplicationBooter;
import com.xylope.betriot.BetRiotApplication;
import com.xylope.betriot.layer.service.command.AbstractCommand;
import com.xylope.betriot.layer.service.command.LeafCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

public class NoticeAllCommand extends LeafCommand {
    public NoticeAllCommand() {
        //usage
        //뱃라이엇 {trigger_noticeCommand} <title> <message>...
        //뱃라이엇 {trigger_noticeCommand} <message>
        super(
                new AbstractCommand() {
                    @Override
                    public void execute(GuildChannel channel, User sender, String... args) {
                        JDA jda = ApplicationBooter.CONTEXT.getBean(BetRiotApplication.class).getJda();
                        String title = "공지사항";

                        if(args.length >= 2) {
                            title = args[0];
                            args = Arrays.copyOfRange(args, 1, args.length); //args 의 0번째 인덱스(title) 를 지운다.
                        }

                        StringBuffer sb = new StringBuffer();
                        for(String arg : args)
                            sb.append(arg).append(" ");

                        MessageEmbed message = new EmbedBuilder()
                                .setTitle("뱃라이엇 공지가 도착했습니다!")
                                .setColor(new Color(148, 255, 88))
                                .addField(title, sb.toString(), false)
                                .setFooter("공지는 Default Channel 에 전송됩니다!")
                                .build();
                        jda.getGuilds().forEach(
                                guild -> {
                                    Objects.requireNonNull(guild.getDefaultChannel()).sendMessage(message).queue();
                                }
                        );
                    }
                });

    }
}

package com.xylope.betriot.layer.service.command.custom;

import com.xylope.betriot.layer.service.command.AbstractCommand;
import com.xylope.betriot.layer.service.command.LeafCommand;
import com.xylope.betriot.layer.service.user.message.ChannelEmbedMessageSender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

public class HelpCommand extends LeafCommand {
    public HelpCommand(ChannelEmbedMessageSender channelEmbedMessageSender, String email) {
        super(new AbstractCommand() {
            @Override
            public void execute(GuildChannel channel, User sender, String... args) {
                Guild guild = channel.getGuild();
                String channelId = channel.getId();
                TextChannel textChannel = guild.getTextChannelById(channelId);
                String intro = "뱃라이엇은 라이엇 API 를 활용해보기위해 개발된 디스코드 봇입니다" +
                        "자신이나 다른사람의 전적을 볼 수 있고 자신의 전적을 공유할 수 있습니다 (" +
                        "또한 해당 전적에 트위치 배팅처럼 뱃라이엇 포인트를 걸 수 있습니다 (개발중)" +
                        "이외에도 인게임 퀘스트 등 여러 기능을 준비중이니 기대해주세요!";
                String commandHelp =
                        "`뱃라이엇 회원가입` : 뱃라이엇 회원이 아닐경우, 뱃라이엇에 가입합니다\n" +
                        "`뱃라이엇 회원탈퇴` : 뱃라이엇 회원일경우, 뱃라이엇에서 탈퇴합니다\n" +
                        "`벳라이엇 프로필` : 자신의 프로필을 확인합니다\n" +
                        "`벳라이엇 프로필 <멘션>` : 해당 유저가 뱃라이엇 회원일경우, 그 유저의 프로필을 확인합니다\n" +
                        "`뱃라이엇 전적` : (개발중)";
                MessageEmbed helpMessage = new EmbedBuilder()
                        .setTitle("뱃라이엇 도움말")
                        .addField("뱃라이엇이란?", intro, false)
                        .addField("명령어", commandHelp, false)
                        .setFooter("developed bt Xylope " + email)
                        .build();
                assert textChannel != null;
                channelEmbedMessageSender.sendMessage(textChannel, helpMessage);
            }
        });
    }
}

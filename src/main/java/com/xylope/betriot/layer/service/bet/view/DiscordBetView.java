package com.xylope.betriot.layer.service.bet.view;

import com.xylope.betriot.layer.dataaccess.apis.discord.JdaAPI;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet.model.BetDto;
import com.xylope.betriot.layer.service.bet.model.BetUserVO;
import com.xylope.betriot.layer.service.bet.model.WinOrLose;
import com.xylope.betriot.layer.service.bet.view.printer.Printer;
import com.xylope.betriot.layer.service.message.ChannelEmbedMessageSender;
import com.xylope.betriot.layer.service.message.ChannelMessageSenderImpl;
import com.xylope.betriot.layer.service.message.PrivateEmbedMessageSender;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class DiscordBetView implements BetView{
    private static Color color = new Color(36, 23, 255);
    private static Color errColor = new Color(255, 23, 77);

    private final JdaAPI jdaAPI;
    private final Printer printer;
    private final ChannelEmbedMessageSender channelEmbedMessageSender;
    private final ChannelMessageSenderImpl channelMessageSender;
    private final PrivateEmbedMessageSender privateEmbedMessageSender;

    @Override
    public void sendReserveBetView(UserVO user) {
        User discordUser  = jdaAPI.getUserById(user.getDiscordId());
        String publisherName = discordUser.getName();

        MessageEmbed message = new EmbedBuilder()
                .setColor(color)
                .setTitle("배팅 개설 에약에 성공하셧습니다!")
                .addField("개설자", publisherName, false)
                .build();

        privateEmbedMessageSender.sendMessage(discordUser.openPrivateChannel().complete(), message); //문제위치
    }

    @Override
    public void sendStartBetView(BetDto bet) {
        String publisherName = getPublisherNameByBetDto(bet);
        String participationCommand = String.format("뱃라이엇 배팅 참가 %s <승리 | 패배> <돈>", bet.getId());

        MessageEmbed message = new EmbedBuilder()
                .setColor(color)
                .setTitle("배팅이 개설되었습니다!")
                .addField("개설자", publisherName, true)
                .addField("배팅 ID", Integer.toString(bet.getId()), true)
                .addField("", String.format("해당 배팅에 참여하시려면\n `%s` \n명령어를 입력해주세요!", participationCommand), false)
                .build();

        channelEmbedMessageSender.sendMessage(bet.getRelayChannel(), message);
    }

    @Override
    public void sendOpenBetParticipationView(BetDto bet) {
        String message = String.format("이제 %s 님의 배팅에 참여하실 수 있습니다!", getPublisherNameByBetDto(bet));

        channelMessageSender.sendMessage(bet.getRelayChannel(), message);
    }

    @Override
    public void sendCloseBetParticipationView(BetDto bet) {
        String message = String.format("이제 %s 님의 배팅에 참여하실 수 없습니다!", getPublisherNameByBetDto(bet));

        channelMessageSender.sendMessage(bet.getRelayChannel(), message);
    }

    @Override
    public void sendGiveRewardToWinnersView(BetDto bet, WinOrLose isPublisherWinOrLose) {
        Map<BetUserVO, WinOrLose> participants = bet.getParticipants();
        List<String> nameOfParticipants = new ArrayList<>();
        int totalMoney = 0;
        int winnerMoney = 0;
        int loserMoney = 0;

        for (BetUserVO user : bet.getParticipants().keySet()) {
            WinOrLose betWhere = bet.getParticipants().get(user);
            long discordId = user.getUser().getDiscordId();
            if(betWhere.equals(isPublisherWinOrLose)) {
                nameOfParticipants.add(jdaAPI.getUserById(discordId).getName() + "(승리)");
                winnerMoney += user.getMoney();
            } else
                nameOfParticipants.add(jdaAPI.getUserById(discordId).getName() + "(패배)");
                loserMoney += user.getMoney();
            totalMoney += user.getMoney();
        }

        StringBuffer displayParticipants = new StringBuffer();
        for(String nameOfParticipant: nameOfParticipants) {
            displayParticipants.append(nameOfParticipant).append("\n");
        }

        MessageEmbed message = new EmbedBuilder()
                .setColor(color)
                .setTitle(String.format("%s 님의 배팅결과가 나왔습니다!", getPublisherNameByBetDto(bet)))
                .addField("매치결과", isPublisherWinOrLose.getDisplayStatus(), false)
                .addField("참여인원", String.valueOf(bet.getParticipants().size()), false)
                .addField("총 배팅액", String.valueOf(totalMoney), true)
                .addField("승리한 배팅액", String.valueOf(winnerMoney), true)
                .addField("패배한 배팅액", String.valueOf(loserMoney), true)
                .addField("배팅 참여자", String.valueOf(displayParticipants), false)
                .build();;

        channelEmbedMessageSender.sendMessage(bet.getRelayChannel(), message);
    }

    @Override
    public void sendEndBetView(int betId, BetDto bet) {
        String message = String.format("%s 님의 배팅이 종료되었습니다", getPublisherNameByBetDto(bet));

        channelMessageSender.sendMessage(bet.getRelayChannel(), message);
    }

    @Override
    public void sendMatchNotFoundView(BetDto bet) {
        MessageEmbed message = new EmbedBuilder()
                .setColor(errColor)
                .setTitle(getMatchCancelMessageTitle(bet))
                .addField("", "제한시간 내에 인게임 매칭이 감지되지 않아 배팅 개설이 취소되었습니다!", false)
                .build();;

        channelEmbedMessageSender.sendMessage(bet.getRelayChannel(), message);
    }

    @Override
    public void sendMatchExceedTimeLimitView(BetDto bet) {
        MessageEmbed message = new EmbedBuilder()
                .setColor(errColor)
                .setTitle(getMatchCancelMessageTitle(bet))
                .addField("", "잡힌지 일정시간이 지난 매칭으로 배팅을 개설시킬 수 없습니다!", false)
                .build();;

        channelEmbedMessageSender.sendMessage(bet.getRelayChannel(), message);
    }

    @Override
    public void sendUserParticipationBettingView(BetDto bet, BetUserVO user) {
        User discordUser = jdaAPI.getUserById(user.getUser().getDiscordId());
        User discordPublisher = jdaAPI.getUserById(bet.getPublisher().getDiscordId());
        MessageEmbed message = new EmbedBuilder()
                .setColor(color)
                .setTitle("배팅에 성공적으로 참여하셧습니다!")
                .addField("배팅 ID", String.valueOf(bet.getId()), true)
                .addField("배팅 개설자", discordPublisher.getName(), true)
                .addField("배팅 참여자", discordUser.getName(), true)
                .build();
        privateEmbedMessageSender.sendMessage(discordUser.openPrivateChannel().complete(), message);
    }

    @Override
    public void sendNotEnoughMoneyToParticipationView(BetDto bet, UserVO user) {
        String userName = jdaAPI.getUserById(user.getDiscordId()).getName();
        String message = String.format("%s 님의 돈이 부족합니다! 소지금 : %d", userName, user.getMoney());
        channelMessageSender.sendMessage(bet.getRelayChannel(), message);
    }

    @Override
    public void sendParticipationAlreadyCloseView(BetDto bet) {
        String publisherName = jdaAPI.getUserById(bet.getPublisher().getDiscordId()).getName();
        String message = String.format("이미 %s님의 배팅참여기간이 종료되었습니다!", publisherName);
        System.out.println(bet.getProgress());
        channelMessageSender.sendMessage(bet.getRelayChannel(), message);
    }

    @Override
    public void sendDuplicateParticipationView(BetDto bet, UserVO user) {
        String userName = jdaAPI.getUserById(user.getDiscordId()).getName();
        String message = String.format("%s 님은 이미 해당 배팅에 참여하셧습니다!", userName);
        channelMessageSender.sendMessage(bet.getRelayChannel(), message);
    }

    private String getPublisherNameByBetDto(BetDto bet) {
        return jdaAPI.getUserById(bet.getPublisher().getDiscordId()).getName();
    }

    private String getMatchCancelMessageTitle(BetDto bet) {
        return String.format("%s님의 배팅 개설에 실패하였습니다 :(", getPublisherNameByBetDto(bet));
    }
}

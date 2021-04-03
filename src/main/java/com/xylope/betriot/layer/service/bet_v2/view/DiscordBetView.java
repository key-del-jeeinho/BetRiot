package com.xylope.betriot.layer.service.bet_v2.view;

import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet_v2.model.BetDto;
import com.xylope.betriot.layer.service.bet_v2.model.WinOrLose;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class DiscordBetView implements BetView{

    @Override
    public void sendReserveBetView(UserVO user) {
        MessageEmbed message = new EmbedBuilder()
                .setTitle("배팅 개설 에약에 성공하셧습니다!")
                .build();
    }

    @Override
    public void sendStartBetView(BetDto bet) {

    }

    @Override
    public void sendOpenBetParticipationView(BetDto bet) {

    }

    @Override
    public void sendCloseBetParticipationView(BetDto bet) {

    }

    @Override
    public void sendGiveRewardToWinnersView(BetDto bet, WinOrLose isPublisherWinOrLose) {

    }

    @Override
    public void sendEndBetView(int betId, BetDto bet) {
        /*

                privateEmbedMessageSender.sendMessage(discordUser.openPrivateChannel().complete(),
                        new EmbedBuilder()
                                .setTitle("배팅 개설에 실패하였습니다 :(")
                                .addField("", "제한시간 내에 인게임 매칭이 감지되지 않아 배팅 개설이 취소되었습니다!", false)
                                .build());
         */
    }

    @Override
    public void sendMatchNotFoundView(BetDto bet) {

    }
}

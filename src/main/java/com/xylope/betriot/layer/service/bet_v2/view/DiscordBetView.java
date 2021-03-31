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
    public void sendStartBetView(BetDto dto) {

    }

    @Override
    public void sendOpenBetParticipationView() {

    }

    @Override
    public void sendCloseBetParticipationView() {

    }

    @Override
    public void sendGiveRewardToWinnersView(BetDto dto, WinOrLose isPublisherWinOrLose) {

    }

    @Override
    public void sendEndBetView(int betId) {

    }
}

package com.xylope.betriot.layer.service.bet_v2.view;

import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet_v2.model.BetDto;
import com.xylope.betriot.layer.service.bet_v2.model.WinOrLose;

public interface BetView {
    void sendReserveBetView(UserVO user);
    void sendStartBetView(BetDto bet);
    void sendOpenBetParticipationView(BetDto bet);
    void sendCloseBetParticipationView(BetDto bet);
    void sendGiveRewardToWinnersView(BetDto bet, WinOrLose isPublisherWinOrLose);
    void sendEndBetView(int betId, BetDto bet);

    void sendMatchNotFoundView(BetDto bet);
}

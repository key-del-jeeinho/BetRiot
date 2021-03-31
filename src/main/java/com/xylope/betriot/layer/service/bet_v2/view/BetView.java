package com.xylope.betriot.layer.service.bet_v2.view;

import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet_v2.model.BetDto;
import com.xylope.betriot.layer.service.bet_v2.model.WinOrLose;

public interface BetView {
    void sendReserveBetView(UserVO user);
    void sendStartBetView(BetDto dto);
    void sendOpenBetParticipationView();
    void sendCloseBetParticipationView();
    void sendGiveRewardToWinnersView(BetDto dto, WinOrLose isPublisherWinOrLose);
    void sendEndBetView(int betId);
}

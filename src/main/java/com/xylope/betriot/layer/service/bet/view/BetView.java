package com.xylope.betriot.layer.service.bet.view;

import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet.model.BetDto;
import com.xylope.betriot.layer.service.bet.model.BetUserVO;
import com.xylope.betriot.layer.service.bet.model.WinOrLose;

public interface BetView {
    void sendReserveBetView(UserVO user);
    void sendStartBetView(BetDto bet);
    void sendOpenBetParticipationView(BetDto bet);
    void sendCloseBetParticipationView(BetDto bet);
    void sendGiveRewardToWinnersView(BetDto bet, WinOrLose isPublisherWinOrLose);
    void sendEndBetView(int betId, BetDto bet);

    void sendMatchNotFoundView(BetDto bet);
    void sendMatchExceedTimeLimitView(BetDto bet);

    void sendUserParticipationBettingView(BetDto bet, BetUserVO user);
    void sendNotEnoughMoneyToParticipationView(BetDto bet, UserVO user);

    void sendParticipationAlreadyCloseView(BetDto bet);
    void sendDuplicateParticipationView(BetDto bet, UserVO user);

    void sendBetAlreadyCreatedView(UserVO user);
}

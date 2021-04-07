package com.xylope.betriot.layer.service.bet.view;

import com.xylope.betriot.layer.dataaccess.apis.discord.JdaAPI;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet.model.BetDto;
import com.xylope.betriot.layer.service.bet.model.BetUserVO;
import com.xylope.betriot.layer.service.bet.model.WinOrLose;
import com.xylope.betriot.layer.service.bet.view.printer.Printer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConsoleBetView implements BetView{
    private final JdaAPI jdaAPI;
    private final Printer printer;

    @Override
    public void sendReserveBetView(UserVO user) {
        String publisherName = jdaAPI.getUserById(user.getDiscordId()).getName();
        printer.print("배팅 개설 예약이 진행되었습니다!\n개설자 : " + publisherName);
    }

    @Override
    public void sendStartBetView(BetDto bet) {
        String publisherName = getPublisherNameByBetDto(bet);
        printer.print("배팅이 시작되었습니다!\n개설자 : " + publisherName);
    }

    @Override
    public void sendOpenBetParticipationView(BetDto bet) {
        String publisherName = getPublisherNameByBetDto(bet);
        printer.print("이제부터 " + publisherName + "님의 배팅 참여가 가능합니다!");
    }

    @Override
    public void sendCloseBetParticipationView(BetDto bet) {
        String publisherName = getPublisherNameByBetDto(bet);
        printer.print("이제부터 " + publisherName + "님의 배팅 참여가 불가능합니다!");
    }

    @Override
    public void sendGiveRewardToWinnersView(BetDto bet, WinOrLose isPublisherWinOrLose) {
        String publisherName = getPublisherNameByBetDto(bet);
        String WORDisplay = isPublisherWinOrLose.getDisplayStatus();
        printer.print(publisherName + "님이 " + WORDisplay + "하셧습니다!\n" + WORDisplay + "에 배팅하신 참여자분들께 보상을 지급합니다!");
    }

    @Override
    public void sendEndBetView(int betId, BetDto bet) {
        String publisherName = getPublisherNameByBetDto(bet);
        printer.print(publisherName + "님의 배팅이 종료되었습니다");
    }

    @Override
    public void sendMatchNotFoundView(BetDto bet) {
        String publisherName = getPublisherNameByBetDto(bet);
        printer.print(publisherName + "님의 매치가 발견되지 않아, 배팅이 취소되었습니다!");
    }

    @Override
    public void sendMatchExceedTimeLimitView(BetDto bet) {
        String publisherName = getPublisherNameByBetDto(bet);
        printer.print("이미" + publisherName + "님의 매치가 시작된지 일정 시간이 지나, 배팅이 취소되었습니다!");
    }

    //TODO 구현하기
    @Override
    public void sendUserParticipationBettingView(BetDto bet, BetUserVO user) {

    }

    @Override
    public void sendNotEnoughMoneyToParticipationView(BetDto bet, UserVO user) {

    }

    @Override
    public void sendParticipationAlreadyCloseView(BetDto bet) {

    }

    @Override
    public void sendDuplicateParticipationView(BetDto bet, UserVO user) {

    }

    private String getPublisherNameByBetDto(BetDto bet) {
        return jdaAPI.getUserById(bet.getPublisher().getDiscordId()).getName();
    }
}

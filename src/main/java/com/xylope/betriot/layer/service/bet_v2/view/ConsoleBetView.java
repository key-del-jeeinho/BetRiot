package com.xylope.betriot.layer.service.bet_v2.view;

import com.xylope.betriot.layer.dataaccess.apis.discord.JdaAPI;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet_v2.model.BetDto;
import com.xylope.betriot.layer.service.bet_v2.model.WinOrLose;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@AllArgsConstructor
public class ConsoleBetView implements BetView{
    private final JdaAPI jdaAPI;
    @Override
    public void sendReserveBetView(UserVO user) {
        String publisherName = jdaAPI.getUserById(user.getDiscordId()).getName();
        print("배팅 개설 예약이 진행되었습니다!\n개설자 : " + publisherName);
    }

    @Override
    public void sendStartBetView(BetDto bet) {
        String publisherName = getPublisherNameByBetDto(bet);
        print("배팅이 시작되었습니다!\n개설자 : " + publisherName);
    }

    @Override
    public void sendOpenBetParticipationView(BetDto bet) {
        String publisherName = getPublisherNameByBetDto(bet);
        print("이제부터 " + publisherName + "님의 배팅 참여가 가능합니다!");
    }

    @Override
    public void sendCloseBetParticipationView(BetDto bet) {
        String publisherName = getPublisherNameByBetDto(bet);
        print("이제부터 " + publisherName + "님의 배팅 참여가 불가능합니다!");
    }

    @Override
    public void sendGiveRewardToWinnersView(BetDto bet, WinOrLose isPublisherWinOrLose) {
        String publisherName = getPublisherNameByBetDto(bet);
        String WORDisplay = isPublisherWinOrLose.getDisplayStatus();
        print(publisherName + "님이 " + WORDisplay + "하셧습니다!\n" + WORDisplay + "에 배팅하신 참여자분들께 보상을 지급합니다!");
    }

    @Override
    public void sendEndBetView(int betId, BetDto bet) {
        String publisherName = getPublisherNameByBetDto(bet);
        print(publisherName + "님의 배팅이 종료되었습니다");
    }

    @Override
    public void sendMatchNotFoundView(BetDto bet) {
        String publisherName = getPublisherNameByBetDto(bet);
        print(publisherName + "님의 매치가 발견되지 않아, 배팅이 취소되었습니다!");
    }

    private String getPublisherNameByBetDto(BetDto bet) {
        return jdaAPI.getUserById(bet.getPublisher().getDiscordId()).getName();
    }

    protected void print(String s) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat ("yyyy.MM.dd hh시 mm분 ss초");
        System.out.println("[BETRIOT] " + format.format(timestamp) + " | " + s);
    }
}

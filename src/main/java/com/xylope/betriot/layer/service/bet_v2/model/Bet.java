package com.xylope.betriot.layer.service.bet_v2.model;

import com.xylope.betriot.layer.domain.vo.UserVO;
import lombok.Getter;

import java.util.*;

public class Bet {
    public static int betNum = 0; //배팅이 삭제될경우 내려간다
    public static int betAsc = 0; //게속해서 올라간다
    @Getter
    private int id;
    @Getter
    private long matchId = -1L;
    private BetProgress progress;
    @Getter
    private final UserVO publisher;
    @Getter
    private final Map<BetUserVO, WinOrLose> participants;

    public Bet(UserVO publisher) {
        this.progress = BetProgress.UN_ACTIVE;
        this.publisher = publisher;
        this.participants = new HashMap<>();
        this.id = betAsc++;
        betNum++;
    }

    public void setMatchId(long matchId) {
        if(this.matchId != -1)
            this.matchId = matchId;
    }

    public void nextStep() {
        this.progress = progress.nextStep();
    }

    public boolean checkProgress(BetProgress progress) {
        return this.progress == progress;
    }

    //만약, 해당 배팅의 프로그래스보다 이전의값이 매개변수로 전달될시 음수, 같을시 0, 이후의 값이 전달될시 양수를 반환한다
    public int compareProgress(BetProgress progress) {
        return progress.compareTo(this.progress);
    }

    public void addParticipant(BetUserVO user, WinOrLose winOrLose) {
        participants.put(user, winOrLose);
    }

    public Set<BetUserVO> getParticipants(WinOrLose sortMethod) {
        Set<BetUserVO> participantsWhoBetIn = new HashSet<>();
        participants.forEach((user, betIn) -> {
            if(betIn.equals(sortMethod))
                participantsWhoBetIn.add(user);
        });

        return participantsWhoBetIn;
    }

    public void close() {
        participants.clear();
        this.id = -1;
        betNum--;
    }

    public BetDto convertToDto() {
        return new BetDto(id, matchId, progress, publisher, participants);
    }
}


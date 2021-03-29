package com.xylope.betriot.layer.service.bet_v2.model;

import com.xylope.betriot.layer.domain.vo.UserVO;
import lombok.Getter;

import java.util.*;

public class Bet {
    public static int betNum = 0;
    @Getter
    private int id;
    private BetProgress progress;
    @Getter
    private final UserVO publisher;
    @Getter
    private final Map<UserVO, WinAndLose> participants;

    public Bet(UserVO publisher) {
        this.progress = BetProgress.UN_ACTIVE;
        this.publisher = publisher;
        this.participants = new HashMap<>();
        this.id = betNum++;
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

    public void addParticipant(UserVO user, WinAndLose winAndLose) {
        participants.put(user, winAndLose);
    }

    public Set<UserVO> getParticipants(WinAndLose sortMethod) {
        Set<UserVO> participantsWhoBetIn = new HashSet<>();
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
}


package com.xylope.betriot.layer.service.bet;

import com.merakianalytics.orianna.types.core.spectator.CurrentMatch;
import com.xylope.betriot.exception.NotEnoughMoneyException;
import com.xylope.betriot.layer.domain.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

@ToString
public class Bet {
    public static final int DEFAULT_SECOND_COUNT = 240; //4분
    @Getter
    private final UserVO publisher;
    @Getter
    private final Map<UserVO, Integer> participantsWin;
    @Getter
    private final Map<UserVO, Integer> participantsLose;
    @Getter
    private final PrivateChannel privateChannel;
    @Getter
    private final TextChannel textChannel;
    private final BetService.Action action; //BetSummoner 의 매치 확인 쿨다운이 0일때 실행되는 콜백
    private int secondCount; //0이될 경우 해당 배팅이 유효(매치성사)한지 검사한다
    @Getter @Setter
    private CurrentMatch currentMatch;

    @Getter @Setter
    private BetType betType;
    @Getter @Setter
    private Progress progress;

    public Bet(UserVO publisher, TextChannel textChannel, PrivateChannel privateChannel, BetService.Action action, BetType betType) {
        this.publisher = publisher;
        this.textChannel = textChannel;
        this.privateChannel = privateChannel;
        this.action = action;
        this.betType = betType;

        this.participantsWin = new HashMap<>();
        this.participantsLose = new HashMap<>();
        this.progress = Progress.UN_ACTIVE;

        secondCount = DEFAULT_SECOND_COUNT;
    }

    public void countdown() {
        countdown(1);
    }
    public void countdown(int count) {
        if(secondCount >= 0) { //-1에서 멈춤
            if (secondCount == 0){ //0일떄 실행하고 이후 감소구문에 의해 -1이됨
                action.doSomething(this);
            }
            secondCount -= count;
        }

    }

    public void addParticipant(UserVO user, int money, boolean isBetWin) {
        if(isBetWin)
            participantsWin.put(user, money);
        else
            participantsLose.put(user, money);
    }

    public HashMap<UserVO, Integer> getAllParticipants() {
        HashMap<UserVO, Integer> allParticipants = new HashMap<>(participantsWin);
        //Merging
        participantsLose.forEach((k, v) -> allParticipants.merge(k, v, Integer::sum)); //만약, 승리 했을경우와 패배했을경우 모두에 돈을 걸었을시, 그 두 금액을 합산한만큼을 맵에 담는다.

        return allParticipants;
    }

    public void cancelBet() {
        getAllParticipants().forEach((user, money) -> {
            int totalMoney = user.getMoney() + money;
            user.setMoney(totalMoney);
        });
    }

    @AllArgsConstructor
    public enum BetType {
        PRIVATE(true, "서버 내 배팅"), GLOBAL(false, "글로벌 배팅");

        @Getter
        private final boolean isPrivate;
        @Getter
        private final String displayType;
    }

    public enum Progress {
        UN_ACTIVE, //초기 Bet 생성이후
        OPEN_BET, //매치가 집혔는지 확인이되어 배팅이 개설된 이후
        CLOSE_BET, //배팅에대한 참여가 종료된 이후
        MATCH_END, //매치가 종료된 이후
        BET_END //배팅이 종료된 이후
    }
}

package com.xylope.betriot.layer.service.bet;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.spectator.CurrentMatch;
import com.xylope.betriot.exception.BetNotFountException;
import com.xylope.betriot.exception.IllegalProcessException;
import com.xylope.betriot.exception.NotEnoughMoneyException;
import com.xylope.betriot.layer.dataaccess.apis.discord.JdaAPI;
import com.xylope.betriot.layer.dataaccess.apis.riot.MatchAPI;
import com.xylope.betriot.layer.domain.event.OnSecondEvent;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.message.ChannelEmbedMessageSender;
import com.xylope.betriot.layer.service.message.ChannelErrorMessageSender;
import com.xylope.betriot.layer.service.message.ChannelMessageSenderImpl;
import com.xylope.betriot.layer.service.message.PrivateEmbedMessageSenderWithMention;
import com.xylope.betriot.layer.service.user.dao.BankUserDao;
import com.xylope.betriot.manager.TimeCounter;
import com.xylope.betriot.manager.TimeListenerAdapter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class BetService {
    public static final int DIVIDEND_RATE = 2;

    private final List<Bet> betQueue;
    private final Action action;

    private final PrivateEmbedMessageSenderWithMention privateEmbedMessageSenderWithMention;
    private final ChannelEmbedMessageSender channelEmbedMessageSender;
    private final ChannelMessageSenderImpl channelMessageSender;
    private final ChannelErrorMessageSender channelErrorMessageSender;

    private final MatchAPI matchAPI;
    private final JdaAPI jdaAPI;

    private final BankUserDao userDao;

    public BetService(TimeCounter counter, MatchAPI matchAPI, JdaAPI jdaAPI,
                      PrivateEmbedMessageSenderWithMention privateEmbedMessageSenderWithMention,
                      ChannelEmbedMessageSender channelEmbedMessageSender,
                      ChannelMessageSenderImpl channelMessageSender,
                      ChannelErrorMessageSender channelErrorMessageSender,
                      BankUserDao userDao) {
        //기본적인 데이터를 초기화한다.
        this.privateEmbedMessageSenderWithMention = privateEmbedMessageSenderWithMention;
        this.channelEmbedMessageSender = channelEmbedMessageSender;
        this.channelMessageSender = channelMessageSender;
        this.channelErrorMessageSender = channelErrorMessageSender;

        this.matchAPI = matchAPI;
        this.jdaAPI = jdaAPI;
        this.userDao = userDao;

        //Counter 이 활성화되지 않았을경우, 이를 활성화한다.
        if(!counter.isRunning())
            counter.setRunning(true);

        //매칭 여부를 일정시간 이후에 확인하므로 해당시간을 확인하기위해 TimeCounter 를 사용한다
        counter.addTimeListener(new TimeListenerAdapter() {
            @Override
            public void onTimeSecond(OnSecondEvent e) {
                countdown();
            }
        });

        //배팅 목록을 담은 리스트, 타이머떄문에 멀티쓰레딩 환경에서 실행되니, CopyOnWriteArrayList 를 사용한다.
        betQueue = new CopyOnWriteArrayList<>();
        //배팅 개설 명령어를 친 뒤 일정시간이 지나, 매칭여부를 검사하는 로직
        action = bet -> {
            UserVO user = bet.getPublisher();

            if(matchAPI.isCurrentMatchExist(user.getRiotId())) {
                startBet(bet);
            } else {
                MessageEmbed message = new EmbedBuilder()
                        .setTitle("매칭 개설에 실패했습니다 :(")
                        .addField("", Bet.DEFAULT_SECOND_COUNT + "초 내로 매칭이 감지되지 않아 배팅 개설에 실패했습니다", false)
                        .build();
                privateEmbedMessageSenderWithMention.setUser(jdaAPI.getUserById(user.getDiscordId()));
                privateEmbedMessageSenderWithMention.sendMessage(
                        jdaAPI.getPrivateChannelByUserId(user.getDiscordId()),
                        message);
            }
            betQueue.remove(bet); //작업이 끝나고 큐에서 해당 배팅을 지운다.
        };
    }

    //Publisher's work

    //해당 명령어를 친 디스코드 방에서만 실행하는 배팅을 개설한다
    public void openPrivateBet(UserVO user, TextChannel openChannel, PrivateChannel privateChannel) {
        openBet(user, openChannel, privateChannel, Bet.BetType.PRIVATE);
    }

    //벳라이엇이 적용된 모든 방에서 실행하는 베팅을 개설한다
    public void openGlobalBet(UserVO user, TextChannel openChannel, PrivateChannel privateChannel) {
        openBet(user, openChannel, privateChannel, Bet.BetType.GLOBAL);
    }

    //배팅을 개설하는 로직이 담긴 private 메서드
    private void openBet(UserVO user, TextChannel openChannel, PrivateChannel privateChannel, Bet.BetType betType) {
        betQueue.add(new Bet(user, openChannel, privateChannel, action, betType));
        MessageEmbed message = new EmbedBuilder()
                .setTitle("배팅 개설 예약에 성공하였습니다!")
                .addField("", Bet.DEFAULT_SECOND_COUNT + "초 내로 인게임 매칭이 잡힐 경우, 자동으로 배팅이 이 채널에 개설됩니다!", false)
                .addField("배팅 종류", betType.getDisplayType(), false)
                .addField("", "", false)
                .setFooter(String.format("중계소 : %s - %s", openChannel.getGuild().getName(), openChannel.getName()))
                .build();

        this.channelEmbedMessageSender.sendMessage(openChannel, message);
    }

    //배팅 객체를 가지고 배팅을 시작한다
    private void startBet(Bet bet) {
        String summonerId = bet.getPublisher().getRiotId();
        CurrentMatch match = matchAPI.getCurrentMatch(summonerId);
        bet.setCurrentMatch(match);

        User user = jdaAPI.getUserById(bet.getPublisher().getDiscordId());;
        int betId = betQueue.indexOf(bet);
        if(betId == -1)
            throw new BetNotFountException("unknown bet : " + bet);

        MessageEmbed message = new EmbedBuilder()
                .setTitle("배팅이 개설되었습니다!")
                .addField("개설자", user.getName(), false)
                .addField("배팅 ID", ""+betId, false)
                .addField("", "해당 배팅에 참여하시려면 `뱃라이엇 배팅 참가 " + betId + " <승리 | 패배> <돈>` 명령어를 입력해주세요!", false)
                .build();

        bet.setProgress(Bet.Progress.OPEN_BET);

        channelEmbedMessageSender.sendMessage(bet.getTextChannel(), message);
    }

    //betQueue 에 존재하는 모든 배팅을 카운트다운한다
    private void countdown() {
        betQueue.forEach(Bet::countdown);
    }

    public void endBet(Bet bet) {
        //TODO exists 로매칭이 끝나는지 여부를 체크할 수 있는지 테스트한다
        Match match =Orianna.matchWithId(bet.getCurrentMatch().getId()).get();
        if(!match.exists()) {
            throw new IllegalProcessException("Match isn't end");
        }

        if(match.getTimeline().getInterval().getStandardMinutes() < 10) {//만약, 게임이 10분 내로 끝났을경우,
            //이를 비정상적인 게임이라 생각하고, 배팅을 취소한다
            bet.cancelBet();
        }

        boolean isPublisherWin = matchAPI.isSummonerWin(match, bet.getPublisher().getRiotId());

        Map<UserVO, Integer> winners;
        if(isPublisherWin) {
            winners = bet.getParticipantsWin();
        } else {
            winners = bet.getParticipantsLose();
        }
        //이긴사람에게 배당률만큼 돈을 지급한다.
        winners.forEach((user, money) -> {
            userDao.addMoney(user, money * DIVIDEND_RATE);
        });
    }


    //Participant's work

    public void addParticipant(int betId, UserVO user, int money, boolean isBetWin) {
        if(money < 0) {
            isBetWin = !isBetWin;
            channelMessageSender.sendMessage(getBet(betId).getTextChannel(), "돈의 값이 음수입니다! 자동으로 반대쪽 배팅에 배팅됩니다!");
        }
        if(user.getMoney() < money) //만약 베팅에 건 돈이 소지금보다 적을경우
            throw new NotEnoughMoneyException(user.getMoney(), money);
        userDao.addMoney(user, -money);
        getBet(betId).addParticipant(user, money, isBetWin);
    }

    public Bet getBet(int id) {
        try {
            return betQueue.get(id);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new UnknownBetIdException(e, id);
        }
    }

    @FunctionalInterface
    public interface Action {
        void doSomething(Bet user);
    }
}

package com.xylope.betriot.layer.service.bet_v2.reader;

import com.xylope.betriot.exception.bet.WrongArgumentException;
import com.xylope.betriot.exception.bet.WrongDisplayStatusException;
import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet_v2.BetService;
import com.xylope.betriot.layer.service.bet_v2.model.WinOrLose;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandBetReader implements BetReader<String[]>{
    private final UserDao dao;

    @Override
    public void read(BetService service, Action action, String... input) {
        switch (action) {
            case BET_OPEN:
                openBet(service, input);
                break;
            case BETTING_PARTICIPATION:
               participationBet(service, input);
                break;
        }
    }

    private void openBet(BetService service, String[] input) {
        try {
            service.createNewBet(Long.parseLong(input[0]));
        } catch(NumberFormatException e) {
            throw new WrongArgumentException(e, 0);
        }
    }

    private void participationBet(BetService service, String[] input) {
        //<SENDER_ID> 0 패배 1000
        UserVO user;
        int betId;
        int money;
        WinOrLose betWhere;

        //SenderArgument
        try {
            user = dao.get(Long.parseLong(input[0]));
        } catch (NumberFormatException e) {
            throw new WrongArgumentException(e, 0);
        }

        //FirstArgument
        try {
            betId = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            throw new WrongArgumentException(e, 1);
        }

        //SecondArgument
        try {
            betWhere = WinOrLose.getByDisplayStatus(input[2]);
        } catch (WrongDisplayStatusException e) {
            throw new WrongArgumentException(e, 2);
        }

        //ThirdArgument
        try {
            money = Integer.parseInt(input[3]);
        } catch (NumberFormatException e) {
            throw new WrongArgumentException(e, 3);
        }

        service.addParticipant(betId, user, money, betWhere);
    }
}

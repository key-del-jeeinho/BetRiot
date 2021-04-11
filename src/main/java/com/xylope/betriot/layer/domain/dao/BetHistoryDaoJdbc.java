package com.xylope.betriot.layer.domain.dao;

import com.xylope.betriot.layer.domain.vo.BetHistoryVO;
import com.xylope.betriot.layer.domain.vo.BetParticipantVO;
import com.xylope.betriot.layer.service.bet.model.WinOrLose;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

public class BetHistoryDaoJdbc implements BetHistoryDao{
    @Setter
    PlatformTransactionManager transactionManager;
    @Setter
    private JdbcOperations jdbc;
    RowMapper<BetHistoryVO> betHistoryMapper = (rs, rowNum) -> {
        long betId = rs.getLong("bet_id");
        return new BetHistoryVO(
                betId,
                rs.getLong("user_id"),
                new DateTime(rs.getLong("match_duration")),
                new DateTime(rs.getLong("bet_end_time")),
                WinOrLose.getById(rs.getInt("bet_result")),
                getParticipantsByBetId(betId));
    };

    RowMapper<BetParticipantVO> betParticipantMapper = (rs, rowNum) ->
            new BetParticipantVO(
                    rs.getLong("user_id"),
                    rs.getInt("bet_money"),
                    WinOrLose.getById(rs.getInt("bet_where"))
            );

    @Override
    public long add(BetHistoryVO betHistoryVO) {

        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            jdbc.update("insert into bet_history (bet_id, user_id, match_duration, bet_end_time, bet_result) values (?, ?, ?, ?, ?)",
                    betHistoryVO.getBetId(), betHistoryVO.getUserId(), betHistoryVO.getMatchDuration().getMillis(), betHistoryVO.getBetEndTime().getMillis(), betHistoryVO.getBetResult().getId());


            for (BetParticipantVO participant : betHistoryVO.getParticipants())
                jdbc.update("insert into bet_participation_history (bet_id, user_id, bet_where, bet_money) values (?, ?, ?, ?)",
                        betHistoryVO.getBetId(), participant.getParticipantId(), participant.getBetWhere().getId(), participant.getBetMoney());

            transactionManager.commit(status);
            return betHistoryVO.getBetId();
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public BetHistoryVO get(long betId) {
        return jdbc.queryForObject("select * from bet_history where bet_id = ?", new Object[]{betId} , betHistoryMapper);
    }

    @Override
    public List<BetHistoryVO> getByUserId(long userId) {
        return jdbc.query("select * from bet_history where user_id = ?", new Object[]{userId}, betHistoryMapper);
    }

    @Override
    public List<BetHistoryVO> getAll() {
        return jdbc.query("select * from bet_history", betHistoryMapper);
    }

    @Override
    public List<BetParticipantVO> getParticipantsByBetId(long betId) {
        return jdbc.query("select * from bet_participation_history where bet_id = ? order by bet_where desc, bet_money desc", new Object[]{betId}, betParticipantMapper);
    }

    @Override
    public void update(BetHistoryVO betHistoryVO) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            long betId = betHistoryVO.getBetId();

            jdbc.update("update bet_history set user_id = ?, match_duration = ?, bet_end_time = ?, bet_result = ? where bet_id = ?",
                    betHistoryVO.getUserId(), betHistoryVO.getMatchDuration().getMillis(), betHistoryVO.getBetEndTime().getMillis(), betHistoryVO.getBetResult(), betId);

            jdbc.update("delete from bet_participation_history where bet_id = ?", betId);

            for (BetParticipantVO participant : betHistoryVO.getParticipants())
                jdbc.update("insert into bet_participation_history (bet_id, user_id, bet_where, bet_money) values (?, ?, ?, ?)",
                        betId, participant.getParticipantId(), participant.getBetWhere(), participant.getBetMoney());

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public void remove(long betId) {
        jdbc.update("delete from bet_history where bet_id  = ?", betId);
    }

    @Override
    public void removeAll() {
        jdbc.update("delete from bet_history");
    }

    @Override
    public int getCount() {
        Integer count;
        if((count = jdbc.queryForObject("select count(*) from bet_history", Integer.class)) != null)
            return count;
        return 0;
    }
}

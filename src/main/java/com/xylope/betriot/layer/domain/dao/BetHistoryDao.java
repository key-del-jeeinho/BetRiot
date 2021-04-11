package com.xylope.betriot.layer.domain.dao;

import com.xylope.betriot.layer.domain.vo.BetHistoryVO;
import com.xylope.betriot.layer.domain.vo.BetParticipantVO;

import java.util.List;

public interface BetHistoryDao {
    long add(BetHistoryVO betHistoryVO);

    BetHistoryVO get(long betId);
    List<BetHistoryVO> getByUserId(long userId);
    List<BetHistoryVO> getAll();
    List<BetParticipantVO> getParticipantsByBetId(long betId);

    void update(BetHistoryVO betHistoryVO);

    void remove(long betId);
    void removeAll();

    int getCount();
}

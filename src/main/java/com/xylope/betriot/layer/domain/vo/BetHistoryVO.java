package com.xylope.betriot.layer.domain.vo;

import com.xylope.betriot.layer.service.bet.model.WinOrLose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.List;

@AllArgsConstructor
public class BetHistoryVO {
    @Getter @Setter
    long betId;
    @Getter @Setter
    long userId;
    @Getter @Setter
    DateTime matchDuration;
    @Getter @Setter
    DateTime betEndTime;
    @Getter @Setter
    WinOrLose betResult;
    @Getter @Setter
    List<BetParticipantVO> participants;
}
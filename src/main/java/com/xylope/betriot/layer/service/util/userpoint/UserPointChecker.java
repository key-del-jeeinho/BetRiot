package com.xylope.betriot.layer.service.util.userpoint;

import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.domain.vo.UserVO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserPointChecker {
    private final UserDao dao;
    private final UserPointView view;

    public void checkUserPoint(long discordId) {
        UserVO user = dao.get(discordId);
        view.sendCheckUserPointView(user);
    }
}

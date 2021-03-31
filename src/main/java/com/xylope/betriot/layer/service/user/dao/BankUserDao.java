package com.xylope.betriot.layer.service.user.dao;

import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.domain.vo.UserVO;

public interface BankUserDao extends UserDao {
    void addMoney(UserVO user, int money);
    void setMoney(UserVO user, int money);
    void setSpareDao(UserDao userDao);
}

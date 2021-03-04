package com.xylope.betriot.layer.domain.dao;

import com.xylope.betriot.layer.domain.vo.UserVO;

import java.util.List;

public interface UserDao {
    UserVO get(long discordId);
    List<UserVO> getAll();
    List<UserVO> getAllOrderByMoney();

    UserVO getInServer(long discordId, long serverId);
    List<UserVO> getAllInServer(long serverId);
    List<UserVO> getAllOrderByMoneyInServer(long serverId);

    void add(UserVO user);
    void remove(long discordId);
    void removeAll();
    void update(UserVO user);

    int getCount();

    boolean isUserExist(long discordId);
}

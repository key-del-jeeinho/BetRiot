package com.xylope.betriot.layer.domain.dao;

import com.xylope.betriot.layer.domain.vo.User;

import java.util.List;

public interface UserDao {
    User get(long discordId);
    List<User> getAll();
    List<User> getAllOrderByMoney();

    User getInServer(long discordId, long serverId);
    List<User> getAllInServer(long serverId);
    List<User> getAllOrderByMoneyInServer(long serverId);

    void add(User user);
    void remove(long discordId);
    void removeAll();
    void update(User user);

    int getCount();

    boolean isUserExist(long discordId);
}

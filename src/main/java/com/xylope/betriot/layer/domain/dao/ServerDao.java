package com.xylope.betriot.layer.domain.dao;

import com.xylope.betriot.layer.domain.vo.ServerVO;

import java.util.List;

public interface ServerDao {
    ServerVO get(long serverId);
    List<ServerVO> getAll();

    //List<ServerVO> getAllOrderByPremiumLevel(); TODO 구현예정

    void add(ServerVO server);
    void add(ServerVO... servers);
    void remove(long serverId);
    void removeAll();
    void update(ServerVO server);

    int getCount();

    boolean isUserExist(long serverId);
}

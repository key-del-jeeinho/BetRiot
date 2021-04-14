package com.xylope.betriot.layer.service.user_v2.dao;

import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.domain.vo.UserVO;
import lombok.Setter;

import java.util.List;

//돈 조작이 가능한 유저 Dao
public class BankUserDaoImpl implements BankUserDao {
    @Setter
    private UserDao spareDao;

    @Override
    public UserVO get(long discordId) {
        return spareDao.get(discordId);
    }

    @Override
    public List<UserVO> getAll() {
        return spareDao.getAll();
    }

    @Override
    public List<UserVO> getAllOrderByMoney() {
        return spareDao.getAllOrderByMoney();
    }

    @Override
    public UserVO getInServer(long discordId, long serverId) {
        return spareDao.getInServer(discordId, serverId);
    }

    @Override
    public List<UserVO> getAllInServer(long serverId) {
        return spareDao.getAllInServer(serverId);
    }

    @Override
    public List<UserVO> getAllOrderByMoneyInServer(long serverId) {
        return spareDao.getAllOrderByMoneyInServer(serverId);
    }

    @Override
    public void add(UserVO user) {
        spareDao.add(user);
    }

    @Override
    public void add(UserVO... users) {
        spareDao.add(users);
    }

    @Override
    public void remove(long discordId) {
        spareDao.remove(discordId);
    }

    @Override
    public void removeAll() {
        spareDao.removeAll();
    }

    @Override
    public void update(UserVO user) {
        spareDao.update(user);
    }

    @Override
    public int getCount() {
        return spareDao.getCount();
    }

    @Override
    public boolean isUserExist(long discordId) {
        return spareDao.isUserExist(discordId);
    }

    @Override
    public List<UserVO> getByPermission(UserVO.Permission permission) {
        return spareDao.getByPermission(permission);
    }

    @Override
    public List<UserVO> getAllOrderByPermission() {
        return spareDao.getAllOrderByPermission();
    }

    @Override
    public boolean isPermission(long discordId, UserVO.Permission permission) {
        return spareDao.isPermission(discordId, permission);
    }

    @Override
    public boolean checkPermission(long discordId, UserVO.Permission minPermission) {
        return spareDao.checkPermission(discordId, minPermission);
    }

    @Override
    public void addMoney(UserVO user, int money) {
        setMoney(user, user.getMoney() + money);
    }

    @Override
    public void setMoney(UserVO user, int money) {
        if(money < 0)
            throw new ArithmeticException("money can't be a negative");
        user.setMoney(money);
        update(user);
    }
}

package com.xylope.betriot.layer.domain.dao;

import com.xylope.betriot.layer.domain.vo.UserVO;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Objects;

public class UserDaoJdbc implements UserDao {
    @Setter
    PlatformTransactionManager transactionManager;
    @Setter
    JdbcOperations jdbc;
    private final RowMapper<UserVO> userMapper = (rs, rowNum) -> new UserVO(
            rs.getLong("discord_id"),
            rs.getString("riot_id"),
            rs.getInt("money"),
            UserVO.Permission.getById(rs.getInt("permission"))
    );

    @Override
    public UserVO get(long discordId) {
        return jdbc.queryForObject("select * from users where discord_id = ?", new Object[]{discordId},
                userMapper);
    }

    @Override
    public List<UserVO> getAll() {
        return jdbc.query("select * from users order by discord_id", userMapper);
    }

    @Override
    public List<UserVO> getAllOrderByMoney() {
        return jdbc.query("select * from users order by money desc", userMapper);
    }

    @Override //TODO Xylope | 미구현 | 2021.03.12
    public UserVO getInServer(long discordId, long serverId) {
        return null;
    }

    @Override //TODO Xylope | 미구현 | 2021.03.12
    public List<UserVO> getAllInServer(long serverId) {
        return null;
    }

    @Override //TODO Xylope | 미구현 | 2021.03.12
    public List<UserVO> getAllOrderByMoneyInServer(long serverId) {
        return null;
    }

    @Override
    public void add(UserVO user) {
        jdbc.update("insert into users (discord_id, riot_id, money, permission) values (?, ?, ?, ?)", user.getDiscordId(), user.getRiotId(), user.getMoney(), user.getPermission().getId());
    }

    @Override
    public void add(UserVO... users) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            for(UserVO user : users) {
                add(user);
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
        }
    }

    @Override
    public void remove(long discordId) {
        jdbc.update("delete from users where discord_id = ?", discordId);
    }

    @Override
    public void removeAll() {
        jdbc.update("delete from users");
    }

    @Override
    public void update(UserVO user) {
        jdbc.update("update users set discord_id = ?, riot_id = ?, money = ? where discord_id = ?", user.getDiscordId(), user.getRiotId(), user.getMoney(), user.getDiscordId());
    }

    @Override
    public int getCount() {
        Integer count;
        if((count = jdbc.queryForObject("select count(*) from users", Integer.class)) != null)
            return count;
        return 0; //queryForObject return null
    }

    @Override
    public boolean isUserExist(long discordId) {
        return  (Objects.requireNonNull(jdbc.queryForObject("select EXISTS(select * from users where discord_id = ?);", new Object[]{discordId},
                (rs, rowNum) -> rs.getInt(1)))
                == 1);
    }


    @Override
    public List<UserVO> getByPermission(UserVO.Permission permission) {
        return jdbc.query("select * from users where permission = ?", new Object[] {permission.getId()}, userMapper);
    }

    @Override
    public List<UserVO> getAllOrderByPermission() {
        return jdbc.query("select * from users order by permission desc", userMapper);
    }

    @Override
    public boolean isPermission(long discordId, UserVO.Permission permission) {
        UserVO user = get(discordId);
        return user.getPermission() == permission;
    }

    @Override
    public boolean checkPermission(long discordId, UserVO.Permission minPermission) {
        return get(discordId).getPermission().getId() >= minPermission.getId();
    }
}

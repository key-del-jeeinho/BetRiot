package com.xylope.betriot.layer.domain.dao;

import com.xylope.betriot.layer.domain.vo.User;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class UserDaoJdbc implements UserDao {
    @Setter
    JdbcOperations jdbc;
    private RowMapper<User> userMapper = (rs, rowNum) -> new User(
            rs.getLong("discord_id"),
            rs.getString("riot_id"),
            rs.getInt("money")
    );

    @Override
    public User get(long discordId) {
        return jdbc.queryForObject("select * from users where discord_id = ?", new Object[]{discordId},
                userMapper);
    }

    @Override
    public List<User> getAll() {
        return jdbc.query("select * from users order by discord_id", userMapper);
    }

    @Override
    public List<User> getAllOrderByMoney() {
        return jdbc.query("select * from users order by money desc", userMapper);
    }

    @Override
    public User getInServer(long discordId, long serverId) {
        return null;
    }

    @Override
    public List<User> getAllInServer(long serverId) {
        return null;
    }

    @Override
    public List<User> getAllOrderByMoneyInServer(long serverId) {
        return null;
    }

    @Override
    public void add(User user) {
        jdbc.update("insert into users (discord_id, riot_id, money) values (?, ?, ?)", user.getDiscordId(), user.getRiotId(), user.getMoney());
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
    public void update(User user) {
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
        return  (jdbc.queryForObject("select EXISTS(select * from users where discord_id = ?);", new Object[]{discordId},
                (rs, rowNum) -> rs.getInt(1))
                == 1);
    }
}

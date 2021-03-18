package com.xylope.betriot.layer.domain.dao;

import com.xylope.betriot.layer.domain.vo.ServerVO;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Objects;

public class ServerDaoJdbc implements ServerDao{
    @Setter
    PlatformTransactionManager transactionManager;
    @Setter
    JdbcOperations jdbc;
    private final RowMapper<ServerVO> serverMapper = (rs, rowNum) -> new ServerVO(
            rs.getInt("id"),
            rs.getLong("server_id"),
            rs.getLong("notice_channel_id"),
            rs.getInt("premium_level")
    );

    @Override
    public ServerVO get(long serverId) {
        return jdbc.queryForObject("select * from servers where server_id = ?", new Object[] {serverId}, ServerVO.class);
    }

    @Override
    public List<ServerVO> getAll() {
        return jdbc.query("select * from servers order by id", serverMapper);
    }

    @Override
    public void add(ServerVO server) {
        jdbc.update("insert into servers (server_id, notice_channel_id, premium_level) values (?, ?, ?);",
                server.getServerId(),
                server.getNoticeChannelId(),
                server.getPremiumLevel());
    }

    @Override
    public void add(ServerVO... servers) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            for(ServerVO server : servers) {
                add(server);
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
        }
    }

    @Override
    public void remove(long serverId) {
        jdbc.update("delete from servers where id = ?", serverId);
    }

    @Override
    public void removeAll() {
        jdbc.update("delete from servers");
    }

    @Override
    public void update(ServerVO server) {
        jdbc.update("update servers set server_id = ?, notice_channel_id = ?, premium_level = ? where id = ?",
                server.getId(),
                server.getNoticeChannelId(),
                server.getPremiumLevel());
    }

    @Override
    public int getCount() {
        Integer count;
        if((count = jdbc.queryForObject("select count(*) from servers", Integer.class)) != null)
            return count;
        return 0; //queryForObject return null
    }

    @Override
    public boolean isUserExist(long serverId) {
        return  (Objects.requireNonNull(jdbc.queryForObject("select EXISTS(select * from servers where server_id = ?);", new Object[]{serverId},
                (rs, rowNum) -> rs.getInt(1)))
                == 1);
    }
}

package com.liang.service.manager;

import com.liang.dal.entity.ConnectionDefinitionDO;
import com.liang.dal.mapper.ConnectionDefinitionMapper;
import com.liang.service.support.Constants;
import com.liang.service.support.dto.ConnectionDTO;
import com.liang.service.support.exceptions.BaseException;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @since 2023/9/10 20:00
 * @author by liangzj
 */
@Slf4j
@Component
public class ConnectionManager {
    private static final ConcurrentHashMap<String, Connection> connectionMap =
            new ConcurrentHashMap<>();

    @Autowired private ConnectionDefinitionMapper connectionMapper;

    public Connection obtain(String connectionId) {
        Connection connection = connectionMap.get(connectionId);

        try {
            if (Objects.isNull(connection) || connection.isClosed()) {
                connection = create(connectionId);
                connectionMap.put(connectionId, connection);
            }
        } catch (SQLException e) {
            try {
                connection = connectionMap.remove(connectionId);
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new BaseException(e.getMessage(), e);
        }

        return connection;
    }

    public void testConnect(ConnectionDTO dto) {
        try (Connection connection =
                DriverManager.getConnection(dto.getUrl(), dto.getUsername(), dto.getPassword())) {
            if (connection == null) {
                throw new BaseException("获取数据库连接失败");
            }
        } catch (SQLException e) {
            throw new BaseException(e.getMessage(), e);
        }
    }

    private Connection create(String connectionId) throws SQLException {
        ConnectionDefinitionDO connectionDO = connectionMapper.selectOne(connectionId);
        if (Objects.isNull(connectionDO)) throw new BaseException("连接信息不存在");

        Connection connection;
        connection =
                DriverManager.getConnection(
                        connectionDO.getUrl(),
                        connectionDO.getUsername(),
                        connectionDO.getPassword());

        return Optional.ofNullable(connection).orElseThrow(() -> new BaseException("数据库连接失败"));
    }

    @PostConstruct
    public void postConstruct() {
        try {
            // 容器启动时加载数据库驱动
            Class.forName(Constants.MYSQL_8_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw new BaseException(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void preDestroy() {
        if (connectionMap.isEmpty()) return;

        for (String connectionId : connectionMap.keySet()) {
            try {
                Connection connection = connectionMap.remove(connectionId);
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("connection close error, connectionId: {}", connectionId, e);
            }
        }
    }
}

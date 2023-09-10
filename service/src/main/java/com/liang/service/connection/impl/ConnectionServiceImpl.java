package com.liang.service.connection.impl;

import com.liang.dal.entity.ConnectionDO;
import com.liang.dal.mapper.ConnectionMapper;
import com.liang.service.connection.ConnectionService;
import com.liang.service.support.converter.ConnectionConverter;
import com.liang.service.support.dto.ConnectionDTO;
import com.liang.service.support.events.ConnectionsChangeEvent;
import com.liang.service.support.exceptions.BaseException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @since 2023/9/9 15:28
 * @author by liangzj
 */
@Slf4j
@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired private ConnectionMapper connectionMapper;
    @Autowired private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void save(ConnectionDTO dto) {
        // 检查是否能够连接成功
        testConnect(dto);

        // 保存
        ConnectionDO connectionDO = new ConnectionDO();
        BeanUtils.copyProperties(dto, connectionDO);
        connectionDO.setConnectionId(UUID.randomUUID().toString().replace("-", ""));
        connectionDO.setDatabaseName(getDatabaseName(dto.getUrl()));
        Integer insert = connectionMapper.insert(connectionDO);
        if (insert == null || insert == 0) log.warn("insert 0 lines");

        // 推送变更消息
        applicationEventPublisher.publishEvent(new ConnectionsChangeEvent(1L));
    }

    @Override
    public List<ConnectionDTO> all() {
        List<ConnectionDO> allList = connectionMapper.selectAll();
        if (CollectionUtils.isEmpty(allList)) {
            return Collections.emptyList();
        }

        return allList.stream().map(ConnectionConverter::convert).toList();
    }

    @Override
    public void testConnect(ConnectionDTO dto) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new BaseException(e.getMessage(), e);
        }

        try (Connection connection =
                DriverManager.getConnection(dto.getUrl(), dto.getUsername(), dto.getPassword())) {
            if (connection == null) {
                throw new BaseException("数据库连接失败");
            }
        } catch (SQLException e) {
            throw new BaseException(e.getMessage(), e);
        }
    }

    private String getDatabaseName(String url) {
        return url.replaceAll("jdbc:mysql://[^/]+/(\\w+).*", "$1");
    }
}

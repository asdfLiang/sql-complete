package com.liang.service.connection.impl;

import com.liang.service.connection.ConnectionService;
import com.liang.service.manager.ConnectionDefinitionManager;
import com.liang.service.manager.ConnectionManager;
import com.liang.service.support.dto.ConnectionDTO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @since 2023/9/9 15:28
 * @author by liangzj
 */
@Slf4j
@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired private ConnectionDefinitionManager connectionDefinitionManager;
    @Autowired private ConnectionManager connectionManager;

    @Override
    public void save(ConnectionDTO dto) {
        // 测试连接失败将抛出异常，中断保存操作
        testConnect(dto);

        connectionDefinitionManager.register(dto);
    }

    @Override
    public List<ConnectionDTO> all() {
        return connectionDefinitionManager.all();
    }

    @Override
    public void testConnect(ConnectionDTO dto) {
        connectionManager.testConnect(dto);
    }
}

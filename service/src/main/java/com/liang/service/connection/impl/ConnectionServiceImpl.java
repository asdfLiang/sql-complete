package com.liang.service.connection.impl;

import com.liang.service.connection.ConnectionService;
import com.liang.service.manager.ConnectionDefinitionManager;
import com.liang.service.manager.ConnectionManager;
import com.liang.service.support.dto.ConnectionDTO;
import com.liang.service.support.dto.TableDTO;

import com.liang.service.support.exceptions.BaseException;
import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<TableDTO> tables(String connectionId) {
        ConnectionDTO dto = connectionDefinitionManager.one(connectionId);

        List<Map<String, Object>> list =
                connectionManager.executeQuery(connectionId, "show tables");
        if (CollectionUtils.isEmpty(list)) return Collections.emptyList();

        List<TableDTO> tables = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String tableName = String.valueOf(map.get("Tables_in_" + dto.getSchemaName()));
            tables.add(new TableDTO(tableName, tableName));
        }

        return tables;
    }
}

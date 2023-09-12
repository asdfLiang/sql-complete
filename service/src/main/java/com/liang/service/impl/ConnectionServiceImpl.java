package com.liang.service.impl;

import com.liang.service.ConnectionService;
import com.liang.service.manager.ConnectionDefinitionManager;
import com.liang.service.manager.ConnectionManager;
import com.liang.service.support.dto.ColumnDTO;
import com.liang.service.support.dto.ConnectionDTO;
import com.liang.service.support.dto.TableDTO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Override
    public List<ColumnDTO> descTable(String connectionId, String tableName) {
        List<Map<String, Object>> list =
                connectionManager.executeQuery(connectionId, "DESC " + tableName);
        if (CollectionUtils.isEmpty(list)) return Collections.emptyList();

        List<ColumnDTO> columns = new ArrayList<>();
        for (Map<String, Object> map : list) {
            ColumnDTO columnDTO = new ColumnDTO();
            columnDTO.setField(String.valueOf(map.get("Field")));
            columnDTO.setType(String.valueOf(map.get("Type")));
            columnDTO.setNullable("YES".equals(map.get("Null")));
            columnDTO.setPrimaryKey("PRI".equals(map.get("Key")));
            columns.add(columnDTO);
        }

        return columns;
    }
}

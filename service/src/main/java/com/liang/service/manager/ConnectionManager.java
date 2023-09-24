package com.liang.service.manager;

import com.liang.dal.entity.ConnectionDefinitionDO;
import com.liang.dal.mapper.ConnectionDefinitionMapper;
import com.liang.service.support.constants.Constants;
import com.liang.service.support.dto.ConnectionDTO;
import com.liang.service.support.exceptions.BaseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

/**
 * @since 2023/9/10 20:00
 * @author by liangzj
 */
@Slf4j
@Component
public class ConnectionManager {
    /** Map<connectionId, DataSource> */
    private static final ConcurrentHashMap<String, DataSource> dataSourceMap =
            new ConcurrentHashMap<>();

    @Autowired private ConnectionDefinitionMapper connectionDefinitionMapper;

    public Connection obtain(String connectionId) throws SQLException {
        DataSource dataSource = dataSourceMap.get(connectionId);

        if (Objects.isNull(dataSource)) {
            dataSource = createDataSource(connectionId);
            dataSourceMap.put(connectionId, dataSource);
            log.info("create dataSource by connectionId: {}", connectionId);
        }

        return dataSource.getConnection();
    }

    public void testConnect(ConnectionDTO dto) {
        try (Connection connection =
                DriverManager.getConnection(dto.getUrl(), dto.getUsername(), dto.getPassword())) {
            if (connection == null) {
                throw new BaseException("获取数据库连接失败");
            }
        } catch (SQLException e) {
            log.error("test connect error | ", e);
            throw new BaseException(e.getMessage(), e);
        }
    }

    private DataSource createDataSource(String connectionId) {
        ConnectionDefinitionDO connectionDO = connectionDefinitionMapper.selectOne(connectionId);
        if (Objects.isNull(connectionDO)) {
            log.error("connection definition not found, connectionId: {}", connectionId);
            throw new BaseException("连接信息不存在，请重新创建数据库连接");
        }

        HikariConfig configuration = new HikariConfig();
        configuration.setSchema(connectionDO.getSchemaName());
        configuration.setJdbcUrl(connectionDO.getUrl());
        configuration.setUsername(connectionDO.getUsername());
        configuration.setPassword(connectionDO.getPassword());
        configuration.setDriverClassName(Constants.MYSQL_8_CLASS_NAME);
        configuration.setMaximumPoolSize(5);

        return new HikariDataSource(configuration);
    }

    public List<Map<String, Object>> executeQuery(
            String connectionId, String sql, Object... params) {
        try (Connection connection = obtain(connectionId);
                PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            // 执行查询sql
            ResultSet resultSet = statement.executeQuery();

            // 结果转换
            List<Map<String, Object>> result = new ArrayList<>();
            while (resultSet.next()) result.add(getRow(resultSet));

            return result;
        } catch (SQLException e) {
            log.error("execute query error, connectionId: {}, sql: {}", connectionId, sql, e);
            throw new BaseException(e.getMessage(), e);
        }
    }

    public Map<String, Object> getRow(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        Map<String, Object> row = new HashMap<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);
            Object columnValue = resultSet.getObject(i);
            row.put(columnName, columnValue);
        }

        return row;
    }
}

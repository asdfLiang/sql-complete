package com.liang.service.manager;

import com.liang.dal.entity.ConnectionDefinitionDO;
import com.liang.dal.mapper.ConnectionDefinitionMapper;
import com.liang.service.support.constants.Constants;
import com.liang.service.support.converter.ConnectionDTOConverter;
import com.liang.service.support.dto.ConnectionDTO;
import com.liang.service.support.events.ConnectionsChangeEvent;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @since 2023/9/10 21:19
 * @author by liangzj
 */
@Slf4j
@Component
public class ConnectionDefinitionManager {
    @Autowired private ConnectionDefinitionMapper connectionDefinitionMapper;
    @Autowired private ApplicationEventPublisher applicationEventPublisher;

    public void register(ConnectionDTO dto) {
        // 保存
        ConnectionDefinitionDO connectionDO = new ConnectionDefinitionDO();
        BeanUtils.copyProperties(dto, connectionDO);
        connectionDO.setConnectionId(UUID.randomUUID().toString().replace("-", ""));
        connectionDO.setSchemaName(getSchemaName(dto.getUrl()));
        Integer insert = connectionDefinitionMapper.insert(connectionDO);

        if (insert == null || insert == 0) log.warn("insert 0 lines connection definition.");

        // 推送变更消息
        applicationEventPublisher.publishEvent(new ConnectionsChangeEvent(1L));
    }

    public List<ConnectionDTO> all() {
        List<ConnectionDefinitionDO> allList = connectionDefinitionMapper.selectAll();
        if (CollectionUtils.isEmpty(allList)) {
            return Collections.emptyList();
        }

        return allList.stream()
                .filter(Objects::nonNull)
                .map(ConnectionDTOConverter::convert)
                .toList();
    }

    public ConnectionDTO one(String connectionId) {
        ConnectionDefinitionDO one = connectionDefinitionMapper.selectOne(connectionId);
        if (Objects.isNull(one)) return null;

        return ConnectionDTOConverter.convert(one);
    }

    public String getSchemaName(String url) {
        if (StringUtils.isBlank(url) || !url.matches(Constants.JDBC_REGEX)) {
            return null;
        }

        return url.replaceAll(Constants.JDBC_REGEX, "$1");
    }

    public static void main(String[] args) {
        ConnectionDefinitionManager manager = new ConnectionDefinitionManager();
        String url1 = "jdbc";
        String url2 = "1234";
        String url3 = "jdbc:mysql://localhost:3306/test";
        System.out.println(url1.matches(Constants.JDBC_REGEX));
        System.out.println(url2.matches(Constants.JDBC_REGEX));
        System.out.println(url3.matches(Constants.JDBC_REGEX));

        System.out.println(manager.getSchemaName(url1));
        System.out.println(manager.getSchemaName(url2));
        System.out.println(manager.getSchemaName(url3));
    }
}

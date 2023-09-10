package com.liang.service.manager;

import com.liang.dal.entity.ConnectionDefinitionDO;
import com.liang.dal.mapper.ConnectionDefinitionMapper;
import com.liang.service.support.Constants;
import com.liang.service.support.converter.ConnectionDTOConverter;
import com.liang.service.support.dto.ConnectionDTO;

import com.liang.service.support.events.ConnectionsChangeEvent;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
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
        connectionDO.setDatabaseName(getDatabaseName(dto.getUrl()));
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

        return allList.stream().map(ConnectionDTOConverter::convert).toList();
    }

    private String getDatabaseName(String url) {
        return url.replaceAll(Constants.JDBC_REGEX, "$1");
    }
}

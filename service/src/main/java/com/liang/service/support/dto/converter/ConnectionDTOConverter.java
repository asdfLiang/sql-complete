package com.liang.service.support.dto.converter;

import com.liang.dal.entity.ConnectionDefinitionDO;
import com.liang.service.support.dto.ConnectionDTO;

import org.springframework.beans.BeanUtils;

/**
 * @since 2023/9/9 21:24
 * @author by liangzj
 */
public class ConnectionDTOConverter {

    public static ConnectionDTO convert(ConnectionDefinitionDO connectionDefDO) {
        ConnectionDTO dto = new ConnectionDTO();
        BeanUtils.copyProperties(connectionDefDO, dto);
        return dto;
    }
}

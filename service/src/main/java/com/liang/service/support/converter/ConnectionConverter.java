package com.liang.service.support.converter;

import com.liang.dal.entity.ConnectionDO;
import com.liang.service.support.dto.ConnectionDTO;

import org.springframework.beans.BeanUtils;

/**
 * @since 2023/9/9 21:24
 * @author by liangzj
 */
public class ConnectionConverter {

    public static ConnectionDTO convert(ConnectionDO connectionDO) {
        ConnectionDTO dto = new ConnectionDTO();
        BeanUtils.copyProperties(connectionDO, dto);
        return dto;
    }
}

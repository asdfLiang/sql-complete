package com.liang.deploy.controller.converter;

import com.liang.deploy.controller.vo.ConnectionVO;
import com.liang.service.support.dto.ConnectionDTO;

import org.springframework.beans.BeanUtils;

/**
 * @since 2023/9/10 9:02
 * @author by liangzj
 */
public class ConnectionVOConverter {

    public static ConnectionVO convert(ConnectionDTO dto) {
        ConnectionVO connectionVO = new ConnectionVO();
        BeanUtils.copyProperties(dto, connectionVO);
        return connectionVO;
    }
}

package com.liang.deploy.controller.converter;

import com.liang.deploy.controller.vo.ConnectionItemVO;
import com.liang.service.support.dto.ConnectionDTO;
import com.liang.service.support.dto.TableDTO;

import java.util.Objects;

/**
 * @since 2023/9/10 9:02
 * @author by liangzj
 */
public class ConnectionVOConverter {

    public static ConnectionItemVO convert(ConnectionDTO dto) {
        ConnectionItemVO itemVO = new ConnectionItemVO();
        itemVO.setItemId(dto.getConnectionId());
        itemVO.setItemName(dto.getConnectionName());
        return itemVO;
    }

    public static ConnectionItemVO convert(TableDTO dto) {
        ConnectionItemVO itemVO = new ConnectionItemVO();
        itemVO.setItemId(dto.getTableName());
        itemVO.setItemName(dto.getTableName());
        return itemVO;
    }
}

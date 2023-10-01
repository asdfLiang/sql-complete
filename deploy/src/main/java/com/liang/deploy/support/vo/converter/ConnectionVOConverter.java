package com.liang.deploy.support.vo.converter;

import com.liang.deploy.support.vo.ConnectionItemVO;
import com.liang.service.support.dto.ColumnDTO;
import com.liang.service.support.dto.ConnectionDTO;
import com.liang.service.support.dto.TableDTO;

/**
 * @since 2023/9/10 9:02
 * @author by liangzj
 */
public class ConnectionVOConverter {

    public static ConnectionItemVO convert(ConnectionDTO dto) {
        ConnectionItemVO itemVO = new ConnectionItemVO();
        itemVO.setConnectionId(dto.getConnectionId());
        itemVO.setItemName(dto.getConnectionName());
        itemVO.setItemType(ConnectionItemVO.SCHEMA);
        return itemVO;
    }

    public static ConnectionItemVO convert(String connectionId, TableDTO dto) {
        ConnectionItemVO itemVO = new ConnectionItemVO();
        itemVO.setConnectionId(connectionId);
        itemVO.setItemName(dto.getTableName());
        itemVO.setItemType(ConnectionItemVO.TABLE);
        return itemVO;
    }

    public static ConnectionItemVO convert(ColumnDTO dto) {
        ConnectionItemVO itemVO = new ConnectionItemVO();
        itemVO.setConnectionId(null);
        itemVO.setItemName(dto.getField());
        itemVO.setItemType(ConnectionItemVO.COLUMN);
        return itemVO;
    }
}

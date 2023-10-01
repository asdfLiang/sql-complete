package com.liang.dal.entity;

import lombok.Data;

/**
 * @since 2023/9/30 21:32
 * @author by liangzj
 */
@Data
public class ProcessSqlDO {

    private Long id;

    private String processId;

    private String processNodeId;

    private String connectionId;

    private String sql;
}

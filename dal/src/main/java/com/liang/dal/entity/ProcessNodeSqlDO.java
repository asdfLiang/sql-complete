package com.liang.dal.entity;

import lombok.Data;

import java.util.Date;

/**
 * @since 2023/9/30 21:32
 * @author by liangzj
 */
@Data
public class ProcessNodeSqlDO {

    private Long id;

    private String processId;

    private String nodeId;

    private String connectionId;

    private String sqlText;

    private Date createTime;

    private Date modifyTime;
}

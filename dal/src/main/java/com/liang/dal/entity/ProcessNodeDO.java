package com.liang.dal.entity;

import lombok.Data;

import java.util.Date;

/**
 * @since 2023/9/30 21:27
 * @author by liangzj
 */
@Data
public class ProcessNodeDO {
    private Long id;

    private String nodeId;

    private String nodeType;

    private String parentId;

    private String processId;

    private Date createTime;

    private Date modifyTime;
}

package com.liang.dal.entity;

import lombok.Data;

import java.util.Date;

/**
 * @since 2023/10/2 8:33
 * @author by liangzj
 */
@Data
public class ProcessSessionDO {

    private Long id;

    private String sessionId;

    private String processId;

    private Date createTime;

    private Date modifyTime;
}

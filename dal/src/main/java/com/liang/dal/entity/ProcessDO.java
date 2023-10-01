package com.liang.dal.entity;

import lombok.Data;

/**
 * sql执行流程
 *
 * @since 2023/9/30 21:29
 * @author by liangzj
 */
@Data
public class ProcessDO {

    private Long id;

    private String processId;

    private String processName;
}

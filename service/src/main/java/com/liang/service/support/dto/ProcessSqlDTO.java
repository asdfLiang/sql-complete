package com.liang.service.support.dto;

import lombok.Data;

/**
 * @since 2023/10/6 16:11
 * @author by liangzj
 */
@Data
public class ProcessSqlDTO {
    private String processId;

    private String nodeId;

    private String connectionId;

    private String sqlText;
}

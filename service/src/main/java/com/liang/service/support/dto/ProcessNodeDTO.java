package com.liang.service.support.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/9/30 22:46
 * @author by liangzj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessNodeDTO {

    private String processId;

    private String parentId;

    private String nodeId;

    private String nodeType;

    private String sql;
}

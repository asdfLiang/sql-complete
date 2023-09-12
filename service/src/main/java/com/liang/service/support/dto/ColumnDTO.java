package com.liang.service.support.dto;

import lombok.Data;

/**
 * @since 2023/9/11 20:30
 * @author by liangzj
 */
@Data
public class ColumnDTO {
    private String field;

    private String type;

    private boolean nullable;

    private boolean isPrimaryKey;
}

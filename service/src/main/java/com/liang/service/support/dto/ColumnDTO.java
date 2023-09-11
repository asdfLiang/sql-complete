package com.liang.service.support.dto;

import lombok.Data;

/**
 * @since 2023/9/11 20:30
 * @author by liangzj
 */
@Data
public class ColumnDTO {
    private String fieldName;

    private String comment;

    private boolean isPrimaryKey;
}

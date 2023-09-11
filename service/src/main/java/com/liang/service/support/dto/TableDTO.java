package com.liang.service.support.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/9/11 20:27
 * @author by liangzj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDTO {
    private String tableName;

    private String comment;
}

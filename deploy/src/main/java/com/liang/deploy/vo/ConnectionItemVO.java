package com.liang.deploy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/9/9 15:44
 * @author by liangzj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionItemVO {
    public static final String SCHEMA = "schema";
    public static final String TABLE = "table";
    public static final String COLUMN = "column";

    private String connectionId;
    private String itemName;

    /** schema,table */
    private String itemType;
}

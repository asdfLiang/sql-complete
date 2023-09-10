package com.liang.dal.entity;

import lombok.Data;

/**
 * @since 2023/9/9 15:44
 * @author by liangzj
 */
@Data
public class ConnectionDefinitionDO {

    private Long id;

    private String connectionId;

    private String connectionName;

    private String url;

    private String databaseName;

    private String username;

    private String password;
}

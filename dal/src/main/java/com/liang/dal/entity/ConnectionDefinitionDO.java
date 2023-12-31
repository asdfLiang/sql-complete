package com.liang.dal.entity;

import lombok.Data;

import java.util.Date;

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

    private String schemaName;

    private String username;

    private String password;

    private Date createTime;

    private Date modifyTime;
}

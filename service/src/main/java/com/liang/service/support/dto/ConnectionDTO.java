package com.liang.service.support.dto;

import lombok.Data;

/**
 * @since 2023/9/9 15:44
 * @author by liangzj
 */
@Data
public class ConnectionDTO {
    private String connectionId;

    private String connectionName;

    private String url;

    private String username;

    private String password;
}

package com.liang.service.support.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @since 2023/10/24 20:44
 * @author by liangzj
 */
@Data
public class ResultDTO {

    private String sql;

    private List<Map<String, String>> resultList;
}

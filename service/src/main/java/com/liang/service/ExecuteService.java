package com.liang.service;

import com.liang.service.support.dto.ResultDTO;

import java.util.List;
import java.util.Map;

/**
 * 流程执行服务
 *
 * @since 2023/10/24 20:39
 * @author by liangzj
 */
public interface ExecuteService {

    /**
     * 执行流程
     *
     * @param processId 流程id
     * @return List<Map<nodeId, Result>>
     */
    List<Map<String, ResultDTO>> execute(String processId);
}

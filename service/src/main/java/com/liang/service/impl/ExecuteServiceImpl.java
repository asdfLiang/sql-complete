package com.liang.service.impl;

import com.liang.service.ExecuteService;
import com.liang.service.support.dto.ResultDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @since 2023/10/24 20:53
 * @author by liangzj
 */
@Service
public class ExecuteServiceImpl implements ExecuteService {
    @Override
    public List<Map<String, ResultDTO>> execute(String processId) {
        // TODO 按照流程执行服务

        return null;
    }
}

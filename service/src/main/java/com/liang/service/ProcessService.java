package com.liang.service;

import com.liang.service.support.dto.ProcessDTO;
import com.liang.service.support.dto.ProcessNodeDTO;

/**
 * @since 2023/9/30 22:44
 * @author by liangzj
 */
public interface ProcessService {

    ProcessDTO save(ProcessDTO dto);

    ProcessNodeDTO saveNode(ProcessNodeDTO dto);

    void deleteNode(String nodeId);

    void saveSql(String nodeId, String connectionId, String sql);
}

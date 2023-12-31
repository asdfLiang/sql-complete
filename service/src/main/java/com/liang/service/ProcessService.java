package com.liang.service;

import com.liang.service.support.dto.ProcessBaseDTO;
import com.liang.service.support.dto.ProcessDTO;
import com.liang.service.support.dto.ProcessNodeDTO;
import com.liang.service.support.dto.ProcessSqlDTO;

import java.util.List;

/**
 * @since 2023/9/30 22:44
 * @author by liangzj
 */
public interface ProcessService {

    ProcessDTO save(ProcessDTO dto);

    ProcessNodeDTO saveNode(ProcessNodeDTO dto);

    void deleteNode(String nodeId);

    ProcessDTO get(String processId);

    List<ProcessBaseDTO> list(List<String> processIds);

    void saveNodeSql(ProcessSqlDTO dto);
}

package com.liang.service.impl;

import com.liang.service.ProcessService;
import com.liang.service.support.dto.ProcessDTO;
import com.liang.service.support.dto.ProcessNodeDTO;

import org.springframework.stereotype.Service;

/**
 * @since 2023/9/30 23:39
 * @author by liangzj
 */
@Service
public class ProcessServiceImpl implements ProcessService {

    @Override
    public ProcessDTO save(ProcessDTO dto) {
        System.out.println("保存了一个流程");
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setProcessId("11111111111111");
        processDTO.setRoot(new ProcessNodeDTO(processDTO.getProcessId(), null, "aaaaaaaaaaa"));
        return processDTO;
    }

    @Override
    public ProcessNodeDTO saveNode(ProcessNodeDTO dto) {
        System.out.println("保存了一个流程节点");
        System.out.println(dto);
        return new ProcessNodeDTO();
    }

    @Override
    public void deleteNode(String nodeId) {
        System.out.println(nodeId);
        System.out.println("删除了一个流程节点");
    }

    @Override
    public void saveSql(String nodeId, String connectionId, String sql) {
        System.out.println("保存了sql");
    }
}

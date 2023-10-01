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
        return new ProcessDTO();
    }

    @Override
    public ProcessNodeDTO saveNode(ProcessNodeDTO dto) {
        System.out.println("保存了一个流程节点");
        return new ProcessNodeDTO();
    }

    @Override
    public void deleteNode(String nodeId) {
        System.out.println("删除了一个流程节点");
    }

    @Override
    public void saveSql(String nodeId, String connectionId, String sql) {
        System.out.println("保存了sql");
    }
}

package com.liang.service.impl;

import com.liang.dal.entity.ProcessDO;
import com.liang.dal.entity.ProcessNodeDO;
import com.liang.dal.mapper.ProcessMapper;
import com.liang.dal.mapper.ProcessNodeMapper;
import com.liang.service.ProcessService;
import com.liang.service.support.constants.NodeType;
import com.liang.service.support.dto.ProcessDTO;
import com.liang.service.support.dto.ProcessNodeDTO;
import com.liang.service.support.dto.converter.ProcessDTOConverter;
import com.liang.service.support.utils.UUIDUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * @since 2023/9/30 23:39
 * @author by liangzj
 */
@Service
public class ProcessServiceImpl implements ProcessService {
    @Autowired private ProcessMapper processMapper;
    @Autowired private ProcessNodeMapper processNodeMapper;

    @Transactional
    @Override
    public ProcessDTO save(ProcessDTO dto) {
        dto.setProcessId(UUIDUtil.get());
        // 保存流程
        ProcessDO processDO = new ProcessDO();
        BeanUtils.copyProperties(dto, processDO);
        processMapper.insert(processDO);

        // 保存根节点
        ProcessNodeDO rootDO = new ProcessNodeDO();
        rootDO.setNodeId(UUIDUtil.get());
        rootDO.setNodeType(NodeType.ROOT.name());
        rootDO.setProcessId(processDO.getProcessId());
        processNodeMapper.insert(rootDO);

        // 回填数据
        ProcessDTO processDTO = new ProcessDTO();
        processDTO.setProcessId(processDO.getProcessId());
        processDTO.setProcessName(processDO.getProcessName());
        processDTO.setRoot(ProcessDTOConverter.convert(rootDO));
        processDTO.setNodes(Collections.singletonList(processDTO.getRoot()));

        System.out.println("保存流程：" + processDTO);
        return processDTO;
    }

    @Override
    public ProcessNodeDTO saveNode(ProcessNodeDTO dto) {
        dto.setNodeId(UUIDUtil.get());
        dto.setNodeType(NodeType.NORMAL.name());
        ProcessNodeDO nodeDO = new ProcessNodeDO();
        BeanUtils.copyProperties(dto, nodeDO);

        processNodeMapper.insert(nodeDO);

        System.out.println("保存流程节点：" + dto);
        return dto;
    }

    @Override
    public void deleteNode(String nodeId) {
        processNodeMapper.delete(nodeId);
        System.out.println("删除流程节点: " + nodeId);
    }

    @Override
    public void saveSql(String nodeId, String connectionId, String sql) {
        System.out.println("保存了sql");
    }
}

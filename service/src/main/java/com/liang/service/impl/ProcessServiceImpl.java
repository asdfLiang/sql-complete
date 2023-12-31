package com.liang.service.impl;

import com.liang.dal.entity.ProcessDO;
import com.liang.dal.entity.ProcessNodeDO;
import com.liang.dal.entity.ProcessNodeSqlDO;
import com.liang.dal.mapper.ProcessMapper;
import com.liang.dal.mapper.ProcessNodeMapper;
import com.liang.dal.mapper.ProcessNodeSqlMapper;
import com.liang.service.ProcessService;
import com.liang.service.support.constants.NodeType;
import com.liang.service.support.dto.ProcessBaseDTO;
import com.liang.service.support.dto.ProcessDTO;
import com.liang.service.support.dto.ProcessNodeDTO;
import com.liang.service.support.dto.ProcessSqlDTO;
import com.liang.service.support.dto.converter.ProcessDTOConverter;
import com.liang.service.support.utils.UUIDUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @since 2023/9/30 23:39
 * @author by liangzj
 */
@Service
public class ProcessServiceImpl implements ProcessService {
    @Autowired private ProcessMapper processMapper;
    @Autowired private ProcessNodeMapper processNodeMapper;
    @Autowired private ProcessNodeSqlMapper processNodeSqlMapper;

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
        processNodeSqlMapper.delete(nodeId);
        System.out.println("删除流程节点: " + nodeId);
    }

    @Override
    public ProcessDTO get(String processId) {
        ProcessDO processDO = processMapper.selectOne(processId);
        if (Objects.isNull(processDO)) return null;
        ProcessDTO dto = new ProcessDTO();
        BeanUtils.copyProperties(processDO, dto);

        List<ProcessNodeDO> nodeDOList = processNodeMapper.select(processId);
        if (CollectionUtils.isEmpty(nodeDOList)) return dto;

        Map<String, ProcessNodeSqlDO> nodeSqlMap = getNodeSqlMap(processId);
        for (ProcessNodeDO nodeDO : nodeDOList) {
            ProcessNodeDTO nodeDTO = new ProcessNodeDTO();
            BeanUtils.copyProperties(nodeDO, nodeDTO);
            if (NodeType.ROOT.name().equals(nodeDTO.getNodeType())) dto.setRoot(nodeDTO);
            dto.getNodes().add(nodeDTO);

            ProcessNodeSqlDO sqlDO = nodeSqlMap.get(nodeDO.getNodeId());
            if (Objects.isNull(sqlDO)) continue;

            nodeDTO.setConnectionId(sqlDO.getConnectionId());
            nodeDTO.setSql(sqlDO.getSqlText());
        }

        return dto;
    }

    @Override
    public List<ProcessBaseDTO> list(List<String> processIds) {
        List<ProcessBaseDTO> processList = listProcess(processIds);
        if (CollectionUtils.isEmpty(processList)) return Collections.emptyList();

        Map<String, ProcessNodeDTO> processRootMap = getProcessRootMap(processIds);

        return processList.stream()
                .filter(process -> Objects.nonNull(processRootMap.get(process.getProcessId())))
                .peek(process -> process.setRoot(processRootMap.get(process.getProcessId())))
                .toList();
    }

    @Override
    public void saveNodeSql(ProcessSqlDTO dto) {
        ProcessNodeSqlDO sqlDO = new ProcessNodeSqlDO();
        BeanUtils.copyProperties(dto, sqlDO);

        processNodeSqlMapper.insertOrUpdate(sqlDO);
    }

    private List<ProcessBaseDTO> listProcess(List<String> processIds) {
        List<ProcessDO> processes = processMapper.selectBatch(processIds);
        if (CollectionUtils.isEmpty(processes)) return Collections.emptyList();

        return processes.stream()
                .map(
                        processDO -> {
                            ProcessBaseDTO dto = new ProcessBaseDTO();
                            BeanUtils.copyProperties(processDO, dto);
                            return dto;
                        })
                .toList();
    }

    /**
     * 获取根节点map
     *
     * @return Map<processId, rootDTO>
     */
    private Map<String, ProcessNodeDTO> getProcessRootMap(List<String> processIds) {
        // 获取流程根节点
        List<ProcessNodeDO> rootList = processNodeMapper.selectRoot(processIds);
        if (CollectionUtils.isEmpty(rootList)) return Collections.emptyMap();

        // 获取根节点的sql
        List<String> nodeIds = rootList.stream().map(ProcessNodeDO::getNodeId).toList();
        List<ProcessNodeSqlDO> sqlDOList = processNodeSqlMapper.selectBatch(nodeIds);
        Map<String, ProcessNodeSqlDO> nodeSqlMap =
                Optional.ofNullable(sqlDOList).orElse(Collections.emptyList()).stream()
                        .collect(
                                Collectors.toMap(ProcessNodeSqlDO::getNodeId, Function.identity()));

        return rootList.stream()
                .collect(
                        Collectors.toMap(
                                ProcessNodeDO::getProcessId,
                                nodeDO -> {
                                    ProcessNodeDTO dto = new ProcessNodeDTO();
                                    BeanUtils.copyProperties(nodeDO, dto);
                                    ProcessNodeSqlDO sqlDO = nodeSqlMap.get(dto.getNodeId());
                                    if (Objects.isNull(sqlDO)) return dto;

                                    dto.setConnectionId(sqlDO.getConnectionId());
                                    dto.setSql(sqlDO.getSqlText());
                                    return dto;
                                }));
    }

    private Map<String, ProcessNodeSqlDO> getNodeSqlMap(String processId) {
        List<ProcessNodeSqlDO> nodeSqlList = processNodeSqlMapper.select(processId);
        if (CollectionUtils.isEmpty(nodeSqlList)) return Collections.emptyMap();

        return nodeSqlList.stream()
                .collect(Collectors.toMap(ProcessNodeSqlDO::getNodeId, Function.identity()));
    }
}

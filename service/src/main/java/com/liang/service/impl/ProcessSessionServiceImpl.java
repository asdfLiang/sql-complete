package com.liang.service.impl;

import com.liang.dal.entity.ProcessSessionDO;
import com.liang.dal.mapper.ProcessSessionMapper;
import com.liang.service.ProcessSessionService;
import com.liang.service.support.dto.ProcessSessionDTO;
import com.liang.service.support.exceptions.BaseException;
import com.liang.service.support.utils.UUIDUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @since 2023/10/2 11:18
 * @author by liangzj
 */
@Slf4j
@Service
public class ProcessSessionServiceImpl implements ProcessSessionService {
    @Autowired private ProcessSessionMapper processSessionMapper;

    @Override
    public String openSession(String processId) throws BaseException {
        // 检查当前流程是否已有会话
        ProcessSessionDO existDO = processSessionMapper.selectByProcessId(processId);
        if (Objects.nonNull(existDO)) throw new BaseException("当前流程已存在会话");

        ProcessSessionDO insertDO = new ProcessSessionDO();
        insertDO.setSessionId(UUIDUtil.get());
        insertDO.setProcessId(processId);
        processSessionMapper.insert(insertDO);

        log.info("打开会话, processId: {} sessionId: {}", processId, insertDO.getSessionId());
        return insertDO.getSessionId();
    }

    @Override
    public void closeSession(String sessionId) {
        Integer number = processSessionMapper.delete(sessionId);
        log.info("关闭会话: {}, sessionId: {}", number, sessionId);
    }

    @Override
    public List<ProcessSessionDTO> list() {
        return null;
    }
}

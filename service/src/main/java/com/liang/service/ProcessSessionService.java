package com.liang.service;

import com.liang.service.support.dto.ProcessSessionDTO;
import com.liang.service.support.exceptions.BaseException;

import java.util.List;

/**
 * @since 2023/10/2 11:17
 * @author by liangzj
 */
public interface ProcessSessionService {

    String openSession(String processId) throws BaseException;

    void closeSession(String sessionId);

    List<ProcessSessionDTO> list();
}

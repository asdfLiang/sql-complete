package com.liang.service;

import com.liang.service.support.exceptions.BaseException;

/**
 * @since 2023/10/2 11:17
 * @author by liangzj
 */
public interface ProcessSessionService {

    String openSession(String processId) throws BaseException;

    void closeSession(String sessionId);
}

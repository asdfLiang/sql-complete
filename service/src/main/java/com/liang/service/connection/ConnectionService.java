package com.liang.service.connection;

import com.liang.service.support.dto.ConnectionDTO;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @since 2023/9/9 15:28
 * @author by liangzj
 */
@Service
public interface ConnectionService {
    void save(ConnectionDTO dto);

    List<ConnectionDTO> all();

    void testConnect(ConnectionDTO dto);
}

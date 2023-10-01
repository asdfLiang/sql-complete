package com.liang.service.support.dto.converter;

import com.liang.dal.entity.ProcessNodeDO;
import com.liang.service.support.dto.ProcessNodeDTO;

import org.springframework.beans.BeanUtils;

/**
 * @since 2023/10/2 0:08
 * @author by liangzj
 */
public class ProcessDTOConverter {
    public static ProcessNodeDTO convert(ProcessNodeDO nodeDO) {
        ProcessNodeDTO nodeDTO = new ProcessNodeDTO();
        BeanUtils.copyProperties(nodeDO, nodeDTO);

        return nodeDTO;
    }
}

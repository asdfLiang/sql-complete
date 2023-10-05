package com.liang.service.support.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/9/30 22:45
 * @author by liangzj
 */
@Data
@NoArgsConstructor
public class ProcessBaseDTO {

    private String processId;

    private String processName;

    private ProcessNodeDTO root = new ProcessNodeDTO();

    public ProcessBaseDTO(String processName) {
        this.processName = processName;
    }
}

package com.liang.service.support.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @since 2023/9/30 22:45
 * @author by liangzj
 */
@Data
@NoArgsConstructor
public class ProcessDTO {

    private String processId;

    private String processName;

    private ProcessNodeDTO root = new ProcessNodeDTO();

    private List<ProcessNodeDTO> nodes;

    public ProcessDTO(String processName) {
        this.processName = processName;
    }
}

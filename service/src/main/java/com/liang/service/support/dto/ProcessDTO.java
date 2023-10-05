package com.liang.service.support.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @since 2023/9/30 22:45
 * @author by liangzj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProcessDTO extends ProcessBaseDTO {

    private List<ProcessNodeDTO> nodes;

    public ProcessDTO(String processName) {
        super(processName);
    }
}

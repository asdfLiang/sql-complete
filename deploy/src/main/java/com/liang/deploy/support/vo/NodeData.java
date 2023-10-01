package com.liang.deploy.support.vo;

import javafx.scene.layout.HBox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/9/30 23:57
 * @author by liangzj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeData {

    private String processId;

    private String nodeId;

    private HBox subNodeHBox;
}

package com.liang.deploy.controller;

import com.liang.deploy.action.AlertAction;
import com.liang.deploy.support.vo.NodeData;
import com.liang.service.ProcessService;
import com.liang.service.support.constants.NodeType;
import com.liang.service.support.dto.ProcessNodeDTO;

import de.felixroske.jfxsupport.FXMLController;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

/**
 * @since 2023/9/24 22:59
 * @author by liangzj
 */
@FXMLController
public class ProcessController {
    @FXML private VBox processRoot;
    @FXML private HBox rootSubNodes;

    @Autowired private ProcessService processService;

    @FXML
    public void initialize() {
        processRoot.setUserData(new NodeData(null, null, rootSubNodes));
    }

    @FXML
    public void addSubNodeToRoot() {
        addSubNode(processRoot);
    }

    @FXML
    public void startProcess() {
        // 检查节点是否有有processId，没有则进行保存
    }

    /**
     * 添加子节点
     *
     * @param node 要添加子节点的节点
     */
    private void addSubNode(VBox node) {
        VBox subNode = buildProcessNode(node);
        subNode.setPadding(new Insets(0, 15, 0, 15));

        // 放到节点下的子节点集合中
        NodeData nodeData = (NodeData) node.getUserData();
        HBox nodeSubHBox = nodeData.getSubNodeHBox();
        nodeSubHBox.getChildren().add(subNode);

        // 边框着色...连线太难了，先整个看的懂的效果
        nodeSubHBox.setStyle("-fx-border-color: black");

        // 保存子节点信息到数据库，回填节点信息
        ProcessNodeDTO dto = new ProcessNodeDTO();
        dto.setProcessId(nodeData.getProcessId());
        dto.setParentId(nodeData.getNodeId());
        dto.setNodeType(NodeType.NORMAL.name());
        ProcessNodeDTO nodeDTO = processService.saveNode(dto);
        // 回填节点信息
        NodeData subNodeData = (NodeData) subNode.getUserData();
        subNodeData.setProcessId(nodeDTO.getProcessId());
        subNodeData.setNodeId(nodeDTO.getNodeId());
    }

    /** 删除子节点 */
    private void removeNode(VBox parent, VBox node) {
        NodeData nodeData = (NodeData) node.getUserData();
        HBox nodeSubHBox = nodeData.getSubNodeHBox();

        // 有子节点不能删除，提示先删子节点
        if (CollectionUtils.isEmpty(nodeSubHBox.getChildren())) {
            HBox parentSubHBox = ((NodeData) parent.getUserData()).getSubNodeHBox();
            parentSubHBox.getChildren().remove(node);
            if (parentSubHBox.getChildren().isEmpty()) parentSubHBox.setStyle(null);

            // 删除数据库中的节点信息
            processService.deleteNode(nodeData.getNodeId());
        } else {
            AlertAction.error("请先删除子节点");
        }
    }

    /** 流程节点 */
    private VBox buildProcessNode(VBox parentNode) {
        VBox node = new VBox();
        node.setAlignment(Pos.TOP_CENTER);

        // 结构定义
        Button removeButton = buildRemoveButton(parentNode, node);
        Pane nodeContent = buildNodeContent();
        Button addButton = buildAddButton(node);
        Line line2 = new Line(0, 0, 0, 30);
        HBox next = new HBox();

        // 组装
        node.getChildren().addAll(removeButton, nodeContent, line2, addButton, next);
        node.setUserData(new NodeData(null, null, next));

        return node;
    }

    /**
     * 添加节点的按钮
     *
     * @param node 要添加的节点
     * @return 添加节点按钮
     */
    private Button buildAddButton(VBox node) {
        Button additionButton = new Button("+");
        additionButton.setPrefWidth(25);
        additionButton.setPrefHeight(25);

        additionButton.setOnAction(event -> addSubNode(node));
        return additionButton;
    }

    /**
     * 删除节点的按钮
     *
     * @param parent 父节点，要从的子节点集合中移除当前节点
     * @param node 要删除的节点
     * @return 删除按钮
     */
    private Button buildRemoveButton(VBox parent, VBox node) {
        Button removeButton = new Button("-");
        removeButton.setPrefWidth(25);
        removeButton.setPrefHeight(25);

        removeButton.setOnAction(event -> removeNode(parent, node));

        return removeButton;
    }

    /** 节点的主体 */
    private Pane buildNodeContent() {
        double TEXT_AREA_WIDTH = 350;
        double TEXT_AREA_HEIGHT = 200;

        TextArea textArea = new TextArea();
        textArea.setPrefWidth(TEXT_AREA_WIDTH);
        textArea.setPrefHeight(TEXT_AREA_HEIGHT);
        textArea.setMaxWidth(TEXT_AREA_WIDTH);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.TOP_CENTER);
        hBox.getChildren().add(textArea);
        return hBox;
    }
}

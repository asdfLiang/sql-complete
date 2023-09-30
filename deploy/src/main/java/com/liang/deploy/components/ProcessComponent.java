package com.liang.deploy.components;

import com.liang.deploy.action.AlertAction;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import org.springframework.util.CollectionUtils;

/**
 * @since 2023/9/24 22:59
 * @author by liangzj
 */
public class ProcessComponent extends AnchorPane {

    public ProcessComponent() {
        super();

        // 添加流程的根节点
        VBox processRoot = processRoot();
        this.getChildren().add(processRoot);
    }

    private VBox processRoot() {
        VBox node = new VBox();
        node.setAlignment(Pos.TOP_CENTER);
        node.setLayoutY(30);

        // 结构定义
        Button startButton = startButton();
        Pane nodeContent = nodeContent();
        Button addButton = addButton(node);
        Line line1 = new Line(0, 0, 0, 30);
        Line line2 = new Line(0, 0, 0, 30);
        HBox next = new HBox();

        // 组装
        node.getChildren().addAll(startButton, line1, nodeContent, line2, addButton, next);
        node.setUserData(next);

        return node;
    }

    private VBox processNode(VBox parentNode) {
        VBox node = new VBox();
        node.setAlignment(Pos.TOP_CENTER);

        // 结构定义
        Button removeButton = removeButton(parentNode, node);
        Pane nodeContent = nodeContent();
        Button addButton = addButton(node);
        Line line2 = new Line(0, 0, 0, 30);
        HBox next = new HBox();

        // 组装
        node.getChildren().addAll(removeButton, nodeContent, line2, addButton, next);
        node.setUserData(next);

        return node;
    }

    /** 节点的主体 */
    private Pane nodeContent() {
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

    private Button startButton() {
        return new Button("开始流程");
    }

    /**
     * 添加节点按钮
     *
     * @param node 要添加的节点
     * @return 添加节点按钮
     */
    private Button addButton(VBox node) {
        Button addButton = new Button("+");
        addButton.setPrefWidth(27);
        addButton.setPrefHeight(27);

        addButton.setOnAction(
                event -> {
                    VBox subNode = processNode(node);
                    subNode.setPadding(new Insets(0, 15, 0, 15));

                    // 放到父节点下的集合中
                    HBox sub = (HBox) node.getUserData();
                    sub.getChildren().add(subNode);

                    // 边框着色...连线太难了，先整个看的懂的效果，后期优化
                    sub.setStyle("-fx-border-color: black");
                });
        return addButton;
    }

    /**
     * 删除节点按钮
     *
     * @param parent 父节点，要从的子节点集合中移除当前节点
     * @param node 要删除的节点
     * @return 删除按钮
     */
    private Button removeButton(VBox parent, VBox node) {
        Button removeButton = new Button("-");
        removeButton.setPrefWidth(27);
        removeButton.setPrefHeight(27);

        removeButton.setOnAction(
                event -> {
                    HBox sub = (HBox) node.getUserData();
                    if (CollectionUtils.isEmpty(sub.getChildren())) {
                        HBox brother = (HBox) parent.getUserData();
                        brother.getChildren().remove(node);
                        if (brother.getChildren().isEmpty()) brother.setStyle(null);
                    } else {
                        AlertAction.error("请先删除子节点");
                    }
                });

        return removeButton;
    }
}

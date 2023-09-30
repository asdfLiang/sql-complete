package com.liang.deploy.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

/**
 * @since 2023/9/24 22:59
 * @author by liangzj
 */
public class ProcessComponent extends AnchorPane {
    private final double PRE_WIDTH;
    private final double TEXT_AREA_WIDTH = 350;
    private final double TEXT_AREA_HEIGHT = 200;

    public ProcessComponent(double preWidth) {
        super();
        this.PRE_WIDTH = preWidth;

        // 创建基本流程
        VBox baseProcess = baseProcess();
        this.getChildren().add(baseProcess);

        // 整个组件始终居中
        this.widthProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (baseProcess.getWidth() == 0) return;
                            baseProcess.setLayoutX(
                                    (newValue.doubleValue() - baseProcess.getWidth()) / 2);
                        });
    }

    private Button startButton() {
        return new Button("开始流程");
    }

    private VBox baseProcess() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setLayoutY(30);

        // 结构定义
        Button startButton = startButton();
        Node textArea = sqlTextArea();
        Button addButton = addButton(vBox);
        Line line1 = new Line(0, 0, 0, 30);
        Line line2 = new Line(0, 0, 0, 30);
        HBox next = new HBox();

        // 组装
        vBox.setUserData(next);
        vBox.getChildren().addAll(startButton, line1, textArea, line2, addButton, next);
        vBox.setLayoutX((PRE_WIDTH - TEXT_AREA_WIDTH) / 2);

        return vBox;
    }

    private VBox processNode(VBox parentNode) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setLayoutY(parentNode.getLayoutY() + parentNode.getHeight() + 25);

        // 结构定义
        Node textArea = sqlTextArea();
        textArea.setLayoutY(25);
        Button addButton = addButton(vBox);
        Line line2 = new Line(0, 0, 0, 30);
        HBox next = new HBox();

        // 组装
        vBox.setUserData(next);
        vBox.getChildren().addAll(textArea, line2, addButton, next);

        return vBox;
    }

    private Node sqlTextArea() {
        TextArea textArea = new TextArea();
        textArea.setPrefWidth(TEXT_AREA_WIDTH);
        textArea.setPrefHeight(TEXT_AREA_HEIGHT);
        textArea.setMaxWidth(TEXT_AREA_WIDTH);

        return textArea;
    }

    /** 添加节点 */
    private Button addButton(VBox preVBox) {
        Button addButton = new Button("+");

        addButton.setOnAction(
                event -> {
                    VBox subNode = processNode(preVBox);
                    subNode.setPadding(new Insets(30, 15, 0, 15));
                    subNode.setLayoutY(preVBox.getLayoutY() + preVBox.getHeight() + 25);

                    // 放到父节点下的集合中
                    HBox sub = (HBox) preVBox.getUserData();
                    sub.setLayoutY(30);
                    sub.getChildren().add(subNode);

                    // 边框着色...连线太难了，先整个看的懂的效果，后期优化
                    sub.setStyle("-fx-border-color: black");
                });
        return addButton;
    }
}

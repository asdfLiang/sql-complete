package com.liang.deploy.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
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

        VBox baseProcess = baseProcess();
        this.getChildren().addAll(baseProcess);

        this.widthProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (baseProcess.getWidth() == 0) return;
                            System.out.println(
                                    "component listener, component width: "
                                            + newValue.doubleValue()
                                            + ", startButton width: "
                                            + baseProcess.getWidth()
                                            + ", layoutX: "
                                            + (newValue.doubleValue() - baseProcess.getWidth())
                                                    / 2);
                            baseProcess.setLayoutX(
                                    (newValue.doubleValue() - baseProcess.getWidth()) / 2);
                        });
    }

    private VBox processNode(VBox parentNode) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        TextArea textArea = sqlTextArea();
        Button addButton = addButton(vBox);
        Line line2 = new Line(0, 0, 0, 30);
        vBox.getChildren().addAll(textArea, line2, addButton);
        return vBox;
    }

    private VBox baseProcess() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setLayoutY(30);

        Button startButton = startButton();
        TextArea textArea = sqlTextArea();
        Button addButton = addButton(null);
        Line line1 = new Line(0, 0, 0, 30);
        Line line2 = new Line(0, 0, 0, 30);
        vBox.getChildren().addAll(startButton, line1, textArea, line2, addButton);

        vBox.setLayoutX((PRE_WIDTH - TEXT_AREA_WIDTH) / 2);

        return vBox;
    }

    private Button startButton() {
        return new Button("开始流程");
    }

    private TextArea sqlTextArea() {
        TextArea textArea = new TextArea();
        textArea.setPrefWidth(TEXT_AREA_WIDTH);
        textArea.setPrefHeight(TEXT_AREA_HEIGHT);

        return textArea;
    }

    private Button addButton(VBox parentNode) {
        Button button = new Button("+");

        button.setOnAction(event -> this.getChildren().add(processNode(parentNode)));

        return button;
    }

    private Line linkLine(VBox start, VBox end) {
        return new Line();
    }
}

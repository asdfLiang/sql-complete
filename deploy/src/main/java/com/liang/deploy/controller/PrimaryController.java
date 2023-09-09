package com.liang.deploy.controller;

import com.liang.deploy.view.ConnectionView;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @since 2023/9/7 7:20
 * @author by liangzj
 */
@FXMLController
public class PrimaryController {
    private final Stage rootStage = AbstractJavaFxApplicationSupport.getStage();

    @Autowired private ConnectionView connectionView;

    /**
     * 打开新建连接窗口
     */
    public void openNewConnectionView() {
        Pane pane = new AnchorPane(connectionView.getView());

        Scene scene = new Scene(pane);

        Stage stage = new Stage();
        stage.setTitle("新建连接");
        stage.setScene(scene);
        stage.initOwner(rootStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}

package com.liang.deploy.controller;

import de.felixroske.jfxsupport.FXMLController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @since 2023/9/9 11:26
 * @author by liangzj
 */
@FXMLController
public class ConnectionController {
    @FXML private TextField connectionName;
    @FXML private TextField url;
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private Button commitBtn;

    /**
     * 保存连接
     */
    public void save() {
        // TODO 将连接信息保存到数据库
        closeStage();
    }

    /**
     * 关闭窗口
     */
    public void closeStage() {
        Stage stage = (Stage) commitBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        // FIXME 增加字段校验,好像有个校验框架?
    }

}

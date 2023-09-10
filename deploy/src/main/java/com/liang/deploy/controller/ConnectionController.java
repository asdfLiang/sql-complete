package com.liang.deploy.controller;

import static com.liang.service.support.Constants.JDBC_REGEX;

import com.liang.service.connection.ConnectionService;
import com.liang.service.support.dto.ConnectionDTO;

import de.felixroske.jfxsupport.FXMLController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @since 2023/9/9 11:26
 * @author by liangzj
 */
@FXMLController
public class ConnectionController {
    @FXML private TextField connectionNameField;
    @FXML private TextField urlField;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button commitBtn;

    @Autowired private ConnectionService connectionService;

    /** 保存新的连接 */
    public void save() {
        if (!validParams()) return;

        ConnectionDTO dto = new ConnectionDTO();
        dto.setConnectionName(connectionNameField.getText());
        dto.setUrl(urlField.getText());
        dto.setUsername(usernameField.getText());
        dto.setPassword(passwordField.getText());
        connectionService.save(dto);

        closeStage();
    }

    /** 关闭窗口 */
    public void closeStage() {
        Stage stage = (Stage) commitBtn.getScene().getWindow();
        stage.close();
    }

    public boolean validParams() {
        String connectionName = connectionNameField.getText();
        String url = urlField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean isValid = true;
        // 连接名称校验
        if (connectionName.isEmpty() || connectionName.length() > 20) {
            connectionNameField.setStyle("-fx-border-color: red; -fx-border-radius: 3");
            isValid = false;
        } else {
            connectionNameField.setStyle(""); // 恢复默认样式
        }

        // url校验
        if (url.isEmpty() || url.length() > 200 || !url.matches(JDBC_REGEX)) {
            urlField.setStyle("-fx-border-color: red; -fx-border-radius: 3");
            isValid = false;
        } else {
            urlField.setStyle(""); // 恢复默认样式
        }

        // 用户名校验
        if (username.isEmpty() || username.length() > 20) {
            usernameField.setStyle("-fx-border-color: red; -fx-border-radius: 3");
            isValid = false;
        } else {
            usernameField.setStyle(""); // 恢复默认样式
        }

        // 密码校验
        if (password.isEmpty() || password.length() > 20) {
            passwordField.setStyle("-fx-border-color: red; -fx-border-radius: 3");
            isValid = false;
        } else {
            passwordField.setStyle(""); // 恢复默认样式
        }

        return isValid;
    }
}

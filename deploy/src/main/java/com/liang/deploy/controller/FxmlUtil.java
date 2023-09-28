package com.liang.deploy.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * @since 2023/9/24 11:12
 * @author by liangzj
 */
public class FxmlUtil {
    public static Pane load(String name) {
        URL url = Objects.requireNonNull(FxmlUtil.class.getResource(name));

        try {
            return FXMLLoader.load(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.liang.deploy;

import com.liang.deploy.view.PrimaryView;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;

import javafx.stage.Stage;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(Application.class, PrimaryView.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        stage.setTitle("sql编排工具");
    }
}

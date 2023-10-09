package com.liang.deploy;

import com.liang.deploy.jfx.view.MainView;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;

import javafx.stage.Stage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan(basePackages = {"com.liang.dal.mapper"})
@ComponentScan(basePackages = {"com.liang.dal", "com.liang.service", "com.liang.deploy"})
@SpringBootApplication
public class Application extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(Application.class, MainView.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        stage.setTitle("sql执行工具");
    }
}

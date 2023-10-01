package com.liang.deploy.support.utils;

import com.liang.service.support.exceptions.BaseException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <b>ClassName</b>: SpringFXMLLoader <br>
 * <b>Description</b>: Will load the FXML hierarchy as specified in the load method and register
 * Spring as the FXML Controller Factory. Allows Spring and Java FX to coexist once the Spring
 * Application context has been bootstrapped.<br>
 * <b>Date</b>: Apr 22, 2019 1:11:58 PM <br>
 *
 * @author pdai
 * @version Apr 22, 2019
 * @apiNote copy from <a
 *     href="https://github.com/realpdai/springboot-javafx-app-demo">springboot-javafx-app-demo</a>
 */
@Slf4j
@Component
public class SpringFXMLLoader {
    private final ApplicationContext context;

    @Autowired
    public SpringFXMLLoader(ApplicationContext context) {
        this.context = context;
    }

    public Parent load(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean); // Spring now FXML Controller Factory
        // loader.setResources(resourceBundle);
        loader.setLocation(getClass().getResource(fxmlPath));
        try {
            return loader.load();
        } catch (IOException e) {
            log.error("load fxml error", e);
            throw new BaseException("布局文件加载异常");
        }
    }
}

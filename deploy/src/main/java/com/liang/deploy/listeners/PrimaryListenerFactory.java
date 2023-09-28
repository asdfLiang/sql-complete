package com.liang.deploy.listeners;

import javafx.beans.value.ChangeListener;

/**
 * @since 2023/9/24 20:42
 * @author by liangzj
 */
public class PrimaryListenerFactory {

    public static ChangeListener<? super Number> tabPaneWidthListener() {
        return (observable, oldValue, newValue) -> {
            System.out.println("processTabPane old width: " + oldValue);
            System.out.println("processTabPane new width: " + newValue);
        };
    }
}

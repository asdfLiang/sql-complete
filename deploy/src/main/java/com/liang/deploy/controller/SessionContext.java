package com.liang.deploy.controller;

import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 2023/10/6 8:36
 * @author by liangzj
 */
@Component
public class SessionContext {
    private static final Map<Tab, VBox> tabRootMap = new HashMap<>();
    private Tab selectedTab;

    public void switchTab(Tab tab) {
        selectedTab = tab;
    }

    public VBox getSelectedRoot() {
        return tabRootMap.get(selectedTab);
    }

    public void putTabRoot(Tab tab, VBox root) {
        tabRootMap.put(tab, root);
    }

    public void removeTab(Tab tab) {
        tabRootMap.remove(tab);
    }
}

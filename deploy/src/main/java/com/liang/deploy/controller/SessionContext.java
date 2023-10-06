package com.liang.deploy.controller;

import com.liang.service.support.dto.ConnectionDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 2023/10/6 8:36
 * @author by liangzj
 */
@Component
public class SessionContext {
    private static final Map<Tab, VBox> TAB_ROOT_MAP = new HashMap<>();
    private static final ObservableList<ConnectionDTO> OBSERVABLE_CONNECTION_LIST =
            FXCollections.observableList(new ArrayList<>());
    private Tab selectedTab;

    public void switchTab(Tab tab) {
        selectedTab = tab;
    }

    public VBox getSelectedRoot() {
        return TAB_ROOT_MAP.get(selectedTab);
    }

    public void putTabRoot(Tab tab, VBox root) {
        TAB_ROOT_MAP.put(tab, root);
    }

    public void removeTab(Tab tab) {
        TAB_ROOT_MAP.remove(tab);
    }

    public ObservableList<ConnectionDTO> getConnectionList() {
        return OBSERVABLE_CONNECTION_LIST;
    }

    public void flushConnectionList(List<ConnectionDTO> connections) {
        OBSERVABLE_CONNECTION_LIST.clear();
        OBSERVABLE_CONNECTION_LIST.addAll(connections);
    }
}

package com.liang.deploy.controller;

import com.liang.deploy.jfx.SpringFXMLLoader;
import com.liang.deploy.vo.ConnectionItemVO;
import com.liang.deploy.vo.NodeData;
import com.liang.deploy.vo.converter.ConnectionVOConverter;
import com.liang.service.ConnectionService;
import com.liang.service.ProcessService;
import com.liang.service.ProcessSessionService;
import com.liang.service.support.dto.*;
import com.liang.service.support.events.ConnectionsChangeEvent;
import com.liang.service.support.events.ProcessTabSelectedEvent;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @since 2023/9/7 7:20
 * @author by liangzj
 */
@FXMLController
public class MainController {
    private final Stage rootStage = AbstractJavaFxApplicationSupport.getStage();

    @FXML private TreeView<ConnectionItemVO> connectionTree;
    @FXML private TabPane processTabPane;

    @Autowired private SpringFXMLLoader springFXMLLoader;

    @Autowired private ConnectionService connectionService;
    @Autowired private ProcessService processService;
    @Autowired private ProcessSessionService processSessionService;
    @Autowired private SessionContext sessionContext;
    @Autowired private ApplicationEventPublisher applicationEventPublisher;

    /** 打开新建连接窗口 */
    @FXML
    public void openNewConnectionView() {
        Parent view = springFXMLLoader.load("/fxml/create-connection.fxml");

        Stage stage = new Stage();
        stage.setTitle("数据库连接");
        stage.setScene(new Scene(view));
        stage.initOwner(rootStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /** 打开新建流程窗口 */
    @FXML
    public void openNewProcessTab() {
        int size = processTabPane.getTabs().size();
        System.out.println(size);
        // 保存流程到数据库，并回填根节点信息
        ProcessDTO processDTO = processService.save(new ProcessDTO("新建流程" + size));

        // 创建会话
        String sessionId = processSessionService.openSession(processDTO.getProcessId());

        // 添加到流程到tab
        Tab tab = addTab(sessionId, processDTO);
        processTabPane.getSelectionModel().select(tab);
        sessionContext.switchTab(tab);
    }

    @FXML
    public void initialize() {
        initConnectionTree();

        initProcessTab();
    }

    /** 初始化tab */
    private void initProcessTab() {
        // 查询未关闭会话
        List<ProcessSessionDTO> sessions = processSessionService.list();
        if (CollectionUtils.isEmpty(sessions)) return;

        // 查询流程
        List<String> processIds = sessions.stream().map(ProcessSessionDTO::getProcessId).toList();
        Map<String, ProcessBaseDTO> processMap =
                processService.list(processIds).stream()
                        .collect(
                                Collectors.toMap(
                                        ProcessBaseDTO::getProcessId, Function.identity()));
        if (CollectionUtils.isEmpty(processMap)) return;

        // 添加流程到tab
        sessions.forEach(
                session -> addTab(session.getSessionId(), processMap.get(session.getProcessId())));
    }

    /** 初始化连接列表 */
    private void initConnectionTree() {
        connectionTree.setRoot(new TreeItem<>(new ConnectionItemVO("", "所有连接", "")));
        // 设置展示文案为数据库名称
        connectionTree.setCellFactory(
                treeView ->
                        new TreeCell<>() {
                            @Override
                            protected void updateItem(ConnectionItemVO item, boolean empty) {
                                super.updateItem(item, empty);
                                setText((empty || item == null) ? null : item.getItemName());
                            }
                        });

        // 设置点击事件，点击连接后展示数据库连接的表
        connectionTree.setOnMouseClicked(
                event -> {
                    TreeItem<ConnectionItemVO> selectedItem =
                            connectionTree.getSelectionModel().getSelectedItem();
                    if (selectedItem != null && !selectedItem.isExpanded()) {
                        expandConnectionItem(selectedItem);
                    }
                });

        //
        flushConnectionList();
        connectionTree.getRoot().setExpanded(true);
    }

    @EventListener(classes = {ConnectionsChangeEvent.class})
    public void flushConnectionList() {
        TreeItem<ConnectionItemVO> root = connectionTree.getRoot();

        // 查询所有连接信息
        List<ConnectionDTO> all = connectionService.all();
        root.getChildren().removeAll(root.getChildren());

        // 刷新列表
        if (CollectionUtils.isEmpty(all)) return;
        for (ConnectionDTO dto : all) {
            ConnectionItemVO itemVO = ConnectionVOConverter.convert(dto);
            TreeItem<ConnectionItemVO> item = new TreeItem<>(itemVO);
            root.getChildren().add(item);
        }

        sessionContext.flushConnectionList(all);
    }

    private Tab addTab(String sessionId, ProcessBaseDTO processDTO) {
        // 加载流程布局
        Parent view = springFXMLLoader.load("/fxml/process-root.fxml");
        ScrollPane scrollPane = new ScrollPane(view);
        scrollPane.setStyle("-fx-background-color: green");

        // 填充节点数据
        VBox processRoot = (VBox) view.lookup("#processRoot");
        NodeData nodeData = (NodeData) processRoot.getUserData();
        nodeData.setProcessId(processDTO.getProcessId());
        nodeData.setNodeId(processDTO.getRoot().getNodeId());
        //
        TextArea processSql = (TextArea) view.lookup("#processSql");
        processSql.setText(processDTO.getRoot().getSql());

        // 创建tab
        Tab tab = new Tab(processDTO.getProcessName(), scrollPane);
        tab.selectedProperty().addListener(handleTabSelectedEvent(processRoot, tab));
        tab.setOnClosed(event -> handleTabClosedEvent(sessionId, tab));

        processTabPane.getTabs().add(tab);
        sessionContext.putTabRoot(tab, processRoot);

        return tab;
    }

    private void handleTabClosedEvent(String sessionId, Tab tab) {
        processSessionService.closeSession(sessionId);
        sessionContext.removeTab(tab);
    }

    private ChangeListener<Boolean> handleTabSelectedEvent(VBox processRoot, Tab tab) {
        return (observable, oldValue, newValue) -> {
            if (newValue) {
                applicationEventPublisher.publishEvent(new ProcessTabSelectedEvent(processRoot));
                sessionContext.switchTab(tab);
            }
        };
    }

    private void expandConnectionItem(TreeItem<ConnectionItemVO> selectedItem) {
        ConnectionItemVO value = selectedItem.getValue();

        if (ConnectionItemVO.SCHEMA.equals(value.getItemType())) {
            expandTables(selectedItem, value.getConnectionId());
        } else if (ConnectionItemVO.TABLE.equals(value.getItemType())) {
            expandColumns(selectedItem, value.getConnectionId(), value.getItemName());
        }
    }

    private void expandTables(TreeItem<ConnectionItemVO> rootItem, String connectionId) {
        List<TableDTO> tables = connectionService.tables(connectionId);
        rootItem.getChildren().removeAll(rootItem.getChildren());
        for (TableDTO dto : tables) {
            ConnectionItemVO itemVO = ConnectionVOConverter.convert(connectionId, dto);
            TreeItem<ConnectionItemVO> item = new TreeItem<>(itemVO);
            rootItem.getChildren().add(item);
        }
    }

    private void expandColumns(
            TreeItem<ConnectionItemVO> rootItem, String connectionId, String tableName) {
        List<ColumnDTO> columns = connectionService.descTable(connectionId, tableName);
        rootItem.getChildren().removeAll(rootItem.getChildren());
        for (ColumnDTO dto : columns) {
            ConnectionItemVO itemVO = ConnectionVOConverter.convert(dto);
            TreeItem<ConnectionItemVO> item = new TreeItem<>(itemVO);
            rootItem.getChildren().add(item);
        }
    }
}

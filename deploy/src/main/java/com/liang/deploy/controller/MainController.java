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

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.util.CollectionUtils;

import java.util.List;

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
        // 保存流程到数据库，并回填根节点信息
        ProcessDTO processDTO = processService.save(new ProcessDTO("新建流程"));

        // 添加到流程到tab
        add2Tab(processDTO);
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
        List<ProcessDTO> list = processService.list(processIds);
        if (CollectionUtils.isEmpty(list)) return;

        // 添加流程到tab
        list.forEach(this::add2Tab);
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
    }

    private void add2Tab(ProcessDTO processDTO) {
        Parent view = springFXMLLoader.load("/fxml/process-root.fxml");
        ScrollPane scrollPane = new ScrollPane(view);
        scrollPane.setStyle("-fx-background-color: green");
        Tab tab = new Tab("新建流程", scrollPane);

        processTabPane.getTabs().add(tab);

        // 回填
        NodeData nodeData = (NodeData) view.lookup("#processRoot").getUserData();
        nodeData.setProcessId(processDTO.getProcessId());
        nodeData.setNodeId(processDTO.getRoot().getNodeId());

        // 创建会话，添加会话删除事件
        String sessionId = processSessionService.openSession(processDTO.getProcessId());
        tab.setOnClosed(event -> processSessionService.closeSession(sessionId));
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

package com.liang.deploy.controller;

import com.liang.deploy.controller.converter.ConnectionVOConverter;
import com.liang.deploy.controller.vo.ConnectionVO;
import com.liang.deploy.view.ConnectionView;
import com.liang.service.connection.ConnectionService;
import com.liang.service.support.dto.ConnectionDTO;
import com.liang.service.support.events.ConnectionsChangeEvent;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
public class PrimaryController {
    private final Stage rootStage = AbstractJavaFxApplicationSupport.getStage();

    @FXML private TreeView<ConnectionVO> connectionTree;

    @Autowired private ConnectionView connectionView;
    @Autowired private ConnectionService connectionService;

    /** 打开新建连接窗口 */
    @FXML
    public void openNewConnectionView() {
        Pane pane = new AnchorPane(connectionView.getView());

        Scene scene = new Scene(pane);

        Stage stage = new Stage();
        stage.setTitle("新建连接");
        stage.setScene(scene);
        stage.initOwner(rootStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    public void initialize() {
        connectionTree.setRoot(new TreeItem<>(new ConnectionVO("", "所有连接")));
        // 设置展示文案为数据库名称
        connectionTree.setCellFactory(
                treeView ->
                        new TreeCell<>() {
                            @Override
                            protected void updateItem(ConnectionVO item, boolean empty) {
                                super.updateItem(item, empty);
                                setText((empty || item == null) ? null : item.getConnectionName());
                            }
                        });

        // 设置点击事件，点击连接后展示数据库连接的表
        connectionTree.setOnMouseClicked(
                event -> {
                    TreeItem<ConnectionVO> selectedItem =
                            connectionTree.getSelectionModel().getSelectedItem();
                    if (selectedItem != null && selectedItem != connectionTree.getRoot()) {
                        showTables(selectedItem, selectedItem.getValue().getConnectionId());
                    }
                });

        //
        flushConnectionList();
    }

    @EventListener(classes = {ConnectionsChangeEvent.class})
    public void flushConnectionList() {
        TreeItem<ConnectionVO> root = connectionTree.getRoot();

        // 查询所有连接信息
        List<ConnectionDTO> all = connectionService.all();
        root.getChildren().removeAll(root.getChildren());

        // 刷新列表
        if (CollectionUtils.isEmpty(all)) return;
        for (ConnectionDTO dto : all) {
            TreeItem<ConnectionVO> item = new TreeItem<>(ConnectionVOConverter.convert(dto));
            root.getChildren().add(item);
        }
    }

    public void showTables(TreeItem<ConnectionVO> treeItem, String connectionId) {
        System.out.println(connectionId);
    }
}

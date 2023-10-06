package com.liang.deploy.controller.handler;

import com.liang.deploy.jfx.AlertAction;
import com.liang.service.ProcessService;
import com.liang.service.support.dto.ConnectionDTO;
import com.liang.service.support.dto.ProcessSqlDTO;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @since 2023/10/6 19:57
 * @author by liangzj
 */
@Component
public class ProcessEventHandler {
    @Autowired private ProcessService processService;

    public ChangeListener<Boolean> saveSqlEventHandler(
            TextArea sqlTextArea, ChoiceBox<ConnectionDTO> choiceBox, ProcessSqlDTO dto) {
        return (observable, oldValue, newValue) -> {
            if (newValue) return;

            if (StringUtils.isBlank(sqlTextArea.getText())) return;

            if (Objects.isNull(choiceBox.getValue())) {
                AlertAction.error("请选择数据库连接");
                return;
            }

            dto.setConnectionId(choiceBox.getValue().getConnectionId());
            dto.setSqlText(sqlTextArea.getText());
            processService.saveNodeSql(dto);
        };
    }

    public void selectConnection(ChoiceBox<ConnectionDTO> choiceBox, String connectionId) {
        if (StringUtils.isNotBlank(connectionId)) {
            ConnectionDTO connectionDTO =
                    choiceBox.getItems().stream()
                            .filter(elm -> StringUtils.equals(elm.getConnectionId(), connectionId))
                            .findFirst()
                            .orElse(null);
            //
            if (Objects.nonNull(connectionDTO)) choiceBox.getSelectionModel().select(connectionDTO);
            else choiceBox.getSelectionModel().select(0);
        } else {
            choiceBox.getSelectionModel().select(0);
        }
    }
}

package com.liang.deploy.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * @since 2023/10/1 1:54
 * @author by liangzj
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@FXMLView(value = "/fxml/process-root.fxml")
public class ProcessView extends AbstractFxmlView {}

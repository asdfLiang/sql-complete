# sql执行工具

## 项目介绍

这是一个sql执行工具，它能够按照一定流程执行sql语句。

## 项目演示

### 创建连接

<img src="https://github.com/asdfLiang/sql-complete/blob/master/deploy/src/main/resources/guide/new_connection.png" width= "500px">

### 创建流程

<img src="https://github.com/asdfLiang/sql-complete/blob/master/deploy/src/main/resources/guide/process.png" width= "500px">

### 执行结果

待开发

## 项目结构

``` lua
sql-complete
├── deploy -- 启动模块，包含项目的JavaFx界面及启动入口
├── service -- 业务模块，实现数据库连接管理、sql流程管理、会话管理及执行sql流程等功能
└── dal -- 持久化模块，保存用户设置的sql流程结构、sql语句及会话等数据
```

## 部署说明

#### 环境说明

1. JDK17
2. JavaFx SDK 20
3. sqlite3

#### 启动过程

1. 下载并解压 JavaFx SDK 到本地，下载地址：https://openjfx.cn/
2. Idea添加VM参数：--module-path "E:\produce\javafx-sdk-20\lib" --add-modules javafx.controls,javafx.fxml
3. 启动项目

## 项目进度

### 2023-10-09

- 连接管理：
    - 已实现：1. 新建连接 2.连接列表树形展示；
    - 待实现：1. 连接删除 2. 连接列表项增加图标
- 流程管理：
    - 已实现：1. 流程节点增删 2.按照数据构建流程结构；
    - 待实现：1. 流程打开 2. 流程删除
- 会话管理：
    - 已实现：1. 打开软件恢复上次会话 2. 会话删除；
- sql执行：
    - 已实现：未开始；
    - 待实现：1. 单个sql执行 2. 父节点sql参数解析 3. sql流程执行

## 待优化点记录

- 支持多版本mysql连接，目前只支持MySQL 8
- tab中的流程居中
- sql语句关键字高亮


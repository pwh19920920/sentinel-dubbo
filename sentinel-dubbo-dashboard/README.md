# 一、修改说明
1. 控制台加入zookeeper，并定义一系列的相关路径约定。
2. 规则走向：控制台 -> zookeeper -> 客户端。

# 二、核心配置
```
sentinel.application.name=sentinel-dashboard # 名字
sentinel.application.port=8719 # 数据端口
sentinel.application.dashboard=localhost:8181 # 控制台地址
sentinel.zookeeper.enable=true # 是否开启zookeeper作为datasource
sentinel.zookeeper.address=localhost:2181 # zookeeper配置
```
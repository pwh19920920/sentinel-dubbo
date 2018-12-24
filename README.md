# 本项目为sentinel与dubbo结合的改造项目。
## 一、由来
Sentinel 是阿里中间件团队开源的，面向分布式服务架构的轻量级流量控制产品，主要以流量为切入点，从流量控制、熔断降级、系统负载保护等多个维度来帮助用户保护服务的稳定性。
点此地址了解更多[Sentinel](https://github.com/alibaba/Sentinel/wiki/%E4%B8%BB%E9%A1%B5)。

## 二、问题
1. Sentinel设定除了限流异常以外都会被认为是需要进行熔断统计，你自己定义的业务异常也毫无例外的在熔断统计范围内。
2. Sentinel的启动配置太过原始，一定要在java -jar的时候加参数启动，调试困难。
3. Sentinel Dashboard不支持将配置发送到datasource中，需要进行一定的改造。

## 三、实现
1. 新增dubbo的filter将异常包装成统一返回体，将异常状态码定义为>=500的值(与HttpStatus相对应)。并定义相关的状态码，并修改SentinelResourceAspect实现，判断返回的状态码是否为>=500，如果是则进行熔断统计。
2. 并新增sentinel-dubbo-starter，进行自动配置化。
3. Sentinel Dashboard改造：控制台规则 -> 配置中心 -> 客户端。

## 四、Dashboard改造
### 改造前
客户端利用sentinel-transport-simple-http模块暴露一个特定的端口，Sentinel Dashboard通过http的形式进行数据推送，客户端接收后将规则保存在本地内存中。
![改造前](https://camo.githubusercontent.com/96b07d598c9eee5a513801cecc68f266fbe58d14/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f6c61726b2f302f323031382f706e672f34373638382f313533363636303239363237332d34663434306262612d356239652d343230352d393430322d6662363038336236363931322e706e67)

### 改造后
客户端注册到相关的注册中心中，Sentinel Dashboard控制台将配置信息推送到配置中心，如nacos，zookeeper中，由配置中心去进行配置推送。
![改造后](https://user-images.githubusercontent.com/9434884/45406233-645e8380-b698-11e8-8199-0c917403238f.png)

### 路径约定
1. 流量控制规则：/sentinel/rules/{appName}/flow
2. 黑白名单规则：/sentinel/rules/{appName}/authority
3. 熔断降级规则：/sentinel/rules/{appName}/degrade
4. 热点参数规则：/sentinel/rules/{appName}/param
5. 负载保护规则：/sentinel/rules/{appName}/system

### 改造支持
1. 改造后Sentinel Dashboard支持api推送以及zookeeper推送。
2. 其他配置中心，如nacos，改造入口可以参考以下实现，并加入Configuration注入。
```
DynamicRuleZookeeperProvider
DynamicRuleZookeeperPublisher
```

## 五、使用
1.运行sentinel-dubbo-dashboard
```
sentinel.application.name=sentinel-dashboard # 名字
sentinel.application.port=8719 # sentinel的http访问端口
sentinel.application.dashboard=localhost:8181 # 控制台地址
sentinel.zookeeper.enable=true # 是否开启zookeeper作为datasource
sentinel.zookeeper.address=localhost:2181 # zookeeper配置
```
配置并运行DashboardApplication

2.客户端加入相关的依赖
```
<dependency>
    <groupId>com.xmutca</groupId>
    <artifactId>sentinel-dubbo-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
客户端加入相关的配置
```
sentinel:
  application:
    name: sentinel-provider # 服务的名字
    port: 8719 # sentinel的http访问端口
    dashboard: localhost:8080 # sentinel dashboard地址
  zookeeper:
    enable: true # 是否使用zookeeper作为datasource
    address: localhost:2181 # zookeeper的地址
```
## 六、思考
1. sentinel针对问题1其实也可以采用直接抛异常的方式，然后在SentinelResourceAspect实现中对业务异常进行排除。没有用这个方案的主要原因是考虑到dubbo也有可能直接对外提供服务（如dubbo2.js），直接抛出异常的方式实在是太过粗暴，也不太友好。
2. sentinel客户端使用datasource需要控制台的相关支持，举个例子就是zookeeper的path要一一对应才能获取相关配置，因此也改了一版的dashboard。
3. 项目中对外服务中异常的处理：如果是通过dubbo本身调用抛的异常，用dubbo的filter对异常进行了包装，如果是集成了web服务以后，http服务抛出来的异常可以使用ControllerAdvice来做相关异常处理。
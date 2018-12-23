# 本项目为sentinel与dubbo结合的改造项目。
## 一、问题
1. sentinel设定除了限流异常以外都会被认为是需要进行熔断统计，你自己定义的业务异常也毫无例外的在熔断统计范围内。
2. sentinel的启动配置太过原始，一定要在java -jar的时候加参数启动，调试困难。

## 二、实现
1. 新增dubbo的filter将异常包装成统一返回体，将异常状态码定义为>=500的值(与HttpStatus相对应)。并定义相关的状态码，并修改SentinelResourceAspect实现，判断返回的状态码是否为>=500，如果是则进行熔断统计。
2. 并新增sentinel-starter，进行自动配置化。

## 三、使用
1.加入相关的依赖
```
<dependency>
    <groupId>com.xmutca</groupId>
    <artifactId>sentinel-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

2.加入相关的配置
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
## 四、思考
1. sentinel针对问题1其实也可以采用直接抛异常的方式，然后在SentinelResourceAspect实现中对业务异常进行排除。没有用这个方案的主要原因是考虑到dubbo也有可能直接对外提供服务（如dubbo2.js），直接抛出异常的方式实在是太过粗暴，也不太友好。
2. sentinel客户端使用datasource需要控制台的相关支持，举个例子就是zookeeper的path要一一对应才能获取相关配置，因此本人也改了一版的dashboard，地址也会放到git中。
3. 项目中对外服务中异常的处理：如果是通过dubbo本身调用抛的异常，用dubbo的filter对异常进行了包装，如果是集成了web服务以后，http服务抛出来的异常可以使用ControllerAdvice来做相关异常处理。
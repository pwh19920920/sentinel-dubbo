package com.xmutca.sentinel.dubbo.starter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-23
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "sentinel")
public class SentinelProperties {

    /**
     * 应用配置
     */
    private ApplicationProperties application;

    /**
     * zk配置
     */
    private ZookeeperProperties zookeeper;

    /**
     * nacos配置中心
     */
    private NacosProperties nacos;

    /**
     * 应用配置
     */
    @Getter
    @Setter
    public static class ApplicationProperties {

        /**
         * 客户端的 port，用于上报相关信息（默认为 8719）, 同台机器上由多台时，需要指定不同的端口
         */
        private String port;

        /**
         * 控制台的地址 IP + 端口
         */
        private String dashboard;

        /**
         * 应用名称，会在控制台中显示
         */
        private String name;

        /**
         * 日志地址
         */
        private String logDir;
    }


    /**
     * zookeeper配置文件
     */
    @Getter
    @Setter
    public static class ZookeeperProperties {

        /**
         * 服务器地址
         */
        private String address;

        /**
         * 睡眠时间
         */
        private int sleepTimeMs = 100;

        /**
         * 最大重试
         */
        private int maxRetries = 3;

        /**
         * 是否开启
         */
        private boolean enable;
    }

    /**
     * nacos配置文件
     */
    public static class NacosProperties {

        /**
         * nacos配置中心地址
         */
        private String serverAddr;

        /***
         * 分组ID
         */
        private String groupId;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getServerAddr() {
            return serverAddr;
        }

        public void setServerAddr(String serverAddr) {
            this.serverAddr = serverAddr;
        }
    }

    public ApplicationProperties getApplication() {
        if (null == application) {
            application = new ApplicationProperties();
        }
        return application;
    }
}

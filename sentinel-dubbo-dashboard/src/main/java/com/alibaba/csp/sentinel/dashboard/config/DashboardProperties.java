package com.alibaba.csp.sentinel.dashboard.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-22
 */
@ConfigurationProperties(prefix = "sentinel")
public class DashboardProperties {

    /**
     * zookeeper配置文件
     */
    private ZookeeperProperties zookeeper;

    /**
     * nacos配置中心
     */
    private NacosProperties nacos;

    /**
     * 系统属性
     */
    public ApplicationProperties application;

    /**
     * 系统属性
     */
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

        public String getLogDir() {
            return logDir;
        }

        public void setLogDir(String logDir) {
            this.logDir = logDir;
        }

        public String getDashboard() {
            return dashboard;
        }

        public String getName() {
            return name;
        }

        public String getPort() {
            return port;
        }

        public void setDashboard(String dashboard) {
            this.dashboard = dashboard;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPort(String port) {
            this.port = port;
        }
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

    /**
     * zookeeper配置文件
     */
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

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getMaxRetries() {
            return maxRetries;
        }

        public int getSleepTimeMs() {
            return sleepTimeMs;
        }

        public void setMaxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
        }

        public void setSleepTimeMs(int sleepTimeMs) {
            this.sleepTimeMs = sleepTimeMs;
        }
    }

    public ZookeeperProperties getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(ZookeeperProperties zookeeper) {
        this.zookeeper = zookeeper;
    }

    public void setApplication(ApplicationProperties application) {
        this.application = application;
    }

    public ApplicationProperties getApplication() {
        if (null == application) {
            application = new ApplicationProperties();
        }
        return application;
    }

    public NacosProperties getNacos() {
        return nacos;
    }

    public void setNacos(NacosProperties nacos) {
        this.nacos = nacos;
    }
}

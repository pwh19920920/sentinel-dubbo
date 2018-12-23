package com.taobao.csp.sentinel.dashboard.config;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-22
 */
@Configuration
@EnableConfigurationProperties(DashboardProperties.class)
public class DashboardConfiguration implements InitializingBean {

    private static final String LOG_DIR_PROP = "csp.sentinel.log.dir";
    private static final String PRO_NAME_PROP = "project.name";
    private static final String APP_NAME_PROP = "spring.application.name";

    @Autowired
    private DashboardProperties dashboardProperties;

    @Autowired
    private Environment env;

    @Override
    public void afterPropertiesSet() {
        // 设置日志地址
        if(StringUtil.isBlank(System.getProperty(LOG_DIR_PROP))
                && StringUtil.isNotBlank(dashboardProperties.getApplication().getLogDir())){
            System.setProperty(LOG_DIR_PROP, dashboardProperties.getApplication().getLogDir());
        }

        // 设置项目名称
        if(StringUtil.isBlank(System.getProperty(PRO_NAME_PROP))){
            String name = env.getProperty(PRO_NAME_PROP);
            if (StringUtil.isBlank(name)){
                name = dashboardProperties.getApplication().getName();
            }

            if (StringUtil.isBlank(name)){
                name = env.getProperty(APP_NAME_PROP);
            }

            if (StringUtil.isNotBlank(name)){
                System.setProperty(PRO_NAME_PROP, name);
            }
        }

        // 设置主页
        if (StringUtil.isNotBlank(dashboardProperties.getApplication().getDashboard())) {
            SentinelConfig.setConfig(TransportConfig.CONSOLE_SERVER, dashboardProperties.getApplication().getDashboard());
        }

        // 设置端口
        if (StringUtil.isNotBlank(dashboardProperties.getApplication().getPort())) {
            SentinelConfig.setConfig(TransportConfig.SERVER_PORT, dashboardProperties.getApplication().getPort());
        }
    }
}

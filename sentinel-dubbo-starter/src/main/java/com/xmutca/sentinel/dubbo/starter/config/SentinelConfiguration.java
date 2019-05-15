package com.xmutca.sentinel.dubbo.starter.config;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.AppNameUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xmutca.sentinel.dubbo.starter.aspect.SentinelRequestMappingAspect;
import com.xmutca.sentinel.dubbo.starter.aspect.SentinelResourceExtendAspect;
import com.xmutca.sentinel.dubbo.starter.reflect.ExceptionRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Optional;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-23
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(SentinelProperties.class)
public class SentinelConfiguration implements InitializingBean {

    public static final String LOG_DIR_PROP = "csp.sentinel.log.dir";
    public static final String PRO_NAME_PROP = "project.name";
    public static final String APP_NAME_PROP = "spring.application.name";

    @Autowired
    private SentinelProperties sentinelProperties;

    @Autowired
    private Environment env;

    // 为了让默认异常注册代码优先执行
    static {
        ExceptionRegistry.updateForPackage("com.xmutca.sentinel.dubbo.starter.exception");
    }

    /**
     * 熔断器注解支持
     * @return
     */
    @Bean
    public SentinelResourceExtendAspect sentinelResourceAspect() {
        return new SentinelResourceExtendAspect();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 设置日志地址
        if(StringUtil.isBlank(System.getProperty(LOG_DIR_PROP))
                && StringUtil.isNotBlank(sentinelProperties.getApplication().getLogDir())){
            System.setProperty(LOG_DIR_PROP, sentinelProperties.getApplication().getLogDir());
        }

        // 设置项目名称
        if(StringUtil.isBlank(System.getProperty(PRO_NAME_PROP))){
            String name = env.getProperty(PRO_NAME_PROP);
            if (StringUtil.isBlank(name)){
                name = sentinelProperties.getApplication().getName();
            }

            if (StringUtil.isBlank(name)){
                name = env.getProperty(APP_NAME_PROP);
            }

            if (StringUtil.isNotBlank(name)){
                System.setProperty(PRO_NAME_PROP, name);
            }
        }

        // 设置主页
        if (StringUtil.isNotBlank(sentinelProperties.getApplication().getDashboard())) {
            SentinelConfig.setConfig(TransportConfig.CONSOLE_SERVER, sentinelProperties.getApplication().getDashboard());
        }

        // 设置端口
        if (StringUtil.isNotBlank(sentinelProperties.getApplication().getPort())) {
            SentinelConfig.setConfig(TransportConfig.SERVER_PORT, sentinelProperties.getApplication().getPort());
        }
    }

    /**
     * zookeeper的datasource注册
     */
    @Configuration
    @ConditionalOnProperty(name = "sentinel.zookeeper.enable", havingValue = "true")
    public class ZookeeperDataSourceConfiguration implements InitializingBean {

        public static final String FLOW_RULE_PATH = "/sentinel/rules/%s/flow";
        public static final String AUTHORITY_RULE_PATH = "/sentinel/rules/%s/authority";
        public static final String DEGRADE_RULE_PATH = "/sentinel/rules/%s/degrade";
        public static final String PARAM_FLOW_RULE_PATH = "/sentinel/rules/%s/param";
        public static final String SYSTEM_RULE_PATH = "/sentinel/rules/%s/system";

        @Override
        public void afterPropertiesSet() throws Exception {
            ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(sentinelProperties.getZookeeper().getAddress(),
                    String.format(FLOW_RULE_PATH, AppNameUtil.getAppName()),
                    source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
            FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

            ReadableDataSource<String, List<AuthorityRule>> authorityRleDataSource = new ZookeeperDataSource<>(sentinelProperties.getZookeeper().getAddress(),
                    String.format(AUTHORITY_RULE_PATH, AppNameUtil.getAppName()),
                    source -> JSON.parseObject(source, new TypeReference<List<AuthorityRule>>() {}));
            AuthorityRuleManager.register2Property(authorityRleDataSource.getProperty());

            ReadableDataSource<String, List<DegradeRule>> degradeRleDataSource = new ZookeeperDataSource<>(sentinelProperties.getZookeeper().getAddress(),
                    String.format(DEGRADE_RULE_PATH, AppNameUtil.getAppName()),
                    source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {}));
            DegradeRuleManager.register2Property(degradeRleDataSource.getProperty());

            ReadableDataSource<String, List<ParamFlowRule>> paramFlowRleDataSource = new ZookeeperDataSource<>(sentinelProperties.getZookeeper().getAddress(),
                    String.format(PARAM_FLOW_RULE_PATH, AppNameUtil.getAppName()),
                    source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {}));
            ParamFlowRuleManager.register2Property(paramFlowRleDataSource.getProperty());

            ReadableDataSource<String, List<SystemRule>> systemRuleDataSource = new ZookeeperDataSource<>(sentinelProperties.getZookeeper().getAddress(),
                    String.format(SYSTEM_RULE_PATH, AppNameUtil.getAppName()),
                    source -> JSON.parseObject(source, new TypeReference<List<SystemRule>>() {}));
            SystemRuleManager.register2Property(systemRuleDataSource.getProperty());
        }
    }

    /**
     * nacos的datasource注册
     */
    @Configuration
    @ConditionalOnProperty(name = "sentinel.nacos.enable", havingValue = "true")
    public class NacosDataSourceConfiguration implements InitializingBean {

        public static final String FLOW_RULE_PATH = "%s_flow";
        public static final String AUTHORITY_RULE_PATH = "%s_authority";
        public static final String DEGRADE_RULE_PATH = "%s_degrade";
        public static final String PARAM_FLOW_RULE_PATH = "%s_param";
        public static final String SYSTEM_RULE_PATH = "%s_system";
        public static final String DEFAULT_GROUP = "SENTINEL_GROUP";

        @Override
        public void afterPropertiesSet() throws Exception {
            // prop
            String groupId = Optional.ofNullable(sentinelProperties.getNacos().getGroupId()).orElse(DEFAULT_GROUP);
            String addr = sentinelProperties.getNacos().getServerAddr();

            ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(
                    addr,
                    groupId,
                    String.format(FLOW_RULE_PATH, AppNameUtil.getAppName()),
                    source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
            FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

            ReadableDataSource<String, List<AuthorityRule>> authorityRleDataSource = new NacosDataSource<>(
                    addr,
                    groupId,
                    String.format(AUTHORITY_RULE_PATH, AppNameUtil.getAppName()),
                    source -> JSON.parseObject(source, new TypeReference<List<AuthorityRule>>() {}));
            AuthorityRuleManager.register2Property(authorityRleDataSource.getProperty());

            ReadableDataSource<String, List<DegradeRule>> degradeRleDataSource = new NacosDataSource<>(
                    addr,
                    groupId,
                    String.format(DEGRADE_RULE_PATH, AppNameUtil.getAppName()),
                    source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {}));
            DegradeRuleManager.register2Property(degradeRleDataSource.getProperty());

            ReadableDataSource<String, List<ParamFlowRule>> paramFlowRleDataSource = new NacosDataSource<>(
                    addr,
                    groupId,
                    String.format(PARAM_FLOW_RULE_PATH, AppNameUtil.getAppName()),
                    source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {}));
            ParamFlowRuleManager.register2Property(paramFlowRleDataSource.getProperty());

            ReadableDataSource<String, List<SystemRule>> systemRuleDataSource = new NacosDataSource<>(
                    addr,
                    groupId,
                    String.format(SYSTEM_RULE_PATH, AppNameUtil.getAppName()),
                    source -> JSON.parseObject(source, new TypeReference<List<SystemRule>>() {}));
            SystemRuleManager.register2Property(systemRuleDataSource.getProperty());
            log.info("loading complete nacos dynamic config");
        }
    }

    /**
     * web过滤器配置
     */
    @Configuration
    @ConditionalOnBean(DispatcherServletAutoConfiguration.class)
    public class SentinelWebFilterConfiguration {

        @Bean
        public SentinelRequestMappingAspect getSentinelRequestMappingAspect() {
            return new SentinelRequestMappingAspect();
        }
    }
}

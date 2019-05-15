package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.config.DashboardProperties;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleNacosProvider;
import com.alibaba.nacos.api.config.ConfigService;

import java.util.Optional;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-22
 */
public class DegradeRuleNacosProvider implements DynamicRuleNacosProvider<DegradeRuleEntity> {

    public static final String DEFAULT_FORMAT = "%s_degrade";
    public static final String DEFAULT_GROUP = "SENTINEL_GROUP";

    /**
     * 配置服务
     */
    private ConfigService configService;

    /**
     * 配置对象
     */
    private DashboardProperties.NacosProperties nacosProperties;

    public DegradeRuleNacosProvider(ConfigService configService, DashboardProperties dashboardProperties) {
        this.configService = configService;
        this.nacosProperties = dashboardProperties.getNacos();
    }

    @Override
    public Class<DegradeRuleEntity> getTClass() {
        return DegradeRuleEntity.class;
    }

    @Override
    public ConfigService getConfigService() {
        return configService;
    }

    @Override
    public String getPathFormat() {
        return DEFAULT_FORMAT;
    }

    @Override
    public String getGroupId() {
        return Optional.ofNullable(nacosProperties.getGroupId()).orElse(DEFAULT_GROUP);
    }
}

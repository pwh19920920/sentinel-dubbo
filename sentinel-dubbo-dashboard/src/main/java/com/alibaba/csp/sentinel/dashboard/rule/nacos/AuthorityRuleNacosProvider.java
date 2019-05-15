package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.config.DashboardProperties;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleNacosProvider;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;

import java.util.Optional;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-22
 */
public class AuthorityRuleNacosProvider implements DynamicRuleNacosProvider<AuthorityRuleEntity> {

    public static final String DEFAULT_FORMAT = "%s_authority";
    public static final String DEFAULT_GROUP = "SENTINEL_GROUP";

    /**
     * 配置服务
     */
    private ConfigService configService;

    /**
     * 配置对象
     */
    private DashboardProperties.NacosProperties nacosProperties;

    public AuthorityRuleNacosProvider(ConfigService configService, DashboardProperties dashboardProperties) {
        this.configService = configService;
        this.nacosProperties = dashboardProperties.getNacos();
    }

    public AuthorityRuleNacosProvider(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public Class<AuthorityRuleEntity> getTClass() {
        return AuthorityRuleEntity.class;
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

    public static void main(String[] args) {
        String json = "{\"app\":\"xmutca-incubator-example-consumer\",\"gmtModified\":1556381537898,\"timeWindow\":10,\"port\":8789,\"resource\":\"/test\",\"grade\":1,\"ip\":\"192.168.199.219\",\"count\":0.1,\"id\":1,\"gmtCreate\":1556381537898,\"limitApp\":\"default\"}";
        System.out.println(JSON.parseObject(json, DegradeRule.class)
        );
    }
}

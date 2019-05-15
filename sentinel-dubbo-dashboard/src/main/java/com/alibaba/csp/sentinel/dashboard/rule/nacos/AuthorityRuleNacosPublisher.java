/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.config.DashboardProperties;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleNacosPublisher;
import com.alibaba.nacos.api.config.ConfigService;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
public class AuthorityRuleNacosPublisher implements DynamicRuleNacosPublisher<AuthorityRuleEntity> {

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

    public AuthorityRuleNacosPublisher(ConfigService configService, DashboardProperties dashboardProperties) {
        this.configService = configService;
        this.nacosProperties = dashboardProperties.getNacos();
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

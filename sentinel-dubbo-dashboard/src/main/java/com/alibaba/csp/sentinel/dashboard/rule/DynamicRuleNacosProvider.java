package com.alibaba.csp.sentinel.dashboard.rule;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;

import java.util.Collections;
import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-27
 */
public interface DynamicRuleNacosProvider<T extends RuleEntity> extends DynamicRuleProvider<T> {

    /**
     * 获取反射
     * @return
     */
    Class<T> getTClass();

    /**
     * 获取规则解码
     * @return
     */
    default Converter<String, List<T>> getRuleEntityDecoders() {
        return s -> JSON.parseArray(s, getTClass());
    }

    /**
     * 获取配置服务
     * @return
     */
    ConfigService getConfigService();

    /**
     * 地址格式
     * @return
     */
    String getPathFormat();

    /**
     * 获取分组
     * @return
     */
    String getGroupId();

    /**
     * 获取规则
     * @param appName
     * @return
     * @throws Exception
     */
    @Override
    default List<T> getRules(String appName) throws Exception {
        // 不正常访问
        if (StringUtil.isBlank(appName)) {
            return Collections.emptyList();
        }

        // 从nacos读取规则
        String reallyPath = String.format(getPathFormat(), appName);
        String rules = getConfigService().getConfig(reallyPath, getGroupId(), 3000);
        if (StringUtil.isEmpty(rules)) {
            return Collections.emptyList();
        }

        // 转换真实对象
        return getRuleEntityDecoders().convert(rules);
    }
}

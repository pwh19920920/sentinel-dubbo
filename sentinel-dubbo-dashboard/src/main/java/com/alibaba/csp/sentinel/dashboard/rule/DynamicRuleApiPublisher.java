package com.alibaba.csp.sentinel.dashboard.rule;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;

import java.util.List;
import java.util.Set;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-27
 */
public interface DynamicRuleApiPublisher<T extends RuleEntity> extends DynamicRulePublisher<T> {

    /**
     * 获取客户端管理
     *
     * @return
     */
    AppManagement getAppManagement();

    /**
     * 具体任务
     * @param appName
     * @param ip
     * @param port
     * @return
     */
    boolean execute(String appName, String ip, int port, List<T> rules);

    /**
     * 发布规则
     * @param app app name
     * @param rules list of rules to push
     * @throws Exception
     */
    @Override
    default void publish(String app, List<T> rules) throws Exception {
        // 判空防止错误
        if (StringUtil.isBlank(app) || rules == null || rules.isEmpty()) {
            return;
        }

        // 获取所有的客户端
        Set<MachineInfo> set = getAppManagement().getDetailApp(app).getMachines();
        for (MachineInfo machine : set) {
            if (!machine.isHealthy()) {
                continue;
            }
            execute(app, machine.getIp(), machine.getPort(), rules);
        }
    }
}

package com.alibaba.csp.sentinel.dashboard.rule;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.util.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-27
 */
public interface DynamicRuleApiProvider<T extends RuleEntity> extends DynamicRuleProvider<T> {

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
    List<T> execute(String appName, String ip, int port);

    /**
     * 获取规则列表
     * @param appName
     * @return
     * @throws Exception
     */
    @Override
    default List<T> getRules(String appName) throws Exception {
        // 判空
        if (StringUtil.isBlank(appName)) {
            return Collections.emptyList();
        }

        // 筛选正常的服务
        List<MachineInfo> list = getAppManagement().getDetailApp(appName).getMachines()
                .stream()
                .filter(MachineInfo::isHealthy)
                .sorted((e1, e2) -> Long.compare(e2.getLastHeartbeat(), e1.getLastHeartbeat())).collect(Collectors.toList());
        if (list.isEmpty()) {
            return Collections.emptyList();
        }

        MachineInfo machine = list.get(0);
        return execute(machine.getApp(), machine.getIp(), machine.getPort());
    }
}

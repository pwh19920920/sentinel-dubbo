package com.xmutca.sentinel.dubbo.example.consumer.controller;

import com.alibaba.fastjson.JSON;
import com.xmutca.sentinel.dubbo.core.result.Result;
import com.xmutca.sentinel.dubbo.example.consumer.service.StudentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-20
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class StudentController {

    @NonNull
    private StudentService studentService;

    /**
     * sayHello入口
     * @param name
     * @return
     */
    @RequestMapping("/sayHello")
    public Result<String> sayHello(String name) {
        Result<String> result = studentService.sayHello(name);
        System.out.println(JSON.toJSONString(result));
        return result;
    }
}

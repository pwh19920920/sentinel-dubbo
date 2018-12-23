package com.xmutca.sentinel.dubbo.core.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

/**
 * 返回结果对象
 *
 * @author: 彭伟煌(pengweihuang @ xmutca.com)
 * @create: 2016-07-08 22:52
 */
public class Result<T> implements Serializable {

    public enum Status {

        /**
         * 业务处理成功
         */
        SUCCESS(200),

        /**
         * 页面重定向
         */
        REDIRECT(302),

        /**
         * 错误请求
         */
        BAD_REQUEST(400),

        /**
         * 请进行登陆
         */
        UNAUTHORIZED(401),

        /**
         * 权限代码
         */
        FORBIDDEN(403),

        /**
         * 页面不存在
         */
        NOT_FOUND(404),

        /**
         * 无法满足条件的，认定为攻击
         */
        NOT_ACCEPTABLE(406),

        /**
         * 请求超时
         */
        REQUEST_TIMEOUT(408),

        /**
         * 业务访问失败
         */
        ERROR(500),

        /**
         * 网关超时
         */
        GATEWAY_TIMEOUT(504);

        private int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String toString() {
            return String.valueOf(getCode());
        }

        public static Status getInstance(int code) {
            Optional<Status> op = Arrays.asList(Status.values()).parallelStream().filter(currentStatus -> currentStatus.code == code).findFirst();
            if (op.isPresent()) {
                return op.get();
            }
            return null;
        }
    }

    /**
     * 消息
     */
    @Getter
    @Setter
    private String message;

    /**
     * 状态
     */
    private Status status = Status.SUCCESS;

    /**
     * 时间戳
     */
    @Getter
    @Setter
    private Long timestamp = System.currentTimeMillis();

    /**
     * 数据
     */
    @Getter
    @Setter
    private T result;

    /**
     * 扩展信息
     */
    @Getter
    @Setter
    private Object extra;

    public Result() {

    }

    public Result(Status status) {
        this.status = status;
    }

    public Result(T result) {
        this.result = result;
    }

    public Result(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Result(Status status, T result) {
        this.status = status;
        this.result = result;
    }

    public Result(String message, T result) {
        this.result = result;
        this.message = message;
    }

    public Result(Status status, String message, T result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public int getStatus() {
        return status.getCode();
    }

    public void setStatus(int status) {
        this.status = Status.getInstance(status);
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
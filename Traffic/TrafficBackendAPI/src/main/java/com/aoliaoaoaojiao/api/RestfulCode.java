package com.aoliaoaoaojiao.api;

/**
 * 操作代码枚举
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/29
 */
public enum RestfulCode {
    /**
     * 成功
     */
    SUCCESS(200,"操作成功"),

    /**
     * 被禁止
     */
    Forbidden(403,"授权错误"),

    /**
     * 没有资源
     */
    NOTFOUND(404,"参数错误"),
    /**
     * 失败
     */
    FAILED(500, "操作失败");

    private int code;
    private String msg;

    RestfulCode(int  code, String msg) {
        this.code=code;
        this.msg=msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return msg;
    }
}

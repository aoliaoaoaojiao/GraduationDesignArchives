package com.aoliaoaoaojiao.api;

/**
 * 通用返回模板
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/29
 */
public class CommonRestful<T> {
    /**
     * 状态代码
     */
    private int code;
    /**
     * 信息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;

    public CommonRestful(){};

    private CommonRestful(int code, String msg, T data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

    /**
     * 成功返回
     *
     * @param data 数据
     * @return {@link CommonRestful<T>}
     */
    public static <T> CommonRestful<T> success(T data){
        return new CommonRestful<T>(RestfulCode.SUCCESS.getCode(),RestfulCode.SUCCESS.getMessage(),data);
    }

    /**
     * 参数错误返回
     *
     * @return {@link CommonRestful<T>}
     */
    public static <T> CommonRestful<T> notFound(T errorMess){
        return new CommonRestful<T>(RestfulCode.NOTFOUND.getCode(),RestfulCode.NOTFOUND.getMessage(),errorMess);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

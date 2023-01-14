package com.aoliaoaoaojiao.exception;

/**
 * 参数值异常
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/30
 */
public class ParamIsValueException extends RuntimeException{
    private final String startTimeParameter;

    private final String endTimeParameter;

    private String mess;

    public ParamIsValueException(String  startTimeParameter, String endTimeParameter,String mess) {
        super("");
        this.startTimeParameter = startTimeParameter;
        this.endTimeParameter = endTimeParameter;
        this.mess = mess+"  startTime:"+startTimeParameter+"    endTime:"+endTimeParameter;
    }

    public String getStartTimeParameter() {
        return startTimeParameter;
    }

    public String getEndTimeParameter() {
        return endTimeParameter;
    }

    public String getMess() {
        return mess;
    }
}

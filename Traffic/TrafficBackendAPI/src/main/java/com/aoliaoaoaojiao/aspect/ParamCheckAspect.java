package com.aoliaoaoaojiao.aspect;

import com.aoliaoaoaojiao.exception.ParamIsValueException;
import com.aoliaoaoaojiao.util.TimeTool;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数检查切面
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/30
 */
@Component
@Aspect
public class ParamCheckAspect {

    @Autowired
    TimeTool timeTool;

    @Pointcut("execution(public * com.aoliaoaoaojiao.controller..*.*(..)) ")
    public void pointcut(){}

    /**
     * 检查参数
     *
     * @param joinPoint 连接点
     * @return {@link Object}* @throws Throwable throwable
     */
    @Around("pointcut()")
    public Object checkParameter(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ModelMap map ;
        //获取方法参数名
        String[] paramNames = signature.getParameterNames();
        //获取参数值
        Object[] paramValues = joinPoint.getArgs();
        Map<String, Long> paramMap = new HashMap<>();
        for (int i=0;i<paramNames.length;i++)
        {
            if ("startTime".equals(paramNames[i]) || "endTime".equals(paramNames[i])){
                paramMap.put(paramNames[i], Long.valueOf(paramValues[i].toString()));
            }
        }
        if (!paramMap.keySet().isEmpty()) {
            checkTimeToParam(paramMap);
        }
        return joinPoint.proceed();
    }


    /**
     * 检查时间参数
     *
     * @param paramMap 参数映射
     */
    private void checkTimeToParam(Map paramMap){
        Long startTime = (Long)paramMap.get("startTime");
        Long endTime = (Long)paramMap.get("endTime");
        if (startTime <= endTime){
            //时间超出七天
            if(startTime<timeTool.sevenDaysStartTime() ){
                throw new ParamIsValueException(startTime.toString(),endTime.toString(),"超出七天，范围不允许");
            }if(endTime>timeTool.getNowTime()){
                throw new ParamIsValueException(startTime.toString(),endTime.toString(),"结束时间定义错误");
            }
        }else {
            throw new ParamIsValueException(startTime.toString(),endTime.toString(),"开始与结束时间定义错误");
        }

    }

}

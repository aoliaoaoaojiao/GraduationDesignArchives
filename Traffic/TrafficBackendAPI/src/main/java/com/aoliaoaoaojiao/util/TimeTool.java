package com.aoliaoaoaojiao.util;

import org.springframework.stereotype.Component;

/**
 * 时间变换相关操作
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/30
 */
@Component
public class TimeTool {
    /**
     * 获取最近一周开始时间
     *
     * @return {@link Long}
     */
    public Long sevenDaysStartTime(){
        long currentTimestamp = System.currentTimeMillis();
        return currentTimestamp-1000*60*60*24*7;
    }
    public Long getNowTime(){
        return System.currentTimeMillis();
    }
}

package com.aoliaoaoaojiao.service;

import com.aoliaoaoaojiao.entity.DiffDirectionStatusCount;
import com.aoliaoaoaojiao.entity.TheWholeCityScore;

import java.util.List;

/**
 * 城市交通历史服务
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/04
 */
public interface TrafficHistoryCityService {
    /**
     * 得到历史城市交通状况
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link TheWholeCityScore}
     */
    List<TheWholeCityScore> getHistoryCityScore(Long startTime, Long endTime);




    /**
     * 获得历史的城市不同状况数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param status    状态
     * @return {@link List<DiffDirectionStatusCount>}
     */
    List<DiffDirectionStatusCount> getHistoryCityStatusCount(String status,Long startTime, Long endTime);

}

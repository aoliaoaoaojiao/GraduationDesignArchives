package com.aoliaoaoaojiao.service;

import com.aoliaoaoaojiao.entity.EveryRegionComplexData;
import com.aoliaoaoaojiao.entity.EveryRegionStatusCount;

import java.util.List;

/**
 * 地区历史交通服务
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/04
 */
public interface TrafficHistoryRegionService {
    /**
     * 得到历史地区得分
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param adCode    地区代码
      * @return {@link List <EveryRegionComplexData>}
     */
    List<EveryRegionComplexData> getHistoryRegionScore(String adCode,Long startTime, Long endTime);

    /**
     * 让历史地区速度
     * 得到历史地区速度
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param adCode    地区代码
     * @return {@link List<EveryRegionComplexData>}
     */
    List<EveryRegionComplexData> getHistoryRegionSpeed(String adCode,Long startTime, Long endTime);


    /**
     * 得到历史区域不同的状态数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<EveryRegionStatusCount>}
     */
    List<EveryRegionStatusCount> getHistoryRegionStatusCount(String adCode,Long startTime, Long endTime);


}

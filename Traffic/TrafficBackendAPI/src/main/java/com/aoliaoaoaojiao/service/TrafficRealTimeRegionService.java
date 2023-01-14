package com.aoliaoaoaojiao.service;

import com.aoliaoaoaojiao.entity.EveryRegionComplexData;
import com.aoliaoaoaojiao.entity.EveryRegionStatusCount;

import java.util.List;

/**
 * 实时区域交通服务层
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/03
 */
public interface TrafficRealTimeRegionService {
    /**
     * 得到实时地区得分
     *
     * @return {@link List <EveryRegionComplexData>}
     */
    List<EveryRegionComplexData> getRealTimeRegionScore();

    /**
     * 得到实时地区速度
     *
     * @return {@link List<EveryRegionComplexData>}
     */
    List<EveryRegionComplexData> getRealTimeRegionSpeed();


    /**
     * 得到实时区域不同的状态数量
     *
     * @return {@link List<EveryRegionStatusCount>}
     */
    List<EveryRegionStatusCount> getRealTimeRegionStatusCount();

}

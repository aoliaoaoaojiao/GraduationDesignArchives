package com.aoliaoaoaojiao.service;

import com.aoliaoaoaojiao.entity.DiffDirectionStatusCount;
import com.aoliaoaoaojiao.entity.TheWholeCityScore;

/**
 * 城市实时交通服务层
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/03
 */
public interface TrafficRealTimeCityService {
    /**
     * 得到实时城市交通状况
     *
     * @return {@link TheWholeCityScore}
     */
    TheWholeCityScore getRealTimeCityScore();

    /**
     * 获得实时的城市状况
     *
     * @return {@link TheWholeCityScore}
     */
    TheWholeCityScore getRealTimeCityStatus();


    /**
     * 获得实时的城市不同状况数量
     *
     * @param status 状态
     * @return {@link DiffDirectionStatusCount}
     */
    DiffDirectionStatusCount getRealTimeCityStatusCount(String status);
}

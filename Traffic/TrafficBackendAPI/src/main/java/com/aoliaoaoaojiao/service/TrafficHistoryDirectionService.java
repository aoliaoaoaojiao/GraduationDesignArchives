package com.aoliaoaoaojiao.service;

import com.aoliaoaoaojiao.entity.DiffDirectionScore;
import com.aoliaoaoaojiao.entity.DiffDirectionStatusCount;

import java.util.List;

/**
 * 历史交通方向服务
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/04
 */
public interface TrafficHistoryDirectionService {
    /**
     * 得到历史方向得分
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List < DiffDirectionScore >}
     */
    List<DiffDirectionScore> getHistoryDirectionScore(String direction,Long startTime, Long endTime);


    /**
     * 得到历史方向平均速度
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<DiffDirectionScore>}
     */
    List<DiffDirectionScore> getHistoryDirectionSpeed(String direction,Long startTime, Long endTime);

    /**
     * 得到历史方向状态数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param direction 方向
     * @return {@link  List<DiffDirectionStatusCount>}
     */
    List<DiffDirectionStatusCount> getHistoryDirectionStatusCount(String direction, Long startTime, Long endTime);
}

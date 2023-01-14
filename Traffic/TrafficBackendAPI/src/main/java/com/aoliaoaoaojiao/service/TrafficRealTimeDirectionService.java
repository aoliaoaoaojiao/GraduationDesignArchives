package com.aoliaoaoaojiao.service;

import com.aoliaoaoaojiao.entity.DiffDirectionScore;
import com.aoliaoaoaojiao.entity.DiffDirectionStatusCount;

import java.util.List;

/**
 * 实时交通方向服务层
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/03
 */
public interface TrafficRealTimeDirectionService {
    /**
     * 得到实时方向得分
     *
     * @return {@link List <DiffDirectionScore>}
     */
    List<DiffDirectionScore> getRealTimeDirectionScore();

    /**
     * 得到实时方向平均速度
     *
     * @return {@link List<DiffDirectionScore>}
     */
    List<DiffDirectionScore> getRealTimeDirectionSpeed();


    /**
     * 得到实时方向状态数
     *
     * @param direction 方向
     * @return {@link  List<DiffDirectionStatusCount>}
     */
    List<DiffDirectionStatusCount> getRealTimeDirectionStatusCount(String direction);
}

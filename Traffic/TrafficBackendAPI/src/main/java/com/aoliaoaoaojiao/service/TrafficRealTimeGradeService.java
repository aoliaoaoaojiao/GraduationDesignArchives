package com.aoliaoaoaojiao.service;

import com.aoliaoaoaojiao.entity.DiffGradeRoadOverall;
import com.aoliaoaoaojiao.entity.DiffGradeStatusCount;

import java.util.List;

/**
 * 实时道路状态服务层
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/28
 */
public interface TrafficRealTimeGradeService {

    /**
     * 获得实时的不同等级道路速度
     *
     * @return {@link List<DiffGradeRoadOverall>}
     */
    List<DiffGradeRoadOverall> getRealTimeGradeSpeed();

    /**
     * 实时获得不同等级道路得分
     *
     * @return {@link List<DiffGradeRoadOverall>}
     */
    List<DiffGradeRoadOverall> getRealTimeGradeScore();


    /**
     * 实时获得年级状态数
     * 得到实时不同等级道路不同状态数量
     *
     * @param grade   等级
     * @return {@link List<DiffGradeStatusCount>}
     */
    List<DiffGradeStatusCount> getRealTimeGradeStatusCount(String grade);
}

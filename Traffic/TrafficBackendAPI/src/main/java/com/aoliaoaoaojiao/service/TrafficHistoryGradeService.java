package com.aoliaoaoaojiao.service;

import com.aoliaoaoaojiao.entity.DiffGradeRoadOverall;
import com.aoliaoaoaojiao.entity.DiffGradeStatusCount;

import java.util.List;

/**
 * 历史数据服务层
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/29
 */
public interface TrafficHistoryGradeService {

    /**
     * 得到历史级速度
     * 获得历史的不同等级道路速度
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param grade     等级
     * @return {@link List<DiffGradeRoadOverall>}
     */
    List<DiffGradeRoadOverall> getHistoryGradeSpeed(String grade,Long startTime, Long endTime);

    /**
     * 得到历史成绩分数
     * 历史获得不同等级道路得分
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param grade     等级
     * @return {@link List<DiffGradeRoadOverall>}
     */
    List<DiffGradeRoadOverall> getHistoryGradeScore(String grade,Long startTime, Long endTime);


    /**
     * 得到历史不同等级道路不同状态数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param grade     年级
     * @return {@link List<DiffGradeStatusCount>}
     */
    List<DiffGradeStatusCount> getHistoryGradeStatusCount(String grade, Long startTime, Long endTime);
}

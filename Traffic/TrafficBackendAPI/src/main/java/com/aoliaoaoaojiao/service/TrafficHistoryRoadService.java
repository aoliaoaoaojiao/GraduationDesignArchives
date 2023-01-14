package com.aoliaoaoaojiao.service;

import com.aoliaoaoaojiao.entity.DiffRoadStatus;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 交通历史道路服务
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/04
 */
public interface TrafficHistoryRoadService {

    /**
     * 道路历史状态
     *
     * @param name      名字
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link PageInfo }
     */
    PageInfo getHistoryDiffRoadStatus(String name, Long startTime, Long endTime, int pageNum, int pageSize);

    /**
     * 历史道路tpi
     *
     * @param name      名字
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<DiffRoadStatus>}
     */
    List<DiffRoadStatus> getHistoryDiffRoadTpi(String name, Long startTime, Long endTime);

    /**
     * 历史道路dtp
     *
     * @param name      名字
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<DiffRoadStatus>}
     */
    List<DiffRoadStatus> getHistoryDiffRoadDtp(String name, Long startTime, Long endTime);

    /**
     * 历史道路速度
     *
     * @param name      名字
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<DiffRoadStatus>}
     */
    List<DiffRoadStatus> getHistoryDiffRoadSpeed(String name, Long startTime, Long endTime);
}

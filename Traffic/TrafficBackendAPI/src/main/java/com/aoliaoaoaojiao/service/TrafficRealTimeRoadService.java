package com.aoliaoaoaojiao.service;

import com.aoliaoaoaojiao.api.RestfulHeatMap;
import com.aoliaoaoaojiao.entity.DiffRoadStatus;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 实时道路交通服务层
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/03
 */
public interface TrafficRealTimeRoadService {
    /**
     * 得到热力图数据
     *
     * @return {@link List <RestfulHeatMap>}
     */
    List<RestfulHeatMap> getAllRealTimeRoadStatus();

    /**
     * 得到实时所有道路状况
     *
     * @param pageNum  页面数量
     * @param pageSize 页面大小
     * @return {@link PageInfo}
     */
    PageInfo getRealTimeSingleRoadStatus(int pageNum, int pageSize);

    /**
     * 得到实时道路状况
     *
     * @param name  道路名
     * @return {@link List<DiffRoadStatus>}
     */
    List<DiffRoadStatus> getRealTimeSingleRoadStatus(String name);

}

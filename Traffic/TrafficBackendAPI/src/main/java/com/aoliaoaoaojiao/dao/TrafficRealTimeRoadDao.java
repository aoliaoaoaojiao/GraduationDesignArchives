package com.aoliaoaoaojiao.dao;


import com.aoliaoaoaojiao.entity.DiffRoadStatus;
import com.aoliaoaoaojiao.entity.RealtimeSectionStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 实时道路交通
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/04
 */
@Mapper
public interface TrafficRealTimeRoadDao {
    /**
     * 得到热力图数据
     *
     * @return {@link List <RealtimeSectionStatus>}
     */
    @Select("select polyline,TPI from realtimesection a,road_polyline b where  `timestamp` = (SELECT MAX(`timestamp`) FROM diffroadleveltop) and a.name=b.name and a.specific_information = b.specific_information")
    List<RealtimeSectionStatus> getAllRealTimeHeatMapData();

    /**
     * 得到实时所有道路(非路段)状况
     *
     * @return {@link List<DiffRoadStatus>}
     */
    @Select("select name,speedAVG,DTP,TPI,roadStatus from diffroadstatus where `timestamp` = (SELECT MAX(`timestamp`) FROM diffroadleveltop) order by tpi DESC")
    List<DiffRoadStatus> getRealTimeDiffRoadStatus();

    /**
     * 得到实时道路(非路段)状况
     *
     * @return {@link List<DiffRoadStatus>}
     */
    @Select("select name,speedAVG,DTP,TPI,roadStatus from diffroadstatus where name=#{name} and `timestamp` = (SELECT MAX(`timestamp`) FROM diffroadleveltop)")
    List<DiffRoadStatus> getRealTimeRoadStatus(String name);
}

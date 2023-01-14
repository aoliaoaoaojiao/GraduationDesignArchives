package com.aoliaoaoaojiao.dao;

import com.aoliaoaoaojiao.entity.DiffRoadStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 道路历史交通
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/04
 */
@Mapper
public interface TrafficHistoryRoadDao {

    /**
     * 得到历史道路(非路段)状况
     *
     * @param name      名字
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List <DiffRoadStatus>}
     */
    @Select("select name,roadStatus,speedAvg,tpi,dtp,`timestamp` from diffroadstatus where name=#{name} and `timestamp` between #{startTime} and #{endTime} ORDER BY `timestamp` asc")
    List<DiffRoadStatus> getHistoryDiffRoadStatus(String name,Long startTime, Long endTime);


    /**
     * 得到历史道路(非路段)tpi
     *
     * @param name      名字
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<DiffRoadStatus>}
     */
    @Select("select tpi,`timestamp` from diffroadstatus where name=#{name} and `timestamp` between #{startTime} and #{endTime} ORDER BY `timestamp` asc")
    List<DiffRoadStatus> getHistoryDiffRoadTpi(String name, Long startTime, Long endTime);

    /**
     * 得到历史道路(非路段)dtp
     *
     * @param name      名字
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<DiffRoadStatus>}
     */
    @Select("select dtp,`timestamp` from diffroadstatus where name=#{name} and `timestamp` between #{startTime} and #{endTime} ORDER BY `timestamp` asc")
    List<DiffRoadStatus> getHistoryDiffRoadDtp(String name, Long startTime, Long endTime);

    /**
     * 得到历史道路(非路段)平均速度
     *
     * @param name      名字
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<DiffRoadStatus>}
     */
    @Select("select speedAvg,`timestamp` from diffroadstatus where name=#{name} and `timestamp` between #{startTime} and #{endTime} ORDER BY `timestamp` asc")
    List<DiffRoadStatus> getHistoryDiffRoadSpeed(String name, Long startTime, Long endTime);

}

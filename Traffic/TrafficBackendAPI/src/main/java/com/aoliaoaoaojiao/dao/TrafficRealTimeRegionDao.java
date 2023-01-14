package com.aoliaoaoaojiao.dao;

import com.aoliaoaoaojiao.entity.EveryRegionComplexData;
import com.aoliaoaoaojiao.entity.EveryRegionStatusCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TrafficRealTimeRegionDao {
    /**
     * 得到实时地区得分
     *
     * @return {@link List <EveryRegionComplexData>}
     */
    @Select("select adcode,areaScore from everyregioncomplexdata where   `timestamp` = (SELECT MAX(`timestamp`) FROM diffroadleveltop)")
    List<EveryRegionComplexData> getRealTimeRegionScore();

    /**
     * 得到实时地区平均交通速度
     *
     * @return {@link List<EveryRegionComplexData>}
     */
    @Select("select adcode,areaSpeedAVG from everyregioncomplexdata where   `timestamp` = (SELECT MAX(`timestamp`) FROM diffroadleveltop)")
    List<EveryRegionComplexData> getRealTimeRegionSpeed();


    /**
     * 得到实时区域状态数
     *
     * @return {@link List<EveryRegionStatusCount>}
     */
    @Select("select adCode,status,countStatus from everyregionstatuscount  where   `timestamp` = (SELECT MAX(`timestamp`) FROM diffroadleveltop) order by adCode")
    List<EveryRegionStatusCount> getRealTimeRegionStatusCount();

}

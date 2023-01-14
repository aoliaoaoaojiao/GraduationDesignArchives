package com.aoliaoaoaojiao.dao;

import com.aoliaoaoaojiao.entity.DiffDirectionStatusCount;
import com.aoliaoaoaojiao.entity.TheWholeCityScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TrafficRealTimeCityDao {
    /**
     * 获得实时的城市平均分数
     *
     * @return {@link TheWholeCityScore}
     */
    @Select("SELECT scoreAvg FROM thewholecityscore WHERE `timestamp` = (SELECT MAX(`timestamp`) FROM diffroadleveltop)")
    TheWholeCityScore getRealTimeCityScore();

    /**
     * 获得实时的城市交通状况
     *
     * @return {@link TheWholeCityScore}
     */
    @Select("SELECT overallSituation FROM thewholecityscore WHERE `timestamp` = (SELECT MAX(`timestamp`) FROM diffroadleveltop)")
    TheWholeCityScore getRealTimeCityStatus();

    /**
     * 得到实时的城市不同状态数量
     *
     * @param status 状态
     * @return {@link TheWholeCityScore}
     */
    @Select("select status,sum(countStatus)as countStatus from diffdirectionstatuscount  WHERE `timestamp` = (SELECT MAX(`timestamp`) FROM diffroadleveltop) and`status`=#{status}")
    DiffDirectionStatusCount getRealTimeCityStatusCount(String status);

}

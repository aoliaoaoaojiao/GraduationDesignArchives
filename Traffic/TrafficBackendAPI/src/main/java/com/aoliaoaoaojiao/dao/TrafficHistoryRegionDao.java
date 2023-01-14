package com.aoliaoaoaojiao.dao;

import com.aoliaoaoaojiao.entity.EveryRegionComplexData;
import com.aoliaoaoaojiao.entity.EveryRegionStatusCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 历史地区交通数据
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/09
 */
@Mapper
public interface TrafficHistoryRegionDao {
    /**
     * 得到历史地区的分数
     * 得到地区历史得分
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param adCode    地区代码
     * @return {@link List<EveryRegionComplexData>}
     */
    @Select("select adcode,areaScore,`timestamp` from everyregioncomplexdata where  adcode=#{adCode} and  `timestamp` between #{startTime} and #{endTime} ORDER BY `timestamp` asc")
    List<EveryRegionComplexData> getHistoryRegionScore(String adCode,Long startTime, Long endTime);

    /**
     * 让历史地区速度
     * 得到地区历史平均交通速度
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param adCode    地区代码
     * @return {@link List<EveryRegionComplexData>}
     */
    @Select("select adcode,areaSpeedAVG,`timestamp` from everyregioncomplexdata where  adcode=#{adCode} and `timestamp` between #{startTime} and #{endTime} ORDER BY `timestamp` asc")
    List<EveryRegionComplexData> getHistoryRegionSpeed(String adCode,Long startTime, Long endTime);

    /**
     * 得到区域历史状态数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<EveryRegionStatusCount>}
     */
    @Select("select adCode,status,countStatus,`timestamp` from everyregionstatuscount where `timestamp` between #{startTime} and #{endTime} and adCode=#{adCode} ORDER BY `timestamp` asc")
    List<EveryRegionStatusCount> getHistoryRegionStatusCount(String adCode ,Long startTime, Long endTime);

}

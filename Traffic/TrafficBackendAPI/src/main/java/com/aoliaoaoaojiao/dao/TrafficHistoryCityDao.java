package com.aoliaoaoaojiao.dao;

import com.aoliaoaoaojiao.entity.DiffDirectionStatusCount;
import com.aoliaoaoaojiao.entity.TheWholeCityScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 城市交通历史
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/05
 */
@Mapper
public interface TrafficHistoryCityDao {
    /**
     * 获得城市历史平均分数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List <TheWholeCityScore>}
     */
    @Select("SELECT scoreAvg,`timestamp` FROM thewholecityscore WHERE `timestamp` between #{startTime} and #{endTime} ORDER BY `timestamp` asc")
    List<TheWholeCityScore> getHistoryCityScore(Long startTime, Long endTime);



    /**
     * 城市不同状态历史数量
     *
     * @param status    状态
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<DiffDirectionStatusCount>}
     */
    @Select("select status,sum(countStatus) as countStatus,`timestamp` from diffdirectionstatuscount where  `timestamp` between #{startTime} and #{endTime} and`status`=#{status} Group by `timestamp`  ORDER BY `timestamp` asc")
    List<DiffDirectionStatusCount> getHistoryCityStatusCount(String status,Long startTime, Long endTime);
}

package com.aoliaoaoaojiao.dao;

import com.aoliaoaoaojiao.entity.DiffDirectionScore;
import com.aoliaoaoaojiao.entity.DiffDirectionStatusCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TrafficHistoryDirectionDao {
    /**
     * 得到不同方向历史得分
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List <DiffDirectionScore>}
     */
    @Select("select direction,directionScore,`timestamp` from diffdirectionscore where direction=#{direction}  and    `timestamp` between #{startTime} and #{endTime} ORDER BY `timestamp` asc")
    List<DiffDirectionScore> getHistoryDirectionScore(String direction,Long startTime, Long endTime);

    /**
     * 得到不同方向历史平均速度
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<DiffDirectionScore>}
     */
    @Select("select direction,directionSpeedAvg,`timestamp` from diffdirectionscore where direction=#{direction}  and   `timestamp` between #{startTime} and #{endTime} ORDER BY `timestamp` asc")
    List<DiffDirectionScore> getHistoryDirectionSpeed(String direction,Long startTime, Long endTime);

    /**
     * 得到不同方向不同状态历史数
     *
     * @param direction 方向
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<DiffDirectionStatusCount>}
     */
    @Select("select direction,status,countStatus,`timestamp` from diffdirectionstatuscount where direction=#{direction}  and `timestamp` between #{startTime} and #{endTime} ORDER BY `timestamp` asc")
    List<DiffDirectionStatusCount> getHistoryDirectionStatusCount(String direction, Long startTime, Long endTime);

}

package com.aoliaoaoaojiao.dao;

import com.aoliaoaoaojiao.entity.DiffDirectionScore;
import com.aoliaoaoaojiao.entity.DiffDirectionStatusCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TrafficRealTimeDirectionDao {
    /**
     * 得到实时不同方向得分
     * (由于时间戳是不断增大的，表中最大的时间戳即为实时数据)
     *
     * @return {@link List <DiffDirectionScore>}
     */
    @Select("select direction,directionScore from diffdirectionscore where  `timestamp`= (SELECT MAX(`timestamp`) FROM diffroadleveltop)")
    List<DiffDirectionScore> getRealTimeDirectionScore();

    /**
     * 得到实时不同方向平均速度
     *
     * @return {@link List<DiffDirectionScore>}
     */
    @Select("select direction,directionSpeedAvg from diffdirectionscore where  `timestamp`= (SELECT MAX(`timestamp`) FROM diffroadleveltop)")
    List<DiffDirectionScore> getRealTimeDirectionSpeed();


    /**
     * 得到实时方向不同状态数
     *
     * @param direction 方向
     * @return {@link List<DiffDirectionStatusCount>}
     */
    @Select("select direction,status,countStatus from diffdirectionstatuscount where direction=#{direction} and `timestamp` = (SELECT MAX(`timestamp`) FROM diffroadleveltop)")
    List<DiffDirectionStatusCount> getRealTimeDirectionStatusCount(String direction);
}

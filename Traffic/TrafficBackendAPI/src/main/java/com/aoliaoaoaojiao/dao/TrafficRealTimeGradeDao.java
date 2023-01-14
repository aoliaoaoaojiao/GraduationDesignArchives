package com.aoliaoaoaojiao.dao;

import com.aoliaoaoaojiao.entity.DiffGradeRoadOverall;
import com.aoliaoaoaojiao.entity.DiffGradeStatusCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TrafficRealTimeGradeDao {
    /**
     * 获得不同等级道路实时平均速度
     *
     * @return {@link List <DiffGradeRoadOverall>}
     */
    @Select("select level,diffLevelSpeed from difflevelroadoverall where  `timestamp` = (SELECT MAX(`timestamp`) FROM diffroadleveltop)")
    List<DiffGradeRoadOverall> getRealTimeLevelSpeed();

    /**
     * 获得不同等级道路实时平均综合得分
     *
     * @return {@link List<DiffGradeRoadOverall>}
     */
    @Select("select level,scoreAVG from difflevelroadoverall where  `timestamp` = (SELECT MAX(`timestamp`) FROM diffroadleveltop)")
    List<DiffGradeRoadOverall> getRealTimeLevelScore();


    /**
     * 得到实时不同等级道路各状态数量
     *
     * @param level  等级
     * @return {@link List<DiffGradeStatusCount>}
     */
    @Select("select level,roadStatus,diffLevelCountStatus from difflevelstatuscount where level=#{level} and  `timestamp` = (SELECT MAX(`timestamp`) FROM diffroadleveltop)")
    List<DiffGradeStatusCount> getRealTimeLevelStatusCount(String level);
}

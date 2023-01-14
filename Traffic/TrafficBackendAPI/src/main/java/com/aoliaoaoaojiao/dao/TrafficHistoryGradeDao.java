package com.aoliaoaoaojiao.dao;

import com.aoliaoaoaojiao.entity.DiffGradeRoadOverall;
import com.aoliaoaoaojiao.entity.DiffGradeStatusCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 不同等级道路历史交通数据
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/11
 */
@Mapper
public interface TrafficHistoryGradeDao {
    /**
     * 获得不同等级道路历史平均速度
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param level     等级
     * @return {@link List<DiffGradeRoadOverall>}
     */
    @Select("select level,diffLevelSpeed,`timestamp` from difflevelroadoverall where level=#{level}  and  `timestamp` between #{startTime} and #{endTime} ORDER BY `timestamp` asc")
    List<DiffGradeRoadOverall> getHistoryLevelSpeed(String level,Long startTime, Long endTime);

    /**
     * 获得不同等级道路历史平均综合得分
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param level     等级
     * @return {@link List<DiffGradeRoadOverall>}
     */
    @Select("select level,scoreAVG,`timestamp` from difflevelroadoverall where level=#{level}  and  `timestamp` between #{startTime} and #{endTime} ORDER BY `timestamp` asc")
    List<DiffGradeRoadOverall> getHistoryLevelScore(String level,Long startTime, Long endTime);

    /**
     * 得到不同等级道路历史各状态数量
     *
     * @param level     等级
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<DiffGradeStatusCount>}
     */
    @Select("select level,roadStatus,diffLevelCountStatus,`timestamp` from difflevelstatuscount where level=#{level}  and  `timestamp` between #{startTime} and #{endTime} ORDER BY `timestamp` asc")
    List<DiffGradeStatusCount> getHistoryLevelStatusCount(String level, Long startTime, Long endTime);

}

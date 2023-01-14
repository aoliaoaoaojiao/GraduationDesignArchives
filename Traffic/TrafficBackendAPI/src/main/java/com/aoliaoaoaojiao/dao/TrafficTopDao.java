package com.aoliaoaoaojiao.dao;

import com.aoliaoaoaojiao.entity.RealTimeTop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 求拥堵排行前几的数据
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/27
 */
@Mapper
public interface TrafficTopDao {
    /**
     * 得到城市拥堵排行前几的路段
     *
     * @param num 数量
     * @return {@link List<RealTimeTop>}
     */
    @Select("SELECT name,specific_information,speed,DTP,TPI,status FROM citytop WHERE `timestamp` = (SELECT MAX(`timestamp`) FROM citytop) order by tpi desc limit #{num} ")
    List<RealTimeTop> getCityTop(int num);


    /**
     * 得到各行政区域拥堵前几的数据
     *
     * @param num 数量
     * @return {@link List<RealTimeTop>}
     */
    @Select("select name,specific_information,speed,DTP,TPI,status from diffareatop  WHERE `timestamp` = (SELECT MAX(`timestamp`) FROM diffareatop) and adcode=#{adCode} order by tpi desc limit #{num}")
    List<RealTimeTop> getRegionTop(String adCode, int num);


    /**
     * 各方向拥堵指数前几的数据
     *
     * @param direction 方向
     * @param num       数量
     * @return {@link List<RealTimeTop>}
     */
    @Select("select name,specific_information,speed,DTP,TPI,status from diffdirectiontop WHERE `timestamp` = (SELECT MAX(`timestamp`) FROM diffdirectiontop) and direction=#{direction} order by tpi desc limit #{num}")
    List<RealTimeTop> getDirectionTop(String direction, int num);


    /**
     * 不同等级的道路拥堵排行TOP N的数据
     *
     * @param level 水平
     * @param num   数量
     * @return {@link List<RealTimeTop>}
     */
    @Select("select name,specific_information,speed,DTP,TPI,status from diffroadleveltop  WHERE `timestamp` = (SELECT MAX(`timestamp`) FROM diffroadleveltop) and level=#{level} order by tpi desc limit #{num}")
    List<RealTimeTop> getGradeRoadTop(String level, int num);


}

package com.aoliaoaoaojiao.service;

import com.aoliaoaoaojiao.entity.RealTimeTop;

import java.util.List;

/**
 * top n服务层
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/27
 */

public interface TrafficTopService {
    /**
     * 得到城市top n
     *
     * @param num 数量
     * @return {@link List< RealTimeTop >}
     */
    List<RealTimeTop> getCityTopN(int num);

    /**
     * 获取各区域TOP n
     *
     * @param num    数量
     * @param adCode 地区代码
     * @return {@link List<RealTimeTop>}
     */
    List<RealTimeTop> getRegionTopN(String adCode,int num);

    /**
     * 各方向top n
     *
     * @param num       数量
     * @param direction 方向
     * @return {@link List<RealTimeTop>}
     */
    List<RealTimeTop> getDirectionTopN(String direction,int num);

    /**
     * 得到各等级道路中拥堵排行Top N的数据
     *
     * @param num   数量
     * @param grade 年级
     * @return {@link List<RealTimeTop>}
     */
    List<RealTimeTop> getGradeTopN(String grade,int num);


}

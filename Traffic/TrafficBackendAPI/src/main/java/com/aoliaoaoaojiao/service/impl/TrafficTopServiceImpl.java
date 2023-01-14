package com.aoliaoaoaojiao.service.impl;

import com.aoliaoaoaojiao.dao.TrafficTopDao;
import com.aoliaoaoaojiao.entity.RealTimeTop;
import com.aoliaoaoaojiao.service.TrafficTopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * top service实现
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/28
 */
@Service
public class TrafficTopServiceImpl implements TrafficTopService {
    @Resource
    TrafficTopDao trafficTopDao;

    /**
     * 得到城市top n
     *
     * @param num 数量
     * @return {@link List<RealTimeTop>}
     */
    @Override
    public List<RealTimeTop> getCityTopN(int num){
        return trafficTopDao.getCityTop(num);
    }

    /**
     * 得到区域top n
     *
     * @param num 数量
     * @return {@link Map}
     */
    @Override
    public List<RealTimeTop> getRegionTopN(String adCode,int num) {
        return trafficTopDao.getRegionTop(adCode,num);
    }

    /**
     * 得到方向top n
     *
     * @param num 数量
     * @return {@link Map}
     */
    @Override
    public List<RealTimeTop> getDirectionTopN(String direction,int num) {
        return trafficTopDao.getDirectionTop(direction,num);
    }

    /**
     * 得到不同等级top n
     *
     * @param num 数量
     * @return {@link Map}
     */
    @Override
    public List<RealTimeTop> getGradeTopN(String grade,int num) {
        return trafficTopDao.getGradeRoadTop(grade,num);
    }
}

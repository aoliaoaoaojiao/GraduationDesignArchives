package com.aoliaoaoaojiao.service.impl;

import com.aoliaoaoaojiao.config.DataNeededForGrouping;
import com.aoliaoaoaojiao.dao.TrafficRealTimeCityDao;
import com.aoliaoaoaojiao.entity.DiffDirectionStatusCount;
import com.aoliaoaoaojiao.entity.TheWholeCityScore;
import com.aoliaoaoaojiao.service.TrafficRealTimeCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 城市交通实时服务实现
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/03
 */
@Service
public class TrafficRealTimeCityServiceImpl implements TrafficRealTimeCityService {

    @Autowired
    DataNeededForGrouping neededForGrouping;
    @Resource
    private TrafficRealTimeCityDao realTimeStatusDao;


    /**
     * 获得实时的城市交通情况
     *
     * @return {@link TheWholeCityScore}
     */
    @Override
    public TheWholeCityScore getRealTimeCityScore() {
        return realTimeStatusDao.getRealTimeCityScore();
    }

    @Override
    public TheWholeCityScore getRealTimeCityStatus() {
        return realTimeStatusDao.getRealTimeCityStatus();
    }

    @Override
    public DiffDirectionStatusCount getRealTimeCityStatusCount(String status) {
        return realTimeStatusDao.getRealTimeCityStatusCount(status);
    }


}

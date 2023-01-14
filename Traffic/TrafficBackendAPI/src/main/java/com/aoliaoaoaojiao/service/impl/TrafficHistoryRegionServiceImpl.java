package com.aoliaoaoaojiao.service.impl;

import com.aoliaoaoaojiao.config.DataNeededForGrouping;
import com.aoliaoaoaojiao.dao.TrafficHistoryRegionDao;
import com.aoliaoaoaojiao.entity.EveryRegionComplexData;
import com.aoliaoaoaojiao.entity.EveryRegionStatusCount;
import com.aoliaoaoaojiao.service.TrafficHistoryRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TrafficHistoryRegionServiceImpl implements TrafficHistoryRegionService {
    @Resource
    TrafficHistoryRegionDao historyDataDao;
    @Autowired
    DataNeededForGrouping neededForGrouping;

    @Override
    public List<EveryRegionComplexData> getHistoryRegionScore(String adCode,Long startTime, Long endTime) {
        return historyDataDao.getHistoryRegionScore(adCode,startTime, endTime);
    }

    @Override
    public List<EveryRegionComplexData> getHistoryRegionSpeed(String adCode,Long startTime, Long endTime) {
        return historyDataDao.getHistoryRegionSpeed(adCode,startTime, endTime);
    }


    @Override
    public List<EveryRegionStatusCount> getHistoryRegionStatusCount(String adCode,Long startTime, Long endTime) {
        return historyDataDao.getHistoryRegionStatusCount(adCode,startTime, endTime);
    }

}

package com.aoliaoaoaojiao.service.impl;

import com.aoliaoaoaojiao.config.DataNeededForGrouping;
import com.aoliaoaoaojiao.dao.TrafficRealTimeRegionDao;
import com.aoliaoaoaojiao.entity.EveryRegionComplexData;
import com.aoliaoaoaojiao.entity.EveryRegionStatusCount;
import com.aoliaoaoaojiao.service.TrafficRealTimeRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实时地区交通服务层实现
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/03
 */
@Service
public class TrafficRealTimeRegionServiceImpl implements TrafficRealTimeRegionService {

    @Autowired
    DataNeededForGrouping neededForGrouping;
    @Resource
    private TrafficRealTimeRegionDao realTimeStatusDao;

    @Override
    public List<EveryRegionComplexData> getRealTimeRegionScore() {
        return realTimeStatusDao.getRealTimeRegionScore();
    }

    @Override
    public List<EveryRegionComplexData> getRealTimeRegionSpeed() {
        return realTimeStatusDao.getRealTimeRegionSpeed();
    }


    @Override
    public List<EveryRegionStatusCount> getRealTimeRegionStatusCount() {

        return realTimeStatusDao.getRealTimeRegionStatusCount();
    }
}

package com.aoliaoaoaojiao.service.impl;

import com.aoliaoaoaojiao.config.DataNeededForGrouping;
import com.aoliaoaoaojiao.dao.TrafficRealTimeDirectionDao;
import com.aoliaoaoaojiao.entity.DiffDirectionScore;
import com.aoliaoaoaojiao.entity.DiffDirectionStatusCount;
import com.aoliaoaoaojiao.service.TrafficRealTimeDirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实时各方向交通服务层实现
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/03
 */
@Service
public class TrafficRealTimeDirectionServiceImpl implements TrafficRealTimeDirectionService {

    @Autowired
    DataNeededForGrouping neededForGrouping;
    @Resource
    private TrafficRealTimeDirectionDao realTimeStatusDao;

    @Override
    public List<DiffDirectionScore> getRealTimeDirectionScore() {

        return realTimeStatusDao.getRealTimeDirectionScore();
    }

    @Override
    public List<DiffDirectionScore> getRealTimeDirectionSpeed() {
        return realTimeStatusDao.getRealTimeDirectionSpeed();
    }


    @Override
    public  List<DiffDirectionStatusCount> getRealTimeDirectionStatusCount(String direction) {
        return realTimeStatusDao.getRealTimeDirectionStatusCount(direction);
    }
}

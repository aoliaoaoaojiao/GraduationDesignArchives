package com.aoliaoaoaojiao.service.impl;

import com.aoliaoaoaojiao.config.DataNeededForGrouping;
import com.aoliaoaoaojiao.dao.TrafficHistoryDirectionDao;
import com.aoliaoaoaojiao.entity.DiffDirectionScore;
import com.aoliaoaoaojiao.entity.DiffDirectionStatusCount;
import com.aoliaoaoaojiao.service.TrafficHistoryDirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实时方向交通服务实现
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/03
 */
@Service
public class TrafficHistoryDirectionServiceImpl implements TrafficHistoryDirectionService {

    @Autowired
    DataNeededForGrouping neededForGrouping;
    @Resource
    private TrafficHistoryDirectionDao historyDataDao;


    @Override
    public List<DiffDirectionScore> getHistoryDirectionScore(String direction,Long startTime, Long endTime) {
        return historyDataDao.getHistoryDirectionScore(direction,startTime, endTime);
    }

    @Override
    public List<DiffDirectionScore> getHistoryDirectionSpeed(String direction,Long startTime, Long endTime) {
        return historyDataDao.getHistoryDirectionSpeed(direction,startTime, endTime);
    }

    @Override
    public  List<DiffDirectionStatusCount> getHistoryDirectionStatusCount(String direction, Long startTime, Long endTime) {
        return historyDataDao.getHistoryDirectionStatusCount(direction,startTime, endTime);
    }


}

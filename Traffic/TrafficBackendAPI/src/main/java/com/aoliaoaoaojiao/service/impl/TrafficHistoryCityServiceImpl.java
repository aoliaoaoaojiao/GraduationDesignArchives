package com.aoliaoaoaojiao.service.impl;

import com.aoliaoaoaojiao.config.DataNeededForGrouping;
import com.aoliaoaoaojiao.dao.TrafficHistoryCityDao;
import com.aoliaoaoaojiao.entity.DiffDirectionStatusCount;
import com.aoliaoaoaojiao.entity.TheWholeCityScore;
import com.aoliaoaoaojiao.service.TrafficHistoryCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 历史城市交通服务实现
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/08
 */
@Service
public class TrafficHistoryCityServiceImpl implements TrafficHistoryCityService {
    @Resource
    TrafficHistoryCityDao historyDataDao;

    @Autowired
    DataNeededForGrouping neededForGrouping;


    @Override
    public List<TheWholeCityScore> getHistoryCityScore(Long startTime, Long endTime) {
        return historyDataDao.getHistoryCityScore(startTime, endTime);
    }



    @Override
    public List<DiffDirectionStatusCount> getHistoryCityStatusCount(String status, Long startTime, Long endTime) {
        return historyDataDao.getHistoryCityStatusCount(status, startTime, endTime);
    }

}

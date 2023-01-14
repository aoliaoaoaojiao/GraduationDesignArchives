package com.aoliaoaoaojiao.service.impl;

import com.aoliaoaoaojiao.config.DataNeededForGrouping;
import com.aoliaoaoaojiao.dao.TrafficHistoryGradeDao;
import com.aoliaoaoaojiao.entity.DiffGradeRoadOverall;
import com.aoliaoaoaojiao.entity.DiffGradeStatusCount;
import com.aoliaoaoaojiao.service.TrafficHistoryGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 交通历史数据服务层实现
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/29
 */
@Service
public class TrafficHistoryGradeServiceImpl implements TrafficHistoryGradeService {
    @Resource
    TrafficHistoryGradeDao historyDataDao;
    @Autowired
    DataNeededForGrouping neededForGrouping;


    @Override
    public List<DiffGradeRoadOverall> getHistoryGradeSpeed(String grade,Long startTime, Long endTime) {
        return historyDataDao.getHistoryLevelSpeed(grade,startTime, endTime);
    }

    @Override
    public List<DiffGradeRoadOverall> getHistoryGradeScore(String grade,Long startTime, Long endTime) {
        return historyDataDao.getHistoryLevelScore(grade,startTime, endTime);
    }


    @Override
    public List<DiffGradeStatusCount> getHistoryGradeStatusCount(String grade, Long startTime, Long endTime) {
        return historyDataDao.getHistoryLevelStatusCount(grade,startTime, endTime);
    }

}

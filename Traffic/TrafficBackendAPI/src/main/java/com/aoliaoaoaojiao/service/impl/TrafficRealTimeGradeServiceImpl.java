package com.aoliaoaoaojiao.service.impl;

import com.aoliaoaoaojiao.config.DataNeededForGrouping;
import com.aoliaoaoaojiao.dao.TrafficRealTimeGradeDao;
import com.aoliaoaoaojiao.entity.DiffGradeRoadOverall;
import com.aoliaoaoaojiao.entity.DiffGradeStatusCount;
import com.aoliaoaoaojiao.service.TrafficRealTimeGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实时道路状态服务接口实现
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/28
 */
@Service
public class TrafficRealTimeGradeServiceImpl implements TrafficRealTimeGradeService {
    @Autowired
    DataNeededForGrouping neededForGrouping;
    @Resource
    private TrafficRealTimeGradeDao realTimeStatusDao;

    @Override
    public List<DiffGradeRoadOverall> getRealTimeGradeSpeed() {
        return realTimeStatusDao.getRealTimeLevelSpeed();
    }

    @Override
    public List<DiffGradeRoadOverall> getRealTimeGradeScore() {
        return realTimeStatusDao.getRealTimeLevelScore();
    }

    @Override
    public List<DiffGradeStatusCount> getRealTimeGradeStatusCount(String grade) {
        return realTimeStatusDao.getRealTimeLevelStatusCount(grade);
    }

}

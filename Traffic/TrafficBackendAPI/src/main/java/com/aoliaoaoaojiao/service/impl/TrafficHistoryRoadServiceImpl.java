package com.aoliaoaoaojiao.service.impl;

import com.aoliaoaoaojiao.config.DataNeededForGrouping;
import com.aoliaoaoaojiao.dao.TrafficHistoryRoadDao;
import com.aoliaoaoaojiao.entity.DiffRoadStatus;
import com.aoliaoaoaojiao.service.TrafficHistoryRoadService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class TrafficHistoryRoadServiceImpl implements TrafficHistoryRoadService {
    @Resource
    TrafficHistoryRoadDao historyDataDao;
    @Autowired
    DataNeededForGrouping neededForGrouping;

    @Override
    public PageInfo getHistoryDiffRoadStatus(String name, Long startTime, Long endTime, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DiffRoadStatus> result = historyDataDao.getHistoryDiffRoadStatus(name, startTime, endTime);
        for (DiffRoadStatus roadStatus:result){
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(roadStatus.getTimestamp());
            roadStatus.setTimestamp(null);
            roadStatus.setTime(time);
        }
        return new PageInfo<>(result);
    }

    @Override
    public List<DiffRoadStatus> getHistoryDiffRoadTpi(String name, Long startTime, Long endTime) {
        return historyDataDao.getHistoryDiffRoadTpi(name, startTime, endTime);
    }

    @Override
    public List<DiffRoadStatus> getHistoryDiffRoadDtp(String name, Long startTime, Long endTime) {
        return historyDataDao.getHistoryDiffRoadDtp(name, startTime, endTime);
    }

    @Override
    public List<DiffRoadStatus> getHistoryDiffRoadSpeed(String name, Long startTime, Long endTime) {
        return historyDataDao.getHistoryDiffRoadSpeed(name, startTime, endTime);
    }
}

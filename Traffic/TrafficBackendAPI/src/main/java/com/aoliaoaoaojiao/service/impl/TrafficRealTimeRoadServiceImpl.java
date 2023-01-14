package com.aoliaoaoaojiao.service.impl;

import com.aoliaoaoaojiao.api.RestfulHeatMap;
import com.aoliaoaoaojiao.config.DataNeededForGrouping;
import com.aoliaoaoaojiao.dao.TrafficRealTimeRoadDao;
import com.aoliaoaoaojiao.entity.DiffRoadStatus;
import com.aoliaoaoaojiao.entity.RealtimeSectionStatus;
import com.aoliaoaoaojiao.service.TrafficRealTimeRoadService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 实时道路交通服务实现
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/03
 */
@Service
public class TrafficRealTimeRoadServiceImpl implements TrafficRealTimeRoadService {
    @Autowired
    DataNeededForGrouping neededForGrouping;
    @Resource
    private TrafficRealTimeRoadDao realTimeStatusDao;

    /**
     * 得到热力图数据
     *
     * @return {@link List<RestfulHeatMap>}
     */
    @Override
    public List<RestfulHeatMap> getAllRealTimeRoadStatus() {
        List<RestfulHeatMap> heatMapData=new ArrayList<>();
        List<RealtimeSectionStatus> result=null;
        //取出所有数据

        result =  realTimeStatusDao.getAllRealTimeHeatMapData();

        for (RealtimeSectionStatus data:result){
            String polyline = data.getPolyline();
            // （lng,lat;lng,lat;lng,lat;......）
            String[] points = polyline.split(";");
            //取每个坐标点（lng,lat）
            if (points.length > 0) {
                for (String point : points) {
                    if (point.contains(",")) {
                        String lng = point.split(",")[0];
                        String lat = point.split(",")[1];
                        // 添加进返回值
                        heatMapData.add(new RestfulHeatMap(lng, lat, data.getTpi()));
                    }
                }
            }
        }
        return heatMapData;
    }

    /**
     * 得到实时道路状况
     *
     * @param pageNum  页面数量
     * @param pageSize 页面大小
     * @return {@link PageInfo}
     */
    @Override
    public PageInfo getRealTimeSingleRoadStatus(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DiffRoadStatus> result = realTimeStatusDao.getRealTimeDiffRoadStatus();
        return new PageInfo<>(result);
    }

    @Override
    public List<DiffRoadStatus> getRealTimeSingleRoadStatus(String name) {
        return realTimeStatusDao.getRealTimeRoadStatus(name);
    }
}

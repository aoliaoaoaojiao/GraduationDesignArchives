package com.aoliaoaoaojiao.controller;

import com.aoliaoaoaojiao.api.CommonRestful;
import com.aoliaoaoaojiao.entity.EveryRegionStatusCount;
import com.aoliaoaoaojiao.service.TrafficRealTimeRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 实时交通区域控制器
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/03
 */
@RestController
@RequestMapping("/api/realtime/")
public class TrafficRealTimeRegionController {
    @Autowired
    TrafficRealTimeRegionService realTimeService;
    /**
     * 得到实时地区得分
     *
     * @return {@link CommonRestful}
     */
    @GetMapping("region/score")
    public CommonRestful getRealTimeRegionScore(){
        return CommonRestful.success(realTimeService.getRealTimeRegionScore());
    }
    /**
     * 得到实时地区得分
     *
     * @return {@link CommonRestful}
     */
    @GetMapping("region/speed")
    public CommonRestful getRealTimeRegionSpeed(){
        return CommonRestful.success(realTimeService.getRealTimeRegionSpeed());
    }


    /**
     * 得到实时区域不同状态数
     *
     * @return {@link CommonRestful}
     */
    @GetMapping("region/count")
    public CommonRestful getRealTimeRegionStatusCount(){
        List<EveryRegionStatusCount> restfulRegions = realTimeService.getRealTimeRegionStatusCount();
        return CommonRestful.success(restfulRegions);
    }
}

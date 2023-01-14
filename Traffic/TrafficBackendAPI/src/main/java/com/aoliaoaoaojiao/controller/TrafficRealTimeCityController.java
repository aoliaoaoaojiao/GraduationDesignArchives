package com.aoliaoaoaojiao.controller;

import com.aoliaoaoaojiao.api.CommonRestful;
import com.aoliaoaoaojiao.entity.TheWholeCityScore;
import com.aoliaoaoaojiao.service.TrafficRealTimeCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 城市交通实时控制层
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/03
 */
@RestController
@RequestMapping("/api/realtime/")
public class TrafficRealTimeCityController {
    @Autowired
    TrafficRealTimeCityService realTimeService;

    /**
     * 得到实时城市得分
     *
     * @return {@link CommonRestful}
     */
    @GetMapping("city/score")
    public CommonRestful getRealTimeCityDataScore(){
        TheWholeCityScore realTimeCityStatus = realTimeService.getRealTimeCityScore();
        return CommonRestful.success(realTimeCityStatus);
    }

    /**
     * 得到实时城市得分
     *
     * @return {@link CommonRestful}
     */
    @GetMapping("city/status")
    public CommonRestful getRealTimeCityDataStatus(){
        TheWholeCityScore realTimeCityStatus = realTimeService.getRealTimeCityStatus();
        return CommonRestful.success(realTimeCityStatus);
    }

    /**
     * 得到实时城市各种状态数量
     *
     * @param status 状态
     * @return {@link CommonRestful}
     */
    @GetMapping("city/count")
    public CommonRestful getRealTimeCityDataStatusCount(@RequestParam String status){
        return CommonRestful.success(realTimeService.getRealTimeCityStatusCount(status));
    }
}

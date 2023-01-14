package com.aoliaoaoaojiao.controller;

import com.aoliaoaoaojiao.api.CommonRestful;
import com.aoliaoaoaojiao.service.TrafficRealTimeGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实时路控制器
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/28
 */
@RestController
@RequestMapping("/api/realtime/")
public class TrafficRealTimeGradeController {
    @Autowired
    TrafficRealTimeGradeService realTimeService;


    /**
     * 获得实时的不同道路等级的速度
     *
     * @return {@link CommonRestful}
     */
    @GetMapping("grade/speed")
    public CommonRestful getRealTimeGradSpeed(){
        return CommonRestful.success(realTimeService.getRealTimeGradeSpeed());
    }
    /**
     * 获得实时的不同道路等级的分数
     *
     * @return {@link CommonRestful}
     */
    @GetMapping("grade/score")
    public CommonRestful getRealTimeGradScore(){
        return CommonRestful.success(realTimeService.getRealTimeGradeScore());
    }

    /**
     * 得到实时不同等级道路不同状态数量
     *
     * @return {@link CommonRestful}
     */
    @GetMapping("grade/count")
    public CommonRestful getRealTimeLevelStatusCount(@RequestParam String grade){
        return CommonRestful.success( realTimeService.getRealTimeGradeStatusCount(grade));
    }

}

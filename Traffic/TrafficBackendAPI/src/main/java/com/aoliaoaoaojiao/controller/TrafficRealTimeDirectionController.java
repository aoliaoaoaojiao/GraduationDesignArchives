package com.aoliaoaoaojiao.controller;

import com.aoliaoaoaojiao.api.CommonRestful;
import com.aoliaoaoaojiao.service.TrafficRealTimeDirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实时交通方向控制器
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/03
 */
@RestController
@RequestMapping("/api/realtime/")
public class TrafficRealTimeDirectionController {
    @Autowired
    TrafficRealTimeDirectionService realTimeService;
    /**
     * 实时不同方向交通综合分数
     *
     * @return {@link CommonRestful}
     */
    @GetMapping("direction/score")
    public CommonRestful realTimeDirectionScore(){

        return CommonRestful.success(realTimeService.getRealTimeDirectionScore());
    }
    /**
     * 实时不同方向交通平均速度
     *
     * @return {@link CommonRestful}
     */
    @GetMapping("direction/speed")
    public CommonRestful realTimeDirectionSpeed(){
        return CommonRestful.success(realTimeService.getRealTimeDirectionSpeed());
    }


    /**
     * 得到实时不同方向不同状态数量
     *
     * @return {@link CommonRestful}
     */
    @GetMapping("direction/count")
    public CommonRestful getRealTimeDirectionStatusCount(
            @RequestParam String direction
           ){
        return CommonRestful.success(realTimeService.getRealTimeDirectionStatusCount(direction));
    }
}

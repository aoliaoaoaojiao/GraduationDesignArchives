package com.aoliaoaoaojiao.controller;

import com.aoliaoaoaojiao.api.CommonRestful;
import com.aoliaoaoaojiao.service.TrafficTopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

/**
 * 获取拥堵top n数据
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/27
 */
@RestController
@RequestMapping("/api/top/")
public class TrafficTopController {
    @Autowired
    TrafficTopService trafficTopService;

    /**
     * 得到城市拥堵 top n
     *
     * @param num 获取数量
     * @return {@link HashMap}
     */
    @GetMapping("city")
    public CommonRestful getCityTopN(@RequestParam(defaultValue = "20") Integer num){

        return CommonRestful.success(trafficTopService.getCityTopN(num));
    }

    /**
     * 得到行政区域拥堵 top n
     *
     * @param num 数量
     * @return {@link HashMap}
     */
    @GetMapping("region")
    public CommonRestful getRegionTopN(@NotNull @RequestParam String adCode, @RequestParam(defaultValue = "20") Integer num){
        return CommonRestful.success(trafficTopService.getRegionTopN(adCode,num));
    }
    /**
     * 得到不同方向拥堵 top n
     *
     * @param num 数量
     * @return {@link HashMap}
     */
    @GetMapping("direction")
    public CommonRestful getDirectionTopN(@NotNull @RequestParam String direction,@RequestParam(defaultValue = "20") Integer num){
        return CommonRestful.success(trafficTopService.getDirectionTopN(direction,num));
    }
    /**
     * 得到不同方向拥堵 top n
     *
     * @param num 数量
     * @return {@link HashMap}
     */
    @GetMapping("grade")
    public CommonRestful getGradeTopN(@NotNull @RequestParam String grade,@RequestParam(defaultValue = "20") Integer num){
        return CommonRestful.success(trafficTopService.getGradeTopN(grade,num));
    }
}

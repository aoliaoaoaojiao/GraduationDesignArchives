package com.aoliaoaoaojiao.controller;

import com.aoliaoaoaojiao.api.CommonRestful;
import com.aoliaoaoaojiao.service.TrafficHistoryCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * 历史城市总体交通数据控制层
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/10
 */
@RestController
@RequestMapping("/api/history/")
public class TrafficHistoryCityController {

    @Autowired
    TrafficHistoryCityService historyDataService;

    @GetMapping("city/score")
    public CommonRestful getHistoryCityScore(@NotNull @RequestParam Long startTime, @NotNull @RequestParam Long endTime){
        return CommonRestful.success(historyDataService.getHistoryCityScore(startTime,endTime));
    }

    @GetMapping("city/count")
    public CommonRestful getHistoryCityCount(@NotNull @RequestParam String status,@NotNull @RequestParam  Long startTime,@NotNull @RequestParam Long endTime){
        return CommonRestful.success(historyDataService.getHistoryCityStatusCount(status,startTime,endTime));
    }
}

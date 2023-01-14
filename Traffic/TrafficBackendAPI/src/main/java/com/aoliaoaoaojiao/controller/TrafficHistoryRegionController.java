package com.aoliaoaoaojiao.controller;

import com.aoliaoaoaojiao.api.CommonRestful;
import com.aoliaoaoaojiao.service.TrafficHistoryRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * 交通历史区域控制器
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/20
 */
@RestController
@RequestMapping("/api/history/")
public class TrafficHistoryRegionController {
    @Autowired
    TrafficHistoryRegionService historyDataService;

    /**
     * 得到各地区的历史分数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link CommonRestful}
     */
    @GetMapping("region/score")
    public CommonRestful getHistoryRegionScore(
            @NotNull @RequestParam String adCode,
            @NotNull @RequestParam  Long startTime,
            @NotNull @RequestParam Long endTime
    ){
        return CommonRestful.success(historyDataService.getHistoryRegionScore(adCode,startTime,endTime));
    }
    /**
     * 得到各地区的历史速度
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link CommonRestful}
     */
    @GetMapping("region/speed")
    public CommonRestful getHistoryRegionSpeed(
            @NotNull @RequestParam String adCode,
            @NotNull @RequestParam Long startTime,
            @NotNull @RequestParam Long endTime){
        return CommonRestful.success(historyDataService.getHistoryRegionSpeed(adCode,startTime,endTime));
    }

    /**
     * 得到各地区的历史状态数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link CommonRestful}
     */
    @GetMapping("region/count")
    public CommonRestful getHistoryRegionStatusCount(
            @NotNull @RequestParam  String adCode,
            @NotNull @RequestParam  Long startTime,
            @NotNull @RequestParam Long endTime
    ){
        return CommonRestful.success(historyDataService.getHistoryRegionStatusCount(
                adCode,
                startTime,
                endTime)
        );
    }

}

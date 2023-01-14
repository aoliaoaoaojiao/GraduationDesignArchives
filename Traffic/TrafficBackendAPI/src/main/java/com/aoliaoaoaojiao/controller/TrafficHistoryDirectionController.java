package com.aoliaoaoaojiao.controller;

import com.aoliaoaoaojiao.api.CommonRestful;
import com.aoliaoaoaojiao.service.TrafficHistoryDirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * 历史各方向交通数据控制层
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/10
 */
@RestController
@RequestMapping("/api/history/")
public class TrafficHistoryDirectionController {
    @Autowired
    TrafficHistoryDirectionService historyDataService;


    /**
     * 得到不同方向的历史分数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link CommonRestful}
     */
    @GetMapping("direction/score")
    public CommonRestful getHistoryDirectionScore(
            @NotNull @RequestParam String direction,
            @NotNull @RequestParam Long startTime,
            @NotNull @RequestParam Long endTime
    ){

        return CommonRestful.success(historyDataService.getHistoryDirectionScore(direction,startTime,endTime));
    }
    /**
     * 得到不同方向的历史分数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link CommonRestful}
     */
    @GetMapping("direction/speed")
    public CommonRestful getHistoryDirectionSpeed(
            @NotNull @RequestParam String direction,
            @NotNull @RequestParam Long startTime,
            @NotNull @RequestParam Long endTime
    ){

        return CommonRestful.success(historyDataService.getHistoryDirectionSpeed(direction,startTime,endTime));
    }

    /**
     * 各方向历史状态数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param direction 方向
     * @return {@link CommonRestful}
     */
    @GetMapping("direction/count")
    public CommonRestful getHistoryDirectionStatusCount(
            @NotNull @RequestParam String direction,
            @NotNull @RequestParam Long startTime,
            @NotNull @RequestParam Long endTime ){

        return CommonRestful.success(historyDataService.getHistoryDirectionStatusCount(
                direction,
                startTime,
                endTime));
    }
}

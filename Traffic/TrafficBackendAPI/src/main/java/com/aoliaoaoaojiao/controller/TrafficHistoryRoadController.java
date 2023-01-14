package com.aoliaoaoaojiao.controller;

import com.aoliaoaoaojiao.api.CommonRestful;
import com.aoliaoaoaojiao.service.TrafficHistoryRoadService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * 历史道路交通数据控制层
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/10
 */
@RestController
@RequestMapping("/api/history/")
public class TrafficHistoryRoadController {
    @Autowired
    TrafficHistoryRoadService historyDataService;

    /**
     * 不同道路历史状态
     *
     * @param name      名字
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link PageInfo}
     */
    @GetMapping("road/status")
    public PageInfo getHistoryDiffRoadStatus(
            @NotNull(message = "道路参数不可空") @RequestParam String name,
            @NotNull @RequestParam Long startTime,
            @NotNull @RequestParam Long endTime,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        return historyDataService.getHistoryDiffRoadStatus(name,startTime,endTime,pageNum,pageSize);
    }

    /**
     * 不同道路历史TPI
     *
     * @param name      名字
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link CommonRestful}
     */
    @GetMapping("road/tpi")
    public CommonRestful getHistoryDiffRoadTpi(
            @NotNull(message = "道路参数不可空") @RequestParam String name,
            @NotNull @RequestParam Long startTime,
            @NotNull @RequestParam Long endTime
    ){
        return CommonRestful.success(historyDataService.getHistoryDiffRoadTpi(name,startTime,endTime));
    }
    /**
     * 不同道路历史DTP
     *
     * @param name      名字
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link CommonRestful}
     */
    @GetMapping("road/dtp")
    public CommonRestful getHistoryDiffRoadDtp(
            @NotNull(message = "道路参数不可空") @RequestParam String name,
            @NotNull @RequestParam Long startTime,
            @NotNull @RequestParam Long endTime
    ){
        return CommonRestful.success(historyDataService.getHistoryDiffRoadDtp(name,startTime,endTime));
    }
    /**
     * 不同道路历史平均速度
     *
     * @param name      名字
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link CommonRestful}
     */
    @GetMapping("road/speed")
    public CommonRestful getHistoryDiffRoadSpeed(
            @NotNull(message = "道路参数不可空") @RequestParam String name,
            @NotNull @RequestParam Long startTime,
            @NotNull @RequestParam Long endTime
    ){
        return CommonRestful.success(historyDataService.getHistoryDiffRoadSpeed(name,startTime,endTime));
    }

}

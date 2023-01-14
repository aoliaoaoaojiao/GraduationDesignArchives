package com.aoliaoaoaojiao.controller;

import com.aoliaoaoaojiao.api.CommonRestful;
import com.aoliaoaoaojiao.service.TrafficHistoryGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * 历史交通数据
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/29
 */
@RestController
@RequestMapping("/api/history/")
public class TrafficHistoryGradeController {
    @Autowired
    TrafficHistoryGradeService historyDataService;



    /**
     * 获得各等级道路平均速度
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param grade     等级
     * @return {@link CommonRestful}
     */
    @GetMapping("grade/speed")
    public CommonRestful getHistoryGradeSpeed(
            @NotNull @RequestParam String grade,
            @NotNull @RequestParam Long startTime,
            @NotNull @RequestParam Long endTime
    ){
        return CommonRestful.success(historyDataService.getHistoryGradeSpeed(grade,startTime,endTime));
    }

    /**
     * 得到历史成绩分数
     * 获得各等级道路平均速度
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param grade     等级
     * @return {@link CommonRestful}
     */
    @GetMapping("grade/score")
    public CommonRestful getHistoryGradeScore(
            @NotNull @RequestParam String grade,
            @NotNull @RequestParam Long startTime
            ,@NotNull @RequestParam Long endTime){
        return CommonRestful.success(historyDataService.getHistoryGradeScore(grade,startTime,endTime));
    }

    /**
     * 不同等级道路历史状态数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param grade     等级
     * @return {@link CommonRestful}
     */
    @GetMapping("grade/count")
    public CommonRestful getHistoryGradeStatusCount(
            @NotNull @RequestParam String grade,
            @NotNull @RequestParam Long startTime,
            @NotNull @RequestParam Long endTime
    ){
        return CommonRestful.success(historyDataService.getHistoryGradeStatusCount(
                grade,
                startTime,
                endTime)
        );
    }

}

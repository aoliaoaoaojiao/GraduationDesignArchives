package com.aoliaoaoaojiao.controller;

import com.aoliaoaoaojiao.api.CommonRestful;
import com.aoliaoaoaojiao.service.TrafficRealTimeRoadService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实时道路交通控制层
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/06/03
 */
@RestController
@RequestMapping("/api/realtime/")
public class TrafficRealTimeRoadController {
    @Autowired
    TrafficRealTimeRoadService realTimeService;

    /**
     * 实时路段状态(热力图)
     *
     * @return {@link CommonRestful}
     */
    @GetMapping("road/section")
    public CommonRestful realtimeRoadStatusList(){
        return CommonRestful.success(realTimeService.getAllRealTimeRoadStatus());
    }

    /**
     * 得到实时所有道路状况
     *
     * @param pageNum  页面数量
     * @param pageSize 页面大小
     * @return {@link PageInfo}
     */
    @GetMapping("road/overall")
    public PageInfo getRealTimeRoadStatus(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize)
    {
        return realTimeService.getRealTimeSingleRoadStatus(pageNum,pageSize);
    }
    /**
     * 得到实时道路状况
     *
     * @param name  页面数量
     * @return {@link CommonRestful}
     */
    @GetMapping("road/single")
    public CommonRestful getRealTimeRoadStatus(
            @RequestParam String name)
    {
        return  CommonRestful.success(realTimeService.getRealTimeSingleRoadStatus(name));
    }
}

<template>
    <div>
        <br>
        <el-row :gutter="2" class="row-bg" type="flex">
            <el-col class="grid-content bg-purple" :offset="1" :span="7">
                <el-card style="width: auto">
                    <div id="beijing_geo_map" class="div_center" style="width: auto;height:320px;"></div>
                </el-card>
            </el-col>
            <el-col class="grid-content bg-purple" :offset="1" :span="7">
                <el-card style="width: auto">
                    <div id="regionRealTimeCount" class="div_center" style="width: auto;height:320px;"></div>
                </el-card>
            </el-col>

            <el-col class="grid-content bg-purple" :offset="1" :span="7">
                <el-card style="width: auto;">
                    <div
                            id="realTimeRegionStackedBarChart"
                            class="div_center"
                            style="width: auto;height:320px;"
                    >

                    </div>
                </el-card>
            </el-col>
            <el-col :offset="1" :span="1">
            </el-col>
        </el-row>


        <br>
        <el-row>
            <el-col :span="21" :offset="1" v-loading="this.dayRegionSpeedLoading">

                <el-card


                >
                    <div
                            id="drawDaySpeedChart"
                            style="width: auto;height:390px;"
                    >

                    </div>
                </el-card>

            </el-col>
        </el-row>

        <br>
        <el-row>
            <el-col :span="21" :offset="1">

                <el-card


                >
                    <div
                            id="scoreDayChart"
                            style="width: auto;height:390px;"
                    >

                    </div>
                </el-card>

            </el-col>
        </el-row>
        <br>

        <el-row>
            <el-col :span="21" :offset="1">

                <el-card


                >
                    <div
                            id="regionStatusDayCount"
                            style="width: auto;height:390px;"
                    >

                    </div>
                </el-card>

            </el-col>
        </el-row>
        <br>
    </div>
</template>

<script>
    import beijing from "../assets/geo/beijing";

    let adCodeMaps = require('../assets/AdCodeMap.json').data;
    export default {
        name: "region",
        data() {
            return {
                // 获取行政区编号对应之行政区
                CodeToName: Map,
                //各地区对应分数
                regionMapScore: [],

                freeRegion: [],
                baseFreeRegion: [],
                lightCongestionRegion: [],
                moderateCongestionRegion: [],
                seriousCongestionRegion: [],

                traffic_status: ['畅通', '基本畅通', '轻度拥堵', '中度拥堵', '严重拥堵'],
                regionNames: [],

                speedChart_XData: [],
                speedChart_YData: [],
                dayRegionSpeedLoading: true,
                daySpeedSeriesData: [],
                daySpeedXData: [],
                daySpeedRegionName: [],


            }
        },
        mounted() {
            // this.regionDrawStatusDayCount();
        },
        created() {
            this.getAdCodeMap();
            this.getRealTimeRegionScoreData();
            this.getRegionStatusCount();
            this.getRealTimeRegionSpeed();
            this.getRegionDaySpeedData();
            this.getDayScoreRegion();
            this.getDayRegionStatusCount();


        },
        methods: {
            // 获取地区代码
            getAdCodeMap() {

                let result = new Map;
                for (let index in adCodeMaps) {
                    result.set(adCodeMaps[index].adCode, adCodeMaps[index].mapTo);
                }
                this.CodeToName = result;
            },
            //获取各区域相关综合分数
            getRealTimeRegionScoreData() {
                this.$axios({
                    method: "get",
                    url: "/realtime/region/score", // 接口地址,获取综合评分
                }).then(response => {
                    let cityResult = response.data.data;// 成功的返回
                    for (let index in cityResult) {

                        this.regionMapScore.push(
                            {
                                name: this.CodeToName.get(cityResult[index].adCode),
                                value: cityResult[index].areaScore.toFixed(2)
                            }
                        );
                        this.drawRegionScoreChart();
                    }
                }).catch(error => console.log(error, "get Real Time Region Score Data error")); // 失败的返回
            },

            // 获取各地区不同交通状态数量占比
            getRegionStatusCount() {

                this.$axios.get("/realtime/region/count", {
                    params: {}
                }).then(response => {

                        let regionResult = response.data.data;// 成功的返回
                        for (let index = 0; index < regionResult.length; index++) {
                            let regionName = this.CodeToName.get(regionResult[index].adCode);
                            if (this.regionNames.indexOf(regionName) === -1) {
                                if (index > 0) {
                                    if (this.freeRegion.length > this.baseFreeRegion.length) {
                                        this.baseFreeRegion.push(0);
                                    }
                                    if (this.freeRegion.length > this.lightCongestionRegion.length) {
                                        this.lightCongestionRegion.push(0);
                                    }
                                    if (this.freeRegion.length > this.moderateCongestionRegion.length) {
                                        this.moderateCongestionRegion.push(0);
                                    }
                                    if (this.freeRegion.length > this.seriousCongestionRegion.length) {
                                        this.seriousCongestionRegion.push(0);
                                    }
                                }
                                this.regionNames.push(regionName);
                            }
                            if (regionResult[index].status === "畅通") {
                                this.freeRegion.push(regionResult[index].countStatus);
                            }
                            if (regionResult[index].status === "基本畅通") {
                                this.baseFreeRegion.push(regionResult[index].countStatus);
                            }
                            if (regionResult[index].status === "轻度拥堵") {
                                this.lightCongestionRegion.push(regionResult[index].countStatus);
                            }
                            if (regionResult[index].status === "中度拥堵") {
                                this.moderateCongestionRegion.push(regionResult[index].countStatus);
                            }
                            if (regionResult[index].status === "严重拥堵") {
                                this.seriousCongestionRegion.push(regionResult[index].countStatus);
                            }

                        }
                        if (this.freeRegion.length > this.baseFreeRegion.length) {
                            this.baseFreeRegion.push(0);
                        }
                        if (this.freeRegion.length > this.lightCongestionRegion.length) {
                            this.lightCongestionRegion.push(0);
                        }
                        if (this.freeRegion.length > this.moderateCongestionRegion.length) {
                            this.moderateCongestionRegion.push(0);
                        }
                        if (this.freeRegion.length > this.seriousCongestionRegion.length) {
                            this.seriousCongestionRegion.push(0);
                        }
                        for (let index in this.freeRegion) {
                            let sum =
                                this.freeRegion[index] +
                                this.baseFreeRegion[index] +
                                this.lightCongestionRegion[index] +
                                this.moderateCongestionRegion[index] +
                                this.seriousCongestionRegion[index];
                            //获取占比
                            this.freeRegion[index] = (this.freeRegion[index] / sum).toFixed(2);
                            this.baseFreeRegion[index] = (this.baseFreeRegion[index] / sum).toFixed(2);
                            this.lightCongestionRegion[index] = (this.lightCongestionRegion[index] / sum).toFixed(2);
                            this.moderateCongestionRegion[index] = (this.moderateCongestionRegion[index] / sum).toFixed(2);
                            this.seriousCongestionRegion[index] = (this.seriousCongestionRegion[index] / sum).toFixed(2);
                        }
                        this.realTimeDrawRegionCountScale()

                    }
                ).catch(error => console.log(error, "get Region Status Count   error")); // 失败的返回
            },

            // 获取各区当日平均速度
            getRegionDaySpeedData() {
                // 获取当前时间戳
                let endTimestamp = Date.parse(new Date());

                // 获取当天时间戳
                let startTimestamp = Date.parse(new Date(new Date().setHours(0, 0, 0, 0)));


                for (let index in adCodeMaps) {
                    //发送请求
                    this.$axios.get("/history/region/speed", {
                        params: {
                            startTime: startTimestamp,
                            endTime: endTimestamp,
                            adCode: adCodeMaps[index].adCode
                        },
                    }).then(response => {

                        let resultData = response.data.data;
                        let adCode;
                        let dayRegionData = [];
                        for (let index in resultData) {
                            if (index === "0") {
                                adCode = resultData[index].adCode;
                                this.daySpeedRegionName.push(this.CodeToName.get(adCode));
                            }
                            if (adCode === "110101") {
                                let time = new Date(resultData[index].timestamp);
                                this.daySpeedXData.push(time.getHours() + ":" + time.getMinutes());
                            }
                            dayRegionData.push((resultData[index].areaSpeedAvg).toFixed(2))
                        }

                        this.daySpeedSeriesData.push(
                            {

                                name: this.CodeToName.get(adCode),
                                type: 'line',
                                data: dayRegionData
                            }
                        );

                        this.drawDaySpeedPolylineChart();

                    }).catch(error => console.log(error, "error")); // 失败的返回
                }
                this.dayRegionSpeedLoading = false;
                // console.log(this.daySpeedSeriesData);
            },

            // 北京各地区交通综合分数
            drawRegionScoreChart() {

                let myChart = this.$echarts.init(
                    document.getElementById('beijing_geo_map'));
                this.$echarts.registerMap('beijing', beijing);
                let option = {
                    title: {
                        text: '北京市各区交通综合分数',
                        left: 'center',
                        textStyle: {
                            color: '#020202'
                        }
                    },

                    visualMap: {
                        type: 'continuous',
                        min: 0,
                        max: 10,
                        text: ["拥堵", "畅通"],
                        realtime: false,
                        calculable: true,
                        color: ["#ed212c", "#f58522", "#edee20", "#89cf26", "#35843e"],
                        show: true
                    },

                    backgroundColor: '#fbfeff',

                    tooltip: {
                        type: 'continuous',
                        orient: 'horizontal',
                        itemWidth: 10,
                        itemHeight: 80,
                        showLabel: true,
                        seriesIndex: [0],
                        min: 0,
                        max: 100,
                        textStyle: {
                            color: '#f58624'
                        },
                        bottom: 30,
                        left: 'left',
                    },

                    series: [
                        {
                            name: "北京",
                            type: 'map',
                            mapType: "beijing",
                            coordinateSystem: 'geo',
                            label: {

                                normal: {
                                    fontSize: 10,
                                    show: true,
                                    color: "#0f0f10"
                                },
                                emphasis: {
                                    show: true
                                }
                            },
                            roam: true,
                            itemStyle: {
                                normal: {label: {show: true}},
                                emphasis: {label: {show: true}}
                            },
                            data: this.regionMapScore
                        }
                    ]
                };
                myChart.setOption(option);
            },

            // 北京各地区交通状况数量
            realTimeDrawRegionCountScale() {
                let myChart = this.$echarts.init(
                    document.getElementById('regionRealTimeCount'));

                let option = {
                    title: {
                        text: '各区域不同状况路段数比值',
                        left: 'center',
                        textStyle: {
                            color: '#020202'
                        }
                    },
                    angleAxis: {
                        type: 'category',
                        data: this.regionNames
                    },
                    radiusAxis: {},
                    polar: {
                        radius: 85
                    },
                    color: ['#35843e', '#89cf26', '#edee20', '#f58522', '#ed212c',],
                    "tooltip": {
                        "trigger": "axis",
                        "axisPointer": {
                            "type": "shadow",
                            textStyle: {
                                color: "#fff"
                            }

                        },
                    },
                    legend: {
                        x: '2%',
                        top: '85%',
                        show: true,
                        data: ['畅通', '基本畅通', '轻度拥堵', '中度拥堵', '严重拥堵']
                    },
                    series: [
                        {
                            type: 'bar',
                            data: this.freeRegion,
                            coordinateSystem: 'polar',
                            name: '畅通',
                            stack: 'a'
                        }, {
                            type: 'bar',
                            data: this.baseFreeRegion,
                            coordinateSystem: 'polar',
                            name: '基本畅通',
                            stack: 'a'
                        }, {
                            type: 'bar',
                            data: this.lightCongestionRegion,
                            coordinateSystem: 'polar',
                            name: '轻度拥堵',
                            stack: 'a'
                        }, {
                            type: 'bar',
                            data: this.moderateCongestionRegion,
                            coordinateSystem: 'polar',
                            name: '中度拥堵',
                            stack: 'a'
                        }, {
                            type: 'bar',
                            data: this.seriousCongestionRegion,
                            coordinateSystem: 'polar',
                            name: '严重拥堵',
                            stack: 'a'
                        },
                    ],

                };
                myChart.setOption(option);
            },

            // 获取北京各区速度
            getRealTimeRegionSpeed() {
                this.$axios({
                    method: "get",
                    url: "/realtime/region/speed", // 接口地址,获取综合评分
                }).then(response => {
                    let regionSpeed = response.data.data;// 成功的返回
                    for (let i in regionSpeed) {
                        this.speedChart_XData.push(this.CodeToName.get(regionSpeed[i].adCode));
                        this.speedChart_YData.push((regionSpeed[i].areaSpeedAvg).toFixed(2));
                        // console.log(regionSpeed[i])
                    }
                    this.realTimeDrawRegionSpeed();
                }).catch(error => console.log(error, "error")); // 失败的返回
            },

            // 实时北京各区速度
            realTimeDrawRegionSpeed() {
                let myChart = this.$echarts.init(
                    document.getElementById('realTimeRegionStackedBarChart'));

                let option = {
                    title: {
                        text: '北京市各区平均速度',
                        left: 'center',
                        textStyle: {
                            color: '#020202'
                        }
                    },
                    angleAxis: {},
                    radiusAxis: {
                        show: false,
                        type: 'category',
                        data: this.speedChart_XData,
                        fontSize: 2,
                        // z: 20
                    },
                    polar: {},
                    tooltip: {
                        "trigger": "axis",
                        "axisPointer": {
                            "type": "shadow",
                        }, textStyle: {
                            color: "#fff"
                        }
                    },

                    series: [
                        {
                            itemStyle: {
                                normal: {
                                    // 定制显示（按顺序）
                                    color: function (params) {
                                        var colorList = ['rgba(17,42,45,0.46)', '#EFE42A', '#64BD3D', '#EE9201', '#29AAE3', '#B74AE5', '#0AAF9F', '#E89589', '#35843e', 'rgba(6,229,147,0.4)', '#F9E79F', '#BA4A00', 'rgba(121,13,168,0.61)', '#616A6B', '#de1f29', '#4A235A'];
                                        return colorList[params.dataIndex]
                                    }
                                },
                            },
                            type: 'bar',
                            data: this.speedChart_YData,
                            coordinateSystem: 'polar',
                            name: '速度',
                            stack: 'a'
                        },
                    ],
                    legend: {
                        x: '2%',
                        top: '80%',
                        show: false,
                        pageIconInactiveColor: '#aaa'
                    },

                };
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            },

            //矩阵倒置
            reverseMatrix(sourceArr) {

                var reversedArr = [];

                for (var n = 0; n < sourceArr[0].length; n++) {

                    reversedArr[n] = [];

                    for (var j = 0; j < sourceArr.length; j++) {

                        reversedArr[n][j] = sourceArr[j][n];

                    }

                }

                return reversedArr;

            },

            // 北京各区域今日交通速度折线图
            drawDaySpeedPolylineChart() {
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts
                    .init(document.getElementById("drawDaySpeedChart"));
                // 指定图表的配置项和数据
                let option = {
                    title: {
                        left: 'center',
                        text: '北京今日各区交通速度',
                        subtext: '数据量较大，页面较卡请理解'
                    },
                    tooltip: {
                        position: ['50%', '30%'],
                        trigger: "axis",
                        axisPointer: {
                            "type": "shadow",
                        },
                        textStyle: {
                            color: "#fff",
                            fontSize: 10
                        }
                    },
                    toolbox: {
                        show: true,
                        orient: 'vertical',
                        x: 'right',
                        y: 'center',
                        feature: {
                            mark: {
                                show: true
                            },
                            dataView: {
                                show: true,
                                readOnly: false
                            },
                            magicType: {
                                show: true,
                                type: ['line', 'bar']
                            },
                            restore: {
                                show: true
                            },
                            saveAsImage: {
                                show: true
                            }
                        }
                    },
                    legend: {
                        x: '0%',
                        top: '90%',
                        data: this.daySpeedRegionName,
                        textStyle: {
                            fontSize: 10,
                        }
                    },
                    xAxis: {
                        data: this.daySpeedXData,
                    },
                    yAxis: {},
                    series: this.daySpeedSeriesData

                };
                myChart.setOption(option);
                // myChart.autoResize()
            },

            //绘制不同区域日不同交通指数
            drawScoreDayChart(data, provinces, years) {
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts
                    .init(document.getElementById("scoreDayChart"));
                var all = {
                    "data": data,
                    "provinces": provinces,

                    "years": years
                };
                let optionData = [];
                for (let i = 0; i < data.length; i++) {
                    optionData.push(
                        {
                            title: {
                                text: "各区域综合交通指数    时间 " + all.years[i]
                            },
                            tooltip: {
                                trigger: 'axis'
                            },
                            calculable: true,
                            grid: {
                                y: 100,
                                y2: 100
                            },
                            xAxis: [{
                                type: 'category',
                                axisLabel: {
                                    interval: 0
                                },
                                data: all.provinces
                            }],
                            yAxis: [{
                                type: 'value',
                                name: '数值'
                            }],
                            series: [{
                                name: '',
                                type: 'bar',
                                markLine: {
                                    symbol: ['arrow', 'none'],
                                    symbolSize: [4, 2],
                                    itemStyle: {
                                        normal: {
                                            lineStyle: {
                                                color: 'orange'
                                            },
                                            barBorderColor: 'orange',
                                            label: {
                                                position: 'left',
                                                formatter: function (params) {
                                                    return Math.round(params.value);
                                                },
                                                textStyle: {
                                                    color: 'orange'
                                                }
                                            }
                                        }
                                    },

                                },
                                data: all.data[i]
                            }]

                        },
                    )
                }

                let option = {
                    title: {
                        text: "北京市各区综合交通指数",
                        subtext: '数据量较大，页面较卡请理解'
                    },
                    timeline: {
                        axisType: 'category',
                        autoPlay: true,
                        playInterval: 3000,
                        data: all.years,
                    },
                    options: optionData
                };
                myChart.setOption(option);
                // myChart.autoResize()
            },

            // 获取当日每个地区的交通指数
            getDayScoreRegion() {

                // 获取当前时间戳
                let endTimestamp = Date.parse(new Date());

                // 获取当天时间戳
                let startTimestamp = Date.parse(new Date(new Date().setHours(0, 0, 0, 0)));

                let provinces = [];
                let requestData = [];
                let paramData = [];
                let year = [];
                for (let index in adCodeMaps) {
                    //发送请求
                    this.$axios.get("/history/region/score", {
                        params: {
                            startTime: startTimestamp,
                            endTime: endTimestamp,
                            adCode: adCodeMaps[index].adCode
                        },
                    }).then(response => {
                        let resultData = response.data.data;
                        let adCode;
                        let dayRegionData = [];
                        for (let i in resultData) {

                            dayRegionData.push(resultData[i].areaScore);
                            if (i === "0") {
                                adCode = resultData[i].adCode;
                                provinces.push(this.CodeToName.get(adCode));
                            }
                            if (adCode === "110101") {
                                let time = new Date(resultData[i].timestamp);
                                if (time != null) {
                                    let hours = time.getHours();
                                    let minutes = time.getMinutes();
                                    let timeKey;

                                    if (minutes === 0) {
                                        timeKey = hours + ":" + minutes + "0";
                                    } else {
                                        timeKey = hours + ":" + minutes;
                                    }
                                    year.push(timeKey);
                                }

                            }

                        }

                        requestData.push(dayRegionData);
                        paramData = this.reverseMatrix(requestData);
                        this.drawScoreDayChart(paramData, provinces, year);

                    }).catch(error => console.log(error, "error"))

                }
            },

            //获取当天不同区域交通状况数量
            getDayRegionStatusCount() {

                // 获取当前时间戳
                let endTimestamp = Date.parse(new Date());

                // 获取当天时间戳
                let startTimestamp = Date.parse(
                    new Date(new Date().setHours(0, 0, 0, 0))
                );
                let AllData = [];
                let adCode = "";
                for (let index in adCodeMaps) {
                    let regionStatusData = [];
                    //发送请求
                    this.$axios.get("/history/region/count", {
                        params: {
                            startTime: startTimestamp,
                            endTime: endTimestamp,
                            adCode: adCodeMaps[index].adCode
                        },
                    }).then(response => {
                        let resultData = response.data.data;

                        let tmp_time = resultData[0].timestamp;
                        adCode = resultData[0].adCode;
                        let tmp_array_length = 1;
                        let tmp_statusMap = {
                            '畅通': [],
                            '基本畅通': [],
                            '轻度拥堵': [],
                            '中度拥堵': [],
                            '严重拥堵': [],
                        };
                        for (let index = 0; index <= resultData.length; index++) {
                            // 防止数据对不上
                            if (index === resultData.length || resultData[index].timestamp !== tmp_time) {
                                if (tmp_statusMap.畅通.length < tmp_array_length) {
                                    tmp_statusMap.畅通.push(0)
                                }
                                if (tmp_statusMap.基本畅通.length < tmp_array_length) {
                                    tmp_statusMap.基本畅通.push(0)
                                }
                                if (tmp_statusMap.轻度拥堵.length < tmp_array_length) {
                                    tmp_statusMap.轻度拥堵.push(0)
                                }
                                if (tmp_statusMap.中度拥堵.length < tmp_array_length) {
                                    tmp_statusMap.中度拥堵.push(0)
                                }
                                if (tmp_statusMap.严重拥堵.length < tmp_array_length) {
                                    tmp_statusMap.严重拥堵.push(0)
                                }
                                if (index === resultData.length) {
                                    break;
                                }
                                tmp_array_length++;
                                tmp_time = resultData[index].timestamp;
                            }
                            if (resultData[index].status === '畅通') {
                                tmp_statusMap.畅通.push(resultData[index].countStatus)
                            }
                            if (resultData[index].status === '基本畅通') {
                                tmp_statusMap.基本畅通.push(resultData[index].countStatus)
                            }
                            if (resultData[index].status === '轻度拥堵') {
                                tmp_statusMap.轻度拥堵.push(resultData[index].countStatus)
                            }
                            if (resultData[index].status === '中度拥堵') {
                                tmp_statusMap.中度拥堵.push(resultData[index].countStatus)
                            }
                            if (resultData[index].status === '严重拥堵') {
                                tmp_statusMap.严重拥堵.push(resultData[index].countStatus)
                            }
                        }

                        AllData.push(
                            {
                                adCode: adCode,
                                tmp_statusMap
                            }
                        );

                        //此时数据形式为[地区代码：不同时间不同情况数量列表[1,2,3........]]
                        this.getMess(
                            AllData,
                            startTimestamp,
                            endTimestamp);

                    }).catch(error => console.log(error, "error"))

                }

            },
            // 解析返回结果
            getMess(AllData,
                    startTimestamp) {
                let tmp_free = [];
                let tmp_baseFree = [];
                let tmp_lightCongestion = [];
                let tmp_moderateCongestion = [];
                let tmp_seriousCongestion = [];
                let Plist = [];
                // 状态:【【地区1不同时间不同的数量】，【地区2不同时间不同的数量】。。。。。。】
                for (let index = 0; index < AllData.length; index++) {
                    if (Plist.indexOf(AllData[index].adCode)) {
                        Plist.push(this.CodeToName.get(AllData[index].adCode))
                    }
                    tmp_free.push(AllData[index].tmp_statusMap.畅通);
                    tmp_baseFree.push(AllData[index].tmp_statusMap.基本畅通);
                    tmp_lightCongestion.push(AllData[index].tmp_statusMap.轻度拥堵);
                    tmp_moderateCongestion.push(AllData[index].tmp_statusMap.中度拥堵);
                    tmp_seriousCongestion.push(AllData[index].tmp_statusMap.严重拥堵);
                }
                // 状态：【【时间1不同地区的状态数】【时间2不同地区的状态数】。。。。。。。】

                let freeParameter = {};
                let baseFreeParameter = {};
                let lightCongestionParameter = {};
                let moderateCongestionParameter = {};
                let seriousCongestionParameter = {};
                let yearParameter = [];
                //临时参数
                let freeParameter_tmp = this.reverseMatrix(tmp_free);
                let baseFreeParameter_tmp = this.reverseMatrix(tmp_baseFree);
                let lightCongestionParameter_tmp = this.reverseMatrix(tmp_lightCongestion);
                let moderateCongestionParameter_tmp = this.reverseMatrix(tmp_moderateCongestion);
                let seriousCongestionParameter_tmp = this.reverseMatrix(tmp_seriousCongestion);

                // 转换成时间对应的每个地区数据{timekey : [地区1数据，地区2数据，地区3数据。。。。。。]}
                for (let i = 0; i < freeParameter_tmp.length; i++) {
                    let time = new Date(startTimestamp + i * 1000 * 15 * 60);
                    let hours = time.getHours();
                    let minutes = time.getMinutes();
                    let timeKey;

                    if (minutes === 0) {
                        timeKey = hours + ":" + minutes + "0";
                    } else {
                        timeKey = hours + ":" + minutes;
                    }

                    yearParameter.push(timeKey);

                    freeParameter[timeKey] = freeParameter_tmp[i];
                    baseFreeParameter[timeKey] = baseFreeParameter_tmp[i];
                    lightCongestionParameter[timeKey] = lightCongestionParameter_tmp[i];
                    moderateCongestionParameter[timeKey] = moderateCongestionParameter_tmp[i];
                    seriousCongestionParameter[timeKey] = seriousCongestionParameter_tmp[i];
                }
                this.regionDrawStatusDayCount(
                    freeParameter,
                    baseFreeParameter,
                    lightCongestionParameter,
                    moderateCongestionParameter,
                    seriousCongestionParameter,
                    Plist,
                    yearParameter
                );

            },

            // 绘制不同区域在当天各时段的数量
            regionDrawStatusDayCount(
                freeParameter,
                baseFreeParameter,
                lightCongestionParameter,
                moderateCongestionParameter,
                seriousCongestionParameter,
                Plist,
                yearParameter
            ) {
                let myChart = this.$echarts
                    .init(document.getElementById("regionStatusDayCount"));

                var dataMap = {};

                function dataFormatter(obj) {
                    var pList = Plist;
                    var temp;
                    for (var index in yearParameter) {
                        let year = yearParameter[index];
                        var max = 0;
                        var sum = 0;
                        temp = obj[year];
                        for (var i = 0, l = temp.length; i < l; i++) {
                            max = Math.max(max, temp[i]);
                            sum += temp[i];
                            obj[year][i] = {
                                name: pList[i],
                                value: temp[i]
                            }
                        }
                        obj[year + '    max'] = Math.floor(max / 100) * 100;
                        obj[year + '    sum'] = sum;
                    }
                    return obj;
                }

                dataMap.free = dataFormatter(freeParameter);
                dataMap.baseFree = dataFormatter(baseFreeParameter);
                dataMap.lightCongestion = dataFormatter(lightCongestionParameter);
                dataMap.moderateCongestion = dataFormatter(moderateCongestionParameter);
                dataMap.seriousCongestion = dataFormatter(seriousCongestionParameter);


                // console.log(dataMap.dataFinancial);

                function getOptions() {
                    let resultData = [];
                    for (var index in yearParameter) {
                        let time = yearParameter[index];
                        resultData.push(
                            {
                                title: {text: "各区域不同交通状况路段的数量   时间 " + time},
                                series: [
                                    {data: dataMap.free[time]},
                                    {data: dataMap.seriousCongestion[time]},
                                    {data: dataMap.baseFree[time]},
                                    {data: dataMap.lightCongestion[time]},
                                    {data: dataMap.moderateCongestion[time]},
                                ]
                            }
                        )
                    }
                    return resultData;
                }

                let option = {
                    baseOption: {
                        timeline: {
                            axisType: 'category',
                            autoPlay: true,
                            playInterval: 3000,
                            data: yearParameter,

                        },
                        title: {
                            subtext: '数据量较大，页面较卡请理解'
                        },
                        tooltip: {},
                        legend: {
                            x: 'right',
                            data: ['畅通', '基本畅通', '轻度拥堵', '中度拥堵', '严重拥堵'],
                        },
                        calculable: true,
                        grid: {
                            top: 80,
                            bottom: 100
                        },
                        xAxis: [
                            {
                                'type': 'category',
                                'axisLabel': {'interval': 0},
                                'data': Plist
                                ,
                                splitLine: {show: false}
                            }
                        ],
                        yAxis: [
                            {
                                type: 'value',
                                name: '数量',
                                // max: 400
                            }
                        ],
                        series: [
                            {name: '畅通', type: 'bar'},
                            {name: '基本畅通', type: 'bar'},
                            {name: '轻度拥堵', type: 'bar'},
                            {name: '中度拥堵', type: 'bar'},
                            {name: '严重拥堵', type: 'bar'},
                            {
                                name: '数量占比',
                                type: 'pie',
                                center: ['75%', '35%'],
                                radius: '28%'
                            }
                        ]
                    },
                    options: getOptions()
                };
                myChart.setOption(option);
            }
        }
    }
</script>

<style scoped>
    .div_center {
        /*垂直居中 */
        vertical-align: middle;
        /*水平居中*/
        text-align: center;
    }
</style>
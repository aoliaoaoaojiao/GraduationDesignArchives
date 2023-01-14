<template>
    <div>
        <br>
        <el-row :gutter="20" class="row-bg" type="flex">
            <el-col class="grid-content bg-purple" :offset="1" :span="5">
                <el-card style="width: 100%;">
                    <div style="width: 100%;height: 280px">
                        <br>
                        <div class="div_center">
                            <el-progress :width="150" :height="200" type="circle" :percentage="percentage"
                                         :color="customColors">
                            </el-progress>
                        </div>
                        <div class="div_center">
                            <span class="tag-group__title"></span>
                            <el-tag>北京市交通综合分数占比</el-tag>
                        </div>
                        <br>
                        <div class="tag-group div_center">
                            <span class="tag-group__title"></span>
                            <el-tag v-loading="realTimeScoreLoading"
                                    size="medium"
                                    type="danger"
                                    effect="plain">
                                交通状况: {{cityStatus}}
                            </el-tag>
                        </div>
                    </div>
                </el-card>
            </el-col>

            <el-col class="grid-content bg-purple" :span="8">
                <el-card
                        v-loading="realTimeStatusCountLoading"
                        style="width: 100%;"
                >
                    <br>
                    <div id="showRealTimeCityStatusBar" class="div_center " style="width: 100%;height: 260px">
                    </div>
                </el-card>
            </el-col>

            <el-col class="row-bg" type="flex" :span="8">
                <el-card
                        v-loading="realTimeStatusCountLoading"
                        style="width: 100%;">
                    <br>
                    <div id="showRealTimeCityStatusPie" class="box-card " style="width: 100%;height: 260px">
                    </div>
                </el-card>
            </el-col>

        </el-row>
        <br>
        <el-row :gutter="20" class="row-bg">
            <el-col :span="21" :offset="1">
                <el-card
                        style="width: auto"
                        v-loading="dayScoreLoading"
                >
                    <div id="drawChartCategory" style="width: auto;height:390px;">
                    </div>
                </el-card>

            </el-col>
        </el-row>
        <br>
        <el-row :gutter="20" class="row-bg">
            <el-col :span="21" :offset="1">
                <el-card style="width: auto" v-loading="dayCountLoading">
                    <div id="showDayCount" style="width: auto;height:390px;">
                    </div>
                </el-card>

            </el-col>
        </el-row>
        <br>
    </div>
</template>

<script>

    export default {
        name: 'HomeIndex',
        data() {
            return {
                realTimeScoreLoading: true,
                // 等待日交通分数曲线加载
                dayScoreLoading: true,
                // 等待实时状态加载完
                realTimeStatusCountLoading: true,
                //等待当日状态数量加载完
                dayCountLoading: true,
                //城市状态
                cityStatus: "",
                // 分数
                percentage: 30,
                customColors: [
                    {color: '#35843e', percentage: 19.82},
                    {color: '#89cf26', percentage: 39.63},
                    {color: '#edee20', percentage: 59.44},
                    {color: '#f58522', percentage: 79.92},
                    {color: '#ed212c', percentage: 100}
                ],
                // 直方图x轴标题
                bar_X_title: [],
                // 直方图Y轴数据
                bar_Y_data: [],
                // 饼图数据
                pie_data: [],
                // 折线图X轴
                day_polyline_xAxisData: [],
                // 折线图Y轴数据
                polyline_seriesNowData: [],
                polyline_seriesOldData: [],
                //当日状态数量x轴
                dayCount_xAxisData: [],
                // 各状态数据
                free: [],
                baseFree: [],
                lightCongestion: [],
                moderateCongestion: [],
                seriousCongestion: [],
                //日数量图X轴
                day_count_xData: [],
                traffic_status: ['畅通', '基本畅通', '轻度拥堵', '中度拥堵', '严重拥堵']
            };
        },
        created() {
            this.getRealTimeScore();
            this.getRealTimeCityStatus();
            this.getRealTimeCount();
            this.getDayData();
            this.getDayStatusCountData();
        }, mounted() {

        },
        methods: {


            /*   网络请求  */
            // 实时状态数量
            getRealTimeCount() {
                for (let index in this.traffic_status) {
                    this.$axios.get("/realtime/city/count", {
                        params: {
                            status: this.traffic_status[index]
                        }
                    }).then(response => {
                        this.bar_X_title.push(this.traffic_status[index]);
                        this.bar_Y_data.push(response.data.data.countStatus);
                        // for (let index in )
                        this.pie_data.push(
                            {
                                name: response.data.data.status,
                                value: response.data.data.countStatus
                            }
                        );
                        this.drawBarChart();
                        this.drawPieChart();
                    }).catch(error => console.log(error, "error")); // 失败的返回
                }
                this.realTimeStatusCountLoading = false;


            },
            //实时分数
            getRealTimeScore() {
                this.$axios({
                    method: "get",
                    url: "/realtime/city/score", // 接口地址,获取综合评分
                }).then(response => {
                    let score = response.data.data.scoreAvg;// 成功的返回
                    this.realTimeScoreLoading = false;
                    this.percentage = parseFloat((score * 10).toFixed(2));
                }).catch(error => console.log(error, "error")); // 失败的返回
            },
            //实时城市状态
            getRealTimeCityStatus() {
                this.$axios.get("/realtime/city/status",
                    {}
                ).then(response => {
                    this.cityStatus = response.data.data.overallSituation;// 成功的返回
                }).catch(error => console.log(error, "status  error")); // 失败的返回
            },


            //获取日数据
            getDayData() {
                // 获取当前时间戳
                let endTimestamp = Date.parse(new Date());

                // 获取当天时间戳
                let startTimestamp = Date.parse(new Date(new Date().setHours(0, 0, 0, 0)));
                for (let i = startTimestamp; i < startTimestamp + 1000 * 60 * 60 * 24; i += 1000 * 60 * 15) {
                    let time = new Date(i);
                    this.day_polyline_xAxisData.push(time.getHours() + ":" + time.getMinutes())
                }
                //发送请求
                this.$axios.get("/history/city/score", {
                    params: {
                        startTime: startTimestamp,
                        endTime: endTimestamp
                    },
                }).then(response => {
                    let responseData = response.data.data;
                    let result = [];
                    let time_tmp = startTimestamp;
                    for (let key in responseData) {
                        let timeStamp = responseData[key].timestamp;
                        if (timeStamp - time_tmp > 1000 * 60 * 30) {
                            for (let start = time_tmp; start < timeStamp; start += 1000 * 60 * 15) {
                                //若丢失数据则取中间值
                                result.push((responseData[key].scoreAvg + responseData[key - 1].scoreAvg) / 2);
                            }
                        }
                        time_tmp = timeStamp;
                        let time = new Date(responseData[key].timestamp);
                        let sizeTime = time.getHours() + ":" + time.getMinutes();
                        result.push(responseData[key].scoreAvg);
                    }
                    this.polyline_seriesNowData = result;
                    this.drawPolylineChart()

                }).catch(error => console.log(error, "error")); // 失败的返回

                //发送请求 --昨日
                this.$axios.get("/history/city/score", {
                    params: {
                        startTime: startTimestamp - 1000 * 60 * 60 * 24,
                        endTime: startTimestamp - 1000 * 60 * 15
                    },
                }).then(response => {
                    let responseData = response.data.data;
                    let result = [];
                    let time_tmp = startTimestamp - 1000 * 60 * 15;
                    for (let key in responseData) {
                        let time = new Date(responseData[key].timestamp);
                        let timeStamp = responseData[key].timestamp;
                        if (timeStamp - time_tmp > 1000 * 60 * 30) {
                            for (let start = time_tmp; start < timeStamp; start += 1000 * 60 * 15) {
                                result.push((responseData[key].scoreAvg + responseData[key - 1].scoreAvg) / 2);
                            }
                        }
                        time_tmp = timeStamp;
                        let sizeTime = time.getHours() + ":" + time.getMinutes();
                        result.push(responseData[key].scoreAvg);
                    }
                    this.polyline_seriesOldData = result;
                    this.dayScoreLoading = false;
                    this.drawPolylineChart()
                }).catch(error => console.log(error, "error")); // 失败的返回

            },


            //日不同状态路段数量
            getDayStatusCountData() {
                let timeList = [];
                let endTimestamp = Date.parse(new Date());
                let startTimestamp = Date.parse(new Date(new Date().setHours(0, 0, 0, 0)));
                for (let i = startTimestamp; i < endTimestamp - endTimestamp % (1000 * 60 * 15); i += 1000 * 60 * 15) {
                    let time = new Date(i);
                    timeList.push(i);
                    this.day_count_xData.push(time.getHours() + ":" + time.getMinutes())
                }
                for (let index in this.traffic_status) {
                    //发送请求
                    this.$axios.get("/history/city/count", {
                        params: {
                            startTime: startTimestamp,
                            endTime: endTimestamp,
                            status: this.traffic_status[index]
                        },
                    }).then(response => {
                        let responseData = response.data.data;
                        let n_tmp = 0;
                        for (let n in responseData) {
                            if (timeList[n_tmp] < responseData[n].timestamp) {
                                for (n_tmp; timeList[n_tmp] < responseData[n].timestamp; n_tmp++) {
                                    if (this.traffic_status[index] === "畅通") {
                                        this.free.push(0)
                                    }
                                    if (this.traffic_status[index] === "基本畅通") {
                                        this.baseFree.push(0)
                                    }
                                    if (this.traffic_status[index] === "轻度拥堵") {
                                        this.lightCongestion.push(0)
                                    }
                                    if (this.traffic_status[index] === "中度拥堵") {
                                        this.moderateCongestion.push(0)
                                    }
                                    if (this.traffic_status[index] === "严重拥堵") {
                                        this.seriousCongestion.push(0)
                                    }
                                }
                            }
                            n_tmp++;
                            if (this.traffic_status[index] === "畅通") {
                                this.free.push(responseData[n].countStatus)
                            }
                            if (this.traffic_status[index] === "基本畅通") {
                                this.baseFree.push(responseData[n].countStatus)
                            }
                            if (this.traffic_status[index] === "轻度拥堵") {
                                this.lightCongestion.push(responseData[n].countStatus)
                            }
                            if (this.traffic_status[index] === "中度拥堵") {
                                this.moderateCongestion.push(responseData[n].countStatus)
                            }
                            if (this.traffic_status[index] === "严重拥堵") {
                                this.seriousCongestion.push(responseData[n].countStatus)
                            }
                        }
                        this.drawDayCount();
                    }).catch(error => console.log(error, "error")); // 失败的返回
                }
                this.dayCountLoading = false;

            },
            /*   画图   */
            // 可变时间轴
            drawDayCount() {
                let myChart = this.$echarts
                    .init(document.getElementById("showDayCount"));
                let option = {
                    backgroundColor: "rgb(255,255,255)",
                    "title": {
                        "text": "本日各交通状况路段数量",
                        x: "2%",
                        textStyle: {
                            color: '#020202',
                            fontSize: '22'
                        },
                        subtextStyle: {
                            color: '#020202',
                            fontSize: '16',

                        },
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
                    "tooltip": {
                        "trigger": "axis",
                        "axisPointer": {
                            "type": "shadow",
                        }, textStyle: {
                            color: "#fff"
                        }
                    },
                    "grid": {
                        "borderWidth": 0,
                        "top": 110,
                        "bottom": 95,
                        textStyle: {
                            color: "#fff"
                        }
                    },
                    "legend": {

                        x: '4%',
                        top: '8%',
                        textStyle: {
                            color: '#90979c',
                        },
                        "data": ['轻度拥堵', '中度拥堵', '严重拥堵', '畅通', '基本畅通'],
                        selected: {
                            '畅通': false,
                            '基本畅通': true,
                            '轻度拥堵': true,
                            '中度拥堵': true,
                            '严重拥堵': true
                        }
                    },
                    "calculable": true,
                    "xAxis": [{
                        "type": "category",
                        "axisLine": {
                            lineStyle: {
                                color: '#90979c'
                            }
                        },
                        "splitLine": {
                            "show": false
                        },
                        "axisTick": {
                            "show": false
                        },
                        "splitArea": {
                            "show": false
                        },
                        "axisLabel": {
                            "interval": 5,

                        },
                        "data": this.day_count_xData,
                    }],
                    "yAxis": [{
                        "type": "value",
                        "splitLine": {
                            "show": false
                        },
                        "axisLine": {
                            lineStyle: {
                                color: '#90979c'
                            }
                        },
                        "axisTick": {
                            "show": false
                        },
                        "axisLabel": {
                            "interval": 0,

                        },
                        "splitArea": {
                            "show": false
                        },

                    }],
                    "dataZoom": [{
                        "show": true,
                        "height": 20,
                        "xAxisIndex": [
                            0
                        ],
                        bottom: 10,
                        "start": 70,
                        "end": 100,
                        handleIcon: 'path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z',
                        handleSize: '110%',
                        handleStyle: {
                            color: "#020202",
                        },
                        textStyle: {
                            color: "#fff"
                        },
                        borderColor: "#90979c"


                    }, {
                        "type": "inside",
                        "show": true,
                        "height": 15,
                        "start": 1,
                        "end": 35
                    }],
                    "series": [{
                        "name": "畅通",
                        "type": "bar",
                        "stack": "总量",
                        "barMaxWidth": 30,
                        "barGap": "20%",
                        "itemStyle": {
                            "normal": {
                                "color": "rgb(54,133,63)",
                                "label": {
                                    "show": true,
                                    "textStyle": {
                                        "color": "#fff"
                                    },
                                    "position": "inside",
                                    formatter: function (p) {
                                        return p.value > 0 ? (p.value) : '';
                                    }
                                }
                            }
                        },
                        "data": this.free,
                    },

                        {
                            "name": "基本畅通",
                            "type": "bar",
                            "stack": "总量",
                            "itemStyle": {
                                "normal": {
                                    "color": "rgb(137,207,38)",
                                    "barBorderRadius": 0,
                                    "label": {
                                        "show": true,
                                        "position": "inside",
                                        formatter: function (p) {
                                            return p.value > 0 ? (p.value) : '';
                                        }
                                    }
                                }
                            },
                            "data": this.baseFree
                        }, {
                            "name": "轻度拥堵",
                            "type": "bar",
                            "stack": "总量",
                            "itemStyle": {
                                "normal": {
                                    "color": "rgb(237,238,32)",
                                    "barBorderRadius": 0,
                                    "label": {
                                        "show": true,
                                        "position": "inside",
                                        formatter: function (p) {
                                            return p.value > 0 ? (p.value) : '';
                                        }
                                    }
                                }
                            },
                            "data": this.lightCongestion
                        }, {
                            "name": "中度拥堵",
                            "type": "bar",
                            "stack": "总量",
                            "itemStyle": {
                                "normal": {
                                    "color": "rgb(245,134,36)",
                                    "barBorderRadius": 0,
                                    "label": {
                                        "show": true,
                                        "position": "inside",
                                        formatter: function (p) {
                                            return p.value > 0 ? (p.value) : '';
                                        }
                                    }
                                }
                            },
                            "data": this.moderateCongestion
                        }, {
                            "name": "严重拥堵",
                            "type": "bar",
                            "stack": "总量",
                            "itemStyle": {
                                "normal": {
                                    "color": "rgb(225,31,42)",
                                    "barBorderRadius": 0,
                                    "label": {
                                        "show": true,
                                        "position": "inside",
                                        formatter: function (p) {
                                            return p.value > 0 ? (p.value) : '';
                                        }
                                    }
                                }
                            },
                            "data": this.seriousCongestion
                        },
                    ]
                };
                myChart.setOption(option)

            },
            //  直方图
            drawBarChart() {
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts
                    .init(document.getElementById("showRealTimeCityStatusBar"));
                // 指定图表的配置项和数据
                let option = {
                    title: {
                        text: "北京市各状态路段数量"
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    xAxis: {
                        data: this.bar_X_title,
                        axisLabel: {
                            formatter: function (value) {
                                return value.split("").join("\n")
                            }
                        },
                    },

                    yAxis: {},
                    series: [
                        {
                            name: "单位：段",
                            type: "bar",
                            data: this.bar_Y_data
                        }
                    ],
                    grid: { // 控制图的大小，调整下面这些值就可以，
                        x: 50,
                        x2: 25,
                        y2: 100,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
                    },
                };
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
                myChart.resize();
            },
            //  饼图
            drawPieChart() {
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts
                    .init(document.getElementById("showRealTimeCityStatusPie"));
                let option = {
                    title: {
                        text: "北京市各状态路段占比"
                    },
                    series: [
                        {
                            name: '状态数量',
                            type: 'pie',
                            radius: '40%',
                            data: this.pie_data,
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true,
                                        formatter: '{b} : {d}%'
                                    },
                                    labelLine: {show: true}
                                }
                            }
                        }
                    ]
                };
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
                myChart.resize();
            },

            // 折线图
            drawPolylineChart() {
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts
                    .init(document.getElementById("drawChartCategory"));
                // 指定图表的配置项和数据

                let option = {
                    title: {
                        text: '综合交通指数'
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
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['今日交通指数', '昨日交通指数'],
                    },
                    xAxis: {
                        data: this.day_polyline_xAxisData
                    },
                    yAxis: {},
                    series: [{
                        name: '今日交通指数',
                        type: 'line',
                        data: this.polyline_seriesNowData
                    }, {
                        name: "昨日交通指数",
                        type: 'line',
                        data: this.polyline_seriesOldData,
                        itemStyle: {
                            normal: {
                                lineStyle: {
                                    color: '#292c2d',//2EC7C9
                                    width: 3,
                                    type: 'dotted'  //'dotted'虚线 'solid'实线
                                }
                            }
                        },

                    },]
                };
                // 使用刚指定的配置项和数据显示图表。
                this.$nextTick(
                    () => {
                        myChart.setOption(option)
                    }
                )

            },

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

    .box-card {
        width: 390px;
        height: 300px;
    }
</style>
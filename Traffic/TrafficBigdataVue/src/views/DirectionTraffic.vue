<template>
    <div>
        <br>
        <el-row :gutter="2" class="row-bg" type="flex">
            <el-col class="grid-content bg-purple" :offset="1" :span="7">
                <el-card style="width: auto">
                    <div id="realTimeDirectionSpeed" class="div_center" style="width: auto;height:320px;"></div>
                </el-card>
            </el-col>
            <el-col class="grid-content bg-purple" :offset="1" :span="7">
                <el-card style="width: auto">
                    <div id="directionRealTimeCount" class="div_center" style="width: auto;height:320px;"></div>
                </el-card>
            </el-col>

            <el-col class="grid-content bg-purple" :offset="1" :span="7">
                <el-card style="width: auto;">
                    <div
                            id="directionRealTimeScore"
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
            <el-col :span="21" :offset="1">

                <el-card


                >
                    <div
                            id="directionDaySpeedChart"
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
                            id="directionScoreDayChart"
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
                            id="directionStatusDayCount"
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
    let directionNames=["南->北","北->南","东->西","西->东"];
    export default {
        name: "directionTraffic",
        mounted() {
            // this.drawMutilPieChart();
        },
        created() {
            this.getDirectionStatusCount();
            this.getRealTimeDirectionSpeed();
            this.getRealTimeDirectionScore();
            this.getDirectionDaySpeedData();
            this.getDayScoreDirection();
            this.getDayScoreDirection();
            this.getDayDirectionStatusCount();

        },
        data() {
            return {}
        },
        methods: {

            // 获取实时不同方向的平均速度
            getRealTimeDirectionSpeed() {
                this.$axios.get("/realtime/direction/speed", {
                    params: {}
                }).then(response => {
                    let resultData = response.data.data;
                    let legendData = [];
                    let seriesData = [];
                    var dataStyle = {
                        normal: {
                            label: {
                                show: false
                            },
                            labelLine: {
                                show: false
                            },
                            // shadowBlur: 15,
                            // shadowColor: 'white',
                        }
                    };
                    var placeHolderStyle = {
                        normal: {
                            color: 'rgba(0,0,0,0)',
                            label: {
                                show: false
                            },
                            labelLine: {
                                show: false
                            }
                        },
                        emphasis: {
                            color: 'rgba(0,0,0,0)'
                        }
                    };
                    for (let index in resultData) {
                        legendData.push(resultData[index].direction);
                        seriesData.push(
                            {
                                type: 'pie',
                                clockWise: true,
                                hoverAnimation: false,
                                radius: [
                                    (65-index*10).toString()+"%",
                                    (75-index*10-3).toString()+"%"
                                ],
                                itemStyle: dataStyle,

                                data: [{
                                    value: resultData[index].directionSpeedAvg.toFixed(2),
                                    name: resultData[index].direction
                                },
                                    {
                                        value: 10,
                                        name: '总数',
                                        tooltip: {
                                            show: false
                                        },
                                        itemStyle: placeHolderStyle
                                    }

                                ]
                            }
                        )
                    }
                    this.drawRingDirectionSpeed(legendData, seriesData);

                }).catch(error => console.log(error, "get Real Time Direction Speed error")); // 失败的返回
            },

            // 绘制不同方向速度的环形图
            drawRingDirectionSpeed(legendData, seriesData) {
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts
                    .init(document.getElementById("realTimeDirectionSpeed"));
                let option = {
                    tooltip : {
                        show: true,
                    },
                    title: {
                        text: "各方向平均交通速度"
                    },
                    legend: {
                        top: "80%",
                        data:legendData,
                        textStyle: {
                            color: '#000000',
                            align:'right',
                            x: 'right',
                            textAlign:'right'
                        },

                        selectedMode: true,
                    },
                    series: seriesData
                };

                myChart.setOption(option)

            },

            // 获取不同方向实时分数
            getRealTimeDirectionScore() {
                this.$axios.get("/realtime/direction/score", {
                    params: {}
                }).then(response => {
                    let resultData = response.data.data;
                    let XAxisData = [];
                    let seriesData = [];
                    for (let index in resultData) {
                        XAxisData.push(resultData[index].direction);
                        seriesData.push(resultData[index].directionScore.toFixed(2));
                    }
                    this.drawScoreDirectionBar(XAxisData, seriesData);
                }).catch(error => console.log(error, "get Real Time Direction Score error")); // 失败的返回

            },

            //  不同方向分数直方图
            drawScoreDirectionBar(XAxisData, seriesData) {
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts
                    .init(document.getElementById("directionRealTimeScore"));
                // 指定图表的配置项和数据
                let option = {
                    title: {
                        text: "各方向交通综合指数"
                    },
                    tooltip: {
                        position: ['20%', '30%'],
                        trigger: 'item',
                    },
                    xAxis: {
                        data: XAxisData,
                        axisLabel: {
                            formatter: function (value) {
                                return value.split("")[0] + "\n到\n" + value.split("")[3]
                            }
                        },
                    },
                    yAxis: {},
                    series: [
                        {
                            name: "评分",
                            type: "bar",
                            data: seriesData
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

            // 获取不同方向不同状态的数量
            getDirectionStatusCount(){
                let seriesData=[];
                let legendData = directionNames;
                for (let index in directionNames){
                    this.$axios.get("/realtime/direction/count", {
                        params: {
                            direction:directionNames[index]
                        }
                    }).then(response => {
                        let resultData = response.data.data;
                        let tmpSeriesData=[];

                        for (let index in resultData){
                            legendData.push(resultData[index].status);
                            tmpSeriesData.push(
                                {
                                    value:resultData[index].countStatus,
                                    name:resultData[index].status,
                                }
                            );
                        }
                        seriesData.push(
                            {
                                value:900,
                                name:resultData[0].direction,
                                children:tmpSeriesData
                            }
                        );
                        this.drawSunburstPieChart(seriesData);
                    }).catch(error => console.log(error, "get Direction Status Count error")); // 失败的返回
                }

            },

            // 绘制旭日图
            drawSunburstPieChart(seriesData) {
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts
                    .init(document.getElementById("directionRealTimeCount"));
                var option = {
                    title: {
                        text: "各方向不同交通状态数量"
                    },
                    series: {
                        radius: ["10%", '90%'],
                        label: {
                            rotate: 'radial'
                        },
                        "tooltip": {
                            "trigger": "axis",
                            "axisPointer": {
                                "type": "shadow",
                            }, textStyle: {
                                color: "#fff"
                            }
                        },
                        type: 'sunburst',
                        data: seriesData
                    }
                };
                myChart.setOption(option)
            },

            // 获取各方向当日平均速度
            getDirectionDaySpeedData() {
                // 获取当前时间戳
                let endTimestamp = Date.parse(new Date());

                // 获取当天时间戳
                let startTimestamp = Date.parse(new Date(new Date().setHours(0, 0, 0, 0)));
                // option数据
                let SeriesData=[];
                // 时间轴
                let speedTimeXData=[];
                // 数据项
                let legendDirectionData=[];
                for (let index in directionNames) {
                    //发送请求
                    this.$axios.get("/history/direction/speed", {
                        params: {
                            startTime: startTimestamp,
                            endTime: endTimestamp,
                            direction: directionNames[index]
                        },
                    }).then(response => {

                        let resultData = response.data.data;
                        let direction;
                        let dayDirectionData = [];
                        for (let index in resultData) {
                            if (index === "0") {
                                direction = resultData[index].direction;
                                legendDirectionData.push(direction);
                            }
                            if (direction === "北->南") {
                                let time = new Date(resultData[index].timestamp);
                                speedTimeXData.push(time.getHours() + ":" + time.getMinutes());
                            }
                            dayDirectionData.push((resultData[index].directionSpeedAvg).toFixed(2))
                        }

                        SeriesData.push(
                            {

                                name: direction,
                                type: 'line',
                                data: dayDirectionData
                            }
                        );

                        this.drawDirectionDaySpeed(legendDirectionData,speedTimeXData,SeriesData);

                    }).catch(error => console.log(error, "get Direction Day Speed error")); // 失败的返回
                }
                // console.log(this.daySpeedSeriesData);
            },

            // 绘制今日北京各方向的平均速度
            drawDirectionDaySpeed(legendDirectionData,speedTimeXData,SeriesData){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts
                    .init(document.getElementById("directionDaySpeedChart"));
                // 指定图表的配置项和数据
                let option = {
                    title: {
                        left: 'center',
                        text: '北京今日各方向交通速度',
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
                        data: legendDirectionData,
                        textStyle: {
                            fontSize: 10,
                        }
                    },
                    xAxis: {
                        data: speedTimeXData,
                    },
                    yAxis: {},
                    series: SeriesData
                };
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

            // 获取当日每个方向的交通指数
            getDayScoreDirection() {

                // 获取当前时间戳
                let endTimestamp = Date.parse(new Date());

                // 获取当天时间戳
                let startTimestamp = Date.parse(new Date(new Date().setHours(0, 0, 0, 0)));

                let directions = [];
                let requestData = [];
                let paramData = [];
                let year = [];
                for (let index in directionNames) {
                    //发送请求
                    this.$axios.get("/history/direction/score", {
                        params: {
                            startTime: startTimestamp,
                            endTime: endTimestamp,
                            direction: directionNames[index]
                        },
                    }).then(response => {

                        let resultData = response.data.data;
                        let direction;
                        let dayDirectionData = [];
                        for (let i in resultData) {

                            dayDirectionData.push(resultData[i].directionScore);
                            if (i === "0") {
                                direction = resultData[i].direction;
                                directions.push(direction);
                            }
                            if (direction === "东->西") {
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

                        requestData.push(dayDirectionData);
                        paramData = this.reverseMatrix(requestData);
                        this.drawScoreDayChart(paramData, directions, year);

                    }).catch(error => console.log(error, "get Day Score Direction error"))

                }
            },

            //绘制不同区域日不同交通指数
            drawScoreDayChart(data, direction, years) {
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts
                    .init(document.getElementById("directionScoreDayChart"));
                var all = {
                    "data": data,
                    "provinces": direction,
                    "years": years
                };
                let optionData = [];
                for (let i = 0; i < data.length; i++) {
                    optionData.push(
                        {
                            title: {
                                text: "北京市各方向综合交通指数    时间 " + all.years[i]
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
                        text: "北京市各方向综合交通指数",
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

            //获取当天不同方向交通状况数量
            getDayDirectionStatusCount() {

                // 获取当前时间戳
                let endTimestamp = Date.parse(new Date());

                // 获取当天时间戳
                let startTimestamp = Date.parse(
                    new Date(new Date().setHours(0, 0, 0, 0))
                );
                let AllData = [];
                let direction = "";
                for (let index in directionNames) {
                    //发送请求
                    this.$axios.get("/history/direction/count", {
                        params: {
                            startTime: startTimestamp,
                            endTime: endTimestamp,
                            direction: directionNames[index]
                        },
                    }).then(response => {
                        let resultData = response.data.data;

                        let tmp_time = resultData[0].timestamp;
                        direction = resultData[0].direction;
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
                                direction: direction,
                                tmp_statusMap
                            }
                        );

                        //此时数据形式为[地区代码：不同时间不同情况数量列表[1,2,3........]]
                        this.getMess(
                            AllData,
                            startTimestamp,
                            endTimestamp);

                    }).catch(error => console.log(error, "get Day Direction Status Count error"))

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
                // 状态:【【方向1不同时间不同的数量】，【方向2不同时间不同的数量】。。。。。。】
                for (let index = 0; index < AllData.length; index++) {
                    if (Plist.indexOf(AllData[index].direction)) {
                        Plist.push(AllData[index].direction)
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
                this.directionDrawStatusDayCount(
                    freeParameter,
                    baseFreeParameter,
                    lightCongestionParameter,
                    moderateCongestionParameter,
                    seriousCongestionParameter,
                    Plist,
                    yearParameter
                );

            },

            // 绘制不同方向在当天各时段的数量
            directionDrawStatusDayCount(
                freeParameter,
                baseFreeParameter,
                lightCongestionParameter,
                moderateCongestionParameter,
                seriousCongestionParameter,
                Plist,
                yearParameter
            ) {
                let myChart = this.$echarts
                    .init(document.getElementById("directionStatusDayCount"));

                var dataMap = {};

                function dataFormatter(obj) {
                    var pList = Plist;
                    // ['北京', '天津', '河北', '山西', '内蒙古', '辽宁', '吉林', '黑龙江', '上海', '江苏', '浙江', '安徽', '福建', '江西', '山东', '河南', '湖北', '湖南', '广东', '广西', '海南', '重庆', '四川', '贵州', '云南', '西藏', '陕西', '甘肃', '青海', '宁夏', '新疆'];
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


                function getOptions() {
                    let resultData = [];
                    for (var index in yearParameter) {
                        let time = yearParameter[index];
                        resultData.push(
                            {
                                title: {text: "各方向不同交通状况路段的数量   时间 " + time},
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

</style>
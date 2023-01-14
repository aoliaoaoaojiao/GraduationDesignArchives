<template>
    <div>
        <br>
        <el-row :gutter="2" class="row-bg" type="flex">
            <el-col class="grid-content bg-purple" :offset="1" :span="7">
                <el-card style="width: auto">
                    <div id="gradRadar" class="div_center" style="width: auto;height:320px;"></div>
                </el-card>
            </el-col>

            <el-col class="grid-content bg-purple" :offset="1" :span="7">
                <el-card style="width: auto">
                    <div id="gradeRealTimeScoreBar" class="div_center" style="width: auto;height:320px;"></div>
                </el-card>
            </el-col>

            <el-col class="grid-content bg-purple" :offset="1" :span="7">
                <el-card style="width: auto;">
                    <div
                            id="gradeSpeedBarChart"
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


                <el-card>
                    <el-select v-model="value" @change="getGradeHistorySpeedData(value)" filterable placeholder="请选择">
                        <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                    <div
                            id="drawGradeDaySpeedChart"
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
                            id="gradeScoreDayChart"
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
                            id="gradeStatusDayCount"
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
    // 按GB 50220-1995:城市道路网包括快速路、主干路、次干路、支路，分别对应1、2、3、4
    // 用来获取等级对应的道路名称
    let roadGradeData = {"1": "快速路", "2": "主干路", "3": "次干路", "4": "支路"};
    export default {
        data() {
            return {
                options: [{
                    value: '1',
                    label: '快速路'
                }, {
                    value: '2',
                    label: '主干路'
                }, {
                    value: '3',
                    label: '次干路'
                }, {
                    value: '4',
                    label: '支路'
                }],
                value: '1'
            }
        },
        name: "RoadGrade",
        mounted() {
            // this.drawGradeRealTimeRadar();
        },
        created() {
            this.getRealTimeGradeScore();
            this.getRealTimeGradeSpeed();
            this.getRealTimeGradeStatusCount();
            this.getGradeHistorySpeedData("1");
            this.getDayScoreGrade();
            this.getDayGradeStatusCount();
        },
        methods: {

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

            // 实时分数
            getRealTimeGradeScore() {
                let gradeName = [];
                let realtimeGradeScore = [];
                //发送请求
                this.$axios.get("/realtime/grade/score", {
                    params: {},
                }).then(response => {
                    let resultData = response.data.data;
                    for (let index in resultData) {
                        realtimeGradeScore.push(resultData[index].scoreAvg.toFixed(2));
                        gradeName.push(roadGradeData[resultData[index].level])
                    }
                    this.drawBarChart(gradeName, realtimeGradeScore)
                }).catch(error => console.log(error, "realtime grade score error...")); // 失败的返回
            },

            //  直方图
            drawBarChart(xAxisData, seriesData) {
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts
                    .init(document.getElementById("gradeRealTimeScoreBar"));
                // 指定图表的配置项和数据
                let option = {
                    title: {
                        text: "各等级道路综合交通指数"
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    xAxis: {
                        data: xAxisData,
                        axisLabel: {
                            formatter: function (value) {
                                return value.split("").join("\n")
                            }
                        },
                    },
                    yAxis: {},
                    series: [
                        {

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

            // 实时速度
            getRealTimeGradeSpeed() {
                //等级对应的名字
                let gradeName = [];
                //实时速度列表
                let realtimeGradeSpeed = [];
                //昨日速度列表
                let oldGradeSpeed = [];
                // 实时等级对应速度
                let nowGradeSpeedMap = {};
                //发送请求
                this.$axios.get("/realtime/grade/speed", {
                    params: {},
                }).then(response => {
                    let resultData = response.data.data;
                    for (let index in resultData) {
                        nowGradeSpeedMap[roadGradeData[resultData[index].level]] = resultData[index].diffLevelSpeed.toFixed(3);
                    }
                    let endTimestamp = Date.parse(new Date()) - 1000 * 60 * 60 * 24;
                    let startTimestamp = endTimestamp - 1000 * 60 * 15;
                    for (let index in roadGradeData) {
                        this.$axios.get("/history/grade/speed", {
                            params: {
                                endTime: endTimestamp,
                                startTime: startTimestamp,
                                grade: index
                            },
                        }).then(response => {
                            let resultData = response.data.data;
                            if (resultData.length===0){
                                gradeName.push(roadGradeData[index]);
                                oldGradeSpeed.push(0);
                                // 取出对应等级的速度放入列表
                                realtimeGradeSpeed.push(nowGradeSpeedMap[roadGradeData[index]]);
                            }else {
                                for (let index in resultData) {
                                    // 将速度对应上
                                    gradeName.push(roadGradeData[resultData[index].level]);
                                    oldGradeSpeed.push(resultData[index].diffLevelSpeed.toFixed(3));
                                    realtimeGradeSpeed.push(nowGradeSpeedMap[roadGradeData[resultData[index].level]]);
                                }
                            }

                            this.drawGradeSpeed(gradeName, realtimeGradeSpeed, oldGradeSpeed);
                        }).catch(error => console.log(error, "history grade speed error...")); // 失败的返回
                    }


                }).catch(error => console.log(error, "realtime grade speed error...")); // 失败的返回

            },
            //今日实时速度和昨日对比
            drawGradeSpeed(xAxisData, seriesNowData, seriesOldData) {
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts
                    .init(document.getElementById("gradeSpeedBarChart"));
                // 指定图表的配置项和数据
                let option = {
                    title: {
                        text: "各等级道路平均交通速度"
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
                    legend: {
                        x: '15%',
                        top: '10%',
                        data: ['昨日速度', '今日速度'],
                    },
                    xAxis: {
                        type: 'category',
                        data: xAxisData,
                        axisLabel: {
                            formatter: function (value) {
                                return value.split("").join("\n")
                            }
                        },
                    },
                    yAxis: [
                        {
                            type: 'value',
                            // name: '昨日速度',
                            min: 0,
                            max: 150,
                            interval: 50,
                        },
                        {
                            type: 'value',
                            // name: '今日速度',
                            min: 0,
                            max: 150,
                            interval: 50,

                        }
                    ],
                    series: [
                        {
                            name: "今日速度",
                            type: "bar",
                            data: seriesNowData
                        },
                        {
                            name: "昨日速度",
                            type: "bar",
                            yAxisIndex: 1,
                            data: seriesOldData
                        }
                    ],
                };
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
                myChart.resize();
            },

            //获取实时各等级道路不同状态路段数量
            getRealTimeGradeStatusCount() {
                let gradeNames = [];
                // {"1": "快速路", "2": "主干路", "3": "次干路", "4": "支路"}
                let gradeRoadName = [];
                let tmpIndex = 1;
                let statusMap = {
                    '畅通': [],
                    '基本畅通': [],
                    '轻度拥堵': [],
                    '中度拥堵': [],
                    '严重拥堵': [],
                };
                for (let grade in roadGradeData) {

                    this.$axios.get("/realtime/grade/count", {
                        params: {
                            grade: grade
                        },
                    }).then(response => {
                        let resultData = response.data.data;
                        let tmpGradeName = "";

                        for (let index = 0; index <= resultData.length; index++) {

                            if (index === resultData.length) {
                                if (statusMap.畅通.length < tmpIndex) {
                                    statusMap.畅通.push(0)
                                }
                                if (statusMap.基本畅通.length < tmpIndex) {
                                    statusMap.基本畅通.push(0)
                                }
                                if (statusMap.轻度拥堵.length < tmpIndex) {
                                    statusMap.轻度拥堵.push(0)
                                }
                                if (statusMap.中度拥堵.length < tmpIndex) {
                                    statusMap.中度拥堵.push(0)
                                }
                                if (statusMap.严重拥堵.length < tmpIndex) {
                                    statusMap.严重拥堵.push(0)
                                }
                                tmpIndex++;
                                gradeRoadName.push(tmpGradeName);
                                break
                            }
                            if (resultData[index].roadStatus === "畅通") {
                                statusMap.畅通.push(resultData[index].diffLevelCountStatus)
                            }
                            if (resultData[index].roadStatus === "基本畅通") {
                                statusMap.基本畅通.push(resultData[index].diffLevelCountStatus)
                            }
                            if (resultData[index].roadStatus === "轻度拥堵") {
                                statusMap.轻度拥堵.push(resultData[index].diffLevelCountStatus)
                            }
                            if (resultData[index].roadStatus === "中度拥堵") {
                                statusMap.中度拥堵.push(resultData[index].diffLevelCountStatus)
                            }
                            if (resultData[index].roadStatus === "严重拥堵") {
                                statusMap.严重拥堵.push(resultData[index].diffLevelCountStatus)
                            }
                            tmpGradeName = roadGradeData[resultData[index].level]
                        }

                        // 共有4个等级道路，当临时长度等于4时为最后一次
                        if (tmpIndex - 1 === 4) {
                            let indicatorData = [];
                            let seriesData = [];


                            for (let index in statusMap) {

                                indicatorData.push(
                                    {
                                        name: index,
                                        max: 1
                                    }
                                );
                            }
                            // 求和，用来求占比
                            let sumFree = statusMap.畅通.reduce((prev, current, index, arr) => {
                                return prev + current
                            });

                            let sum_bf = statusMap.基本畅通.reduce((prev, current, index, arr) => {
                                return prev + current
                            });

                            let sum_lc = statusMap.轻度拥堵.reduce((prev, current, index, arr) => {
                                return prev + current
                            });

                            let sum_mc = statusMap.中度拥堵.reduce((prev, current, index, arr) => {
                                return prev + current
                            });

                            let sum_sc = statusMap.严重拥堵.reduce((prev, current, index, arr) => {
                                return prev + current
                            });

                            for (let n = 0; n < gradeRoadName.length; n++) {

                                let tmpValue = [];
                                if (sumFree !== 0) {
                                    tmpValue.push((statusMap.畅通[n] / sumFree).toFixed(2))
                                } else {
                                    tmpValue.push(0)
                                }
                                if (sum_bf !== 0) {
                                    tmpValue.push((statusMap.基本畅通[n] / sum_bf).toFixed(2))
                                } else {
                                    tmpValue.push(0)
                                }
                                if (sum_lc !== 0) {
                                    tmpValue.push((statusMap.轻度拥堵[n] / sum_lc).toFixed(2))
                                } else {
                                    tmpValue.push(0)
                                }
                                if (sum_mc !== 0) {
                                    tmpValue.push((statusMap.中度拥堵[n] / sum_mc).toFixed(2))
                                } else {
                                    tmpValue.push(0)
                                }
                                if (sum_sc !== 0) {
                                    tmpValue.push((statusMap.严重拥堵[n] / sum_sc).toFixed(2))
                                } else {
                                    tmpValue.push(0)
                                }

                                seriesData.push(
                                    {
                                        value: tmpValue,
                                        name: gradeRoadName[n]
                                    }
                                );
                            }
                            this.drawGradeRealTimeRadar(gradeRoadName, indicatorData, seriesData);
                        }

                    }).catch(error => console.log(error, "realtime grade status count error...")); // 失败的返回

                }


            },

            //道路等级雷达图
            drawGradeRealTimeRadar(gradeRoadName, indicatorData, seriesData) {
                let myChart = this.$echarts
                    .init(document.getElementById("gradRadar"));

                let option = {
                    title: {
                        text: '各等级道路交通状态占比'
                    },
                    tooltip: {
                        position: ['50%', '30%'],
                    },
                    legend: {
                        x: '0%',
                        top: '80%',
                        data: gradeRoadName
                    },
                    radar: {
                        indicator: indicatorData,
                        radius: 50
                    },
                    series: [{
                        type: 'radar',
                        data: seriesData
                    }]
                };

                myChart.setOption(option)
            },

            //获取当日和昨日速度数据
            getGradeHistorySpeedData(value) {

                // 获取当前时间戳
                let endTimestamp = Date.parse(new Date());
                let xAxisData = [];
                let seriesNowData = [];
                let seriesOldData = [];
                // 获取当天时间戳
                let startTimestamp = Date.parse(new Date(new Date().setHours(0, 0, 0, 0)));
                for (let i = startTimestamp; i < startTimestamp + 1000 * 60 * 60 * 24; i += 1000 * 60 * 15) {
                    let time = new Date(i);
                    xAxisData.push(time.getHours() + ":" + time.getMinutes())
                }
                //发送请求
                this.$axios.get("/history/grade/speed", {
                    params: {
                        startTime: startTimestamp,
                        endTime: endTimestamp,
                        grade: value
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
                                result.push((responseData[key].diffLevelSpeed + responseData[key - 1].diffLevelSpeed) / 2);
                            }
                        }
                        time_tmp = timeStamp;
                        let time = new Date(responseData[key].timestamp);
                        let sizeTime = time.getHours() + ":" + time.getMinutes();
                        result.push(responseData[key].diffLevelSpeed);
                    }
                    seriesNowData = result;
                    this.drawGradeDaySpeedChart(xAxisData, seriesNowData, seriesOldData)

                }).catch(error => console.log(error, "history grade speed error")); // 失败的返回

                //发送请求 --昨日
                this.$axios.get("/history/grade/speed", {
                    params: {
                        startTime: startTimestamp - 1000 * 60 * 60 * 24,
                        endTime: startTimestamp - 1000 * 60 * 15,
                        grade: value
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
                                result.push((responseData[key].diffLevelSpeed + responseData[key - 1].diffLevelSpeed) / 2);
                            }
                        }
                        time_tmp = timeStamp;
                        let sizeTime = time.getHours() + ":" + time.getMinutes();
                        result.push(responseData[key].diffLevelSpeed);
                    }
                    seriesOldData = result;
                    this.drawGradeDaySpeedChart(xAxisData, seriesNowData, seriesOldData)
                }).catch(error => console.log(error, "history day speed error")); // 失败的返回

            },

            // 绘制速度图
            drawGradeDaySpeedChart(xAxisData, seriesNowData, seriesOldData) {

                let myChart = this.$echarts
                    .init(document.getElementById("drawGradeDaySpeedChart"));

                let option = {
                    title: {
                        text: '各级道路昨日和今日平均速度对比图'
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
                        data: ['今日速度', '昨日速度'],
                    },
                    xAxis: {
                        data: xAxisData
                    },
                    yAxis: {},
                    series: [{
                        name: '今日速度',
                        type: 'line',
                        data: seriesNowData
                    }, {
                        name: "昨日速度",
                        type: 'line',
                        data: seriesOldData,
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

            // 获取今日不同等级道路综合交通指数
            getDayScoreGrade() {

                // 获取当前时间戳
                let endTimestamp = Date.parse(new Date());

                // 获取当天时间戳
                let startTimestamp = Date.parse(new Date(new Date().setHours(0, 0, 0, 0)));

                let provinces = [];
                let requestData = [];
                let paramData = [];
                let year = [];
                for (let index in roadGradeData) {
                    //发送请求
                    this.$axios.get("/history/grade/score", {
                        params: {
                            startTime: startTimestamp,
                            endTime: endTimestamp,
                            grade: index
                        },
                    }).then(response => {
                        let resultData = response.data.data;
                        let grade;
                        let dayGradeData = [];
                        for (let i in resultData) {

                            dayGradeData.push(resultData[i].scoreAvg);
                            if (i === "0") {
                                grade = resultData[i].level;
                                provinces.push(roadGradeData[grade]);
                            }
                            // 只放入某个等级道路的时间戳数据
                            if (grade === "1") {
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

                        requestData.push(dayGradeData);
                        //倒置矩阵【 【等级1 道路各时间段分数】。。。。。。】==>【【时间段1 各等级道路分数】】
                        paramData = this.reverseMatrix(requestData);
                        this.drawGradeScoreDayChart(paramData, provinces, year);

                    }).catch(error => console.log(error, "get Grade  Day Score error"))

                }
            },

            //绘制各级道路日综合交通指数变化
            drawGradeScoreDayChart(data, provinces, years) {
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts
                    .init(document.getElementById("gradeScoreDayChart"));
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
                                text: "各级道路综合交通指数    时间 " + all.years[i]
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
                                name: '综合指数'
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
                    },
                    timeline: {
                        axisType: 'category',
                        autoPlay: true,
                        playInterval: 2000,
                        data: all.years,
                    },
                    options: optionData
                };
                myChart.setOption(option);
                // myChart.autoResize()
            },

            // gradeStatusDayCount
            // /history/grade/count

            //获取当天不同等级道路交通状况数量
            getDayGradeStatusCount() {

                // 获取当前时间戳
                let endTimestamp = Date.parse(new Date());

                // 获取当天时间戳
                let startTimestamp = Date.parse(
                    new Date(new Date().setHours(0, 0, 0, 0))
                );
                let AllData = [];
                let level = "";
                for (let index in roadGradeData) {
                    //发送请求
                    this.$axios.get("/history/grade/count", {
                        params: {
                            startTime: startTimestamp,
                            endTime: endTimestamp,
                            grade: index
                        },
                    }).then(response => {

                        let resultData = response.data.data;

                        let tmp_time = resultData[0].timestamp;
                        level = resultData[0].level;
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
                            if (resultData[index].roadStatus === '畅通') {
                                tmp_statusMap.畅通.push(resultData[index].diffLevelCountStatus)
                            }
                            if (resultData[index].roadStatus === '基本畅通') {
                                tmp_statusMap.基本畅通.push(resultData[index].diffLevelCountStatus)
                            }
                            if (resultData[index].roadStatus === '轻度拥堵') {
                                tmp_statusMap.轻度拥堵.push(resultData[index].diffLevelCountStatus)
                            }
                            if (resultData[index].roadStatus === '中度拥堵') {
                                tmp_statusMap.中度拥堵.push(resultData[index].diffLevelCountStatus)
                            }
                            if (resultData[index].roadStatus === '严重拥堵') {
                                tmp_statusMap.严重拥堵.push(resultData[index].diffLevelCountStatus)
                            }
                        }

                        AllData.push(
                            {
                                grade: roadGradeData[index],
                                tmp_statusMap
                            }
                        );

                        //此时数据形式为[道路等级：不同时间不同情况数量列表[1,2,3........]]
                        this.getMess(
                            AllData,
                            startTimestamp,
                            endTimestamp);

                    }).catch(error => console.log(error, "get Day Grade Status Count error ..."))

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
                // 状态:【【等级1不同时间不同的数量】，【等级2不同时间不同的数量】。。。。。。】
                for (let index = 0; index < AllData.length; index++) {
                    if (Plist.indexOf(AllData[index].grade)) {
                        Plist.push(AllData[index].grade)
                    }
                    tmp_free.push(AllData[index].tmp_statusMap.畅通);
                    tmp_baseFree.push(AllData[index].tmp_statusMap.基本畅通);
                    tmp_lightCongestion.push(AllData[index].tmp_statusMap.轻度拥堵);
                    tmp_moderateCongestion.push(AllData[index].tmp_statusMap.中度拥堵);
                    tmp_seriousCongestion.push(AllData[index].tmp_statusMap.严重拥堵);
                }
                // 状态：【【时间1不同等级道路的状态数】【时间2不同等级道路的状态数】。。。。。。。】

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
                this.gradeDrawStatusDayCount(
                    freeParameter,
                    baseFreeParameter,
                    lightCongestionParameter,
                    moderateCongestionParameter,
                    seriousCongestionParameter,
                    Plist,
                    yearParameter
                );

            },

            // 绘制不同等级道路在当天各时段的数量
            gradeDrawStatusDayCount(
                freeParameter,
                baseFreeParameter,
                lightCongestionParameter,
                moderateCongestionParameter,
                seriousCongestionParameter,
                Plist,
                yearParameter
            ) {
                let myChart = this.$echarts
                    .init(document.getElementById("gradeStatusDayCount"));

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


                function getOptions() {
                    let resultData = [];
                    for (var index in yearParameter) {
                        let time = yearParameter[index];
                        resultData.push(
                            {
                                title: {text: "各等级道路不同交通状况数的数量   时间 " + time},
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
                            x: '40%',
                            top: '8%',
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
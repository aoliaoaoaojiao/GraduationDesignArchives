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
    let directionNames=["???->???","???->???","???->???","???->???"];
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

            // ???????????????????????????????????????
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
                                        name: '??????',
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

                }).catch(error => console.log(error, "get Real Time Direction Speed error")); // ???????????????
            },

            // ????????????????????????????????????
            drawRingDirectionSpeed(legendData, seriesData) {
                // ??????????????????dom????????????echarts??????
                let myChart = this.$echarts
                    .init(document.getElementById("realTimeDirectionSpeed"));
                let option = {
                    tooltip : {
                        show: true,
                    },
                    title: {
                        text: "???????????????????????????"
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

            // ??????????????????????????????
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
                }).catch(error => console.log(error, "get Real Time Direction Score error")); // ???????????????

            },

            //  ???????????????????????????
            drawScoreDirectionBar(XAxisData, seriesData) {
                // ??????????????????dom????????????echarts??????
                let myChart = this.$echarts
                    .init(document.getElementById("directionRealTimeScore"));
                // ?????????????????????????????????
                let option = {
                    title: {
                        text: "???????????????????????????"
                    },
                    tooltip: {
                        position: ['20%', '30%'],
                        trigger: 'item',
                    },
                    xAxis: {
                        data: XAxisData,
                        axisLabel: {
                            formatter: function (value) {
                                return value.split("")[0] + "\n???\n" + value.split("")[3]
                            }
                        },
                    },
                    yAxis: {},
                    series: [
                        {
                            name: "??????",
                            type: "bar",
                            data: seriesData
                        }
                    ],
                    grid: { // ??????????????????????????????????????????????????????
                        x: 50,
                        x2: 25,
                        y2: 100,// y2???????????? X??????Zoom??????????????????????????????????????????????????? label?????????zoom???
                    },
                };
                // ???????????????????????????????????????????????????
                myChart.setOption(option);
                myChart.resize();
            },

            // ???????????????????????????????????????
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
                    }).catch(error => console.log(error, "get Direction Status Count error")); // ???????????????
                }

            },

            // ???????????????
            drawSunburstPieChart(seriesData) {
                // ??????????????????dom????????????echarts??????
                let myChart = this.$echarts
                    .init(document.getElementById("directionRealTimeCount"));
                var option = {
                    title: {
                        text: "?????????????????????????????????"
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

            // ?????????????????????????????????
            getDirectionDaySpeedData() {
                // ?????????????????????
                let endTimestamp = Date.parse(new Date());

                // ?????????????????????
                let startTimestamp = Date.parse(new Date(new Date().setHours(0, 0, 0, 0)));
                // option??????
                let SeriesData=[];
                // ?????????
                let speedTimeXData=[];
                // ?????????
                let legendDirectionData=[];
                for (let index in directionNames) {
                    //????????????
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
                            if (direction === "???->???") {
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

                    }).catch(error => console.log(error, "get Direction Day Speed error")); // ???????????????
                }
                // console.log(this.daySpeedSeriesData);
            },

            // ??????????????????????????????????????????
            drawDirectionDaySpeed(legendDirectionData,speedTimeXData,SeriesData){
                // ??????????????????dom????????????echarts??????
                let myChart = this.$echarts
                    .init(document.getElementById("directionDaySpeedChart"));
                // ?????????????????????????????????
                let option = {
                    title: {
                        left: 'center',
                        text: '?????????????????????????????????',
                        subtext: '???????????????????????????????????????'
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

            //????????????
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

            // ???????????????????????????????????????
            getDayScoreDirection() {

                // ?????????????????????
                let endTimestamp = Date.parse(new Date());

                // ?????????????????????
                let startTimestamp = Date.parse(new Date(new Date().setHours(0, 0, 0, 0)));

                let directions = [];
                let requestData = [];
                let paramData = [];
                let year = [];
                for (let index in directionNames) {
                    //????????????
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
                            if (direction === "???->???") {
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

            //???????????????????????????????????????
            drawScoreDayChart(data, direction, years) {
                // ??????????????????dom????????????echarts??????
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
                                text: "????????????????????????????????????    ?????? " + all.years[i]
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
                                name: '??????'
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
                        text: "????????????????????????????????????",
                        subtext: '???????????????????????????????????????'
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

            //??????????????????????????????????????????
            getDayDirectionStatusCount() {

                // ?????????????????????
                let endTimestamp = Date.parse(new Date());

                // ?????????????????????
                let startTimestamp = Date.parse(
                    new Date(new Date().setHours(0, 0, 0, 0))
                );
                let AllData = [];
                let direction = "";
                for (let index in directionNames) {
                    //????????????
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
                            '??????': [],
                            '????????????': [],
                            '????????????': [],
                            '????????????': [],
                            '????????????': [],
                        };
                        for (let index = 0; index <= resultData.length; index++) {
                            // ?????????????????????
                            if (index === resultData.length || resultData[index].timestamp !== tmp_time) {
                                if (tmp_statusMap.??????.length < tmp_array_length) {
                                    tmp_statusMap.??????.push(0)
                                }
                                if (tmp_statusMap.????????????.length < tmp_array_length) {
                                    tmp_statusMap.????????????.push(0)
                                }
                                if (tmp_statusMap.????????????.length < tmp_array_length) {
                                    tmp_statusMap.????????????.push(0)
                                }
                                if (tmp_statusMap.????????????.length < tmp_array_length) {
                                    tmp_statusMap.????????????.push(0)
                                }
                                if (tmp_statusMap.????????????.length < tmp_array_length) {
                                    tmp_statusMap.????????????.push(0)
                                }
                                if (index === resultData.length) {
                                    break;
                                }
                                tmp_array_length++;
                                tmp_time = resultData[index].timestamp;
                            }
                            if (resultData[index].status === '??????') {
                                tmp_statusMap.??????.push(resultData[index].countStatus)
                            }
                            if (resultData[index].status === '????????????') {
                                tmp_statusMap.????????????.push(resultData[index].countStatus)
                            }
                            if (resultData[index].status === '????????????') {
                                tmp_statusMap.????????????.push(resultData[index].countStatus)
                            }
                            if (resultData[index].status === '????????????') {
                                tmp_statusMap.????????????.push(resultData[index].countStatus)
                            }
                            if (resultData[index].status === '????????????') {
                                tmp_statusMap.????????????.push(resultData[index].countStatus)
                            }
                        }

                        AllData.push(
                            {
                                direction: direction,
                                tmp_statusMap
                            }
                        );

                        //?????????????????????[???????????????????????????????????????????????????[1,2,3........]]
                        this.getMess(
                            AllData,
                            startTimestamp,
                            endTimestamp);

                    }).catch(error => console.log(error, "get Day Direction Status Count error"))

                }

            },

            // ??????????????????
            getMess(AllData,
                    startTimestamp) {
                let tmp_free = [];
                let tmp_baseFree = [];
                let tmp_lightCongestion = [];
                let tmp_moderateCongestion = [];
                let tmp_seriousCongestion = [];
                let Plist = [];
                // ??????:????????????1??????????????????????????????????????????2???????????????????????????????????????????????????
                for (let index = 0; index < AllData.length; index++) {
                    if (Plist.indexOf(AllData[index].direction)) {
                        Plist.push(AllData[index].direction)
                    }
                    tmp_free.push(AllData[index].tmp_statusMap.??????);
                    tmp_baseFree.push(AllData[index].tmp_statusMap.????????????);
                    tmp_lightCongestion.push(AllData[index].tmp_statusMap.????????????);
                    tmp_moderateCongestion.push(AllData[index].tmp_statusMap.????????????);
                    tmp_seriousCongestion.push(AllData[index].tmp_statusMap.????????????);
                }
                // ?????????????????????1????????????????????????????????????2???????????????????????????????????????????????????

                let freeParameter = {};
                let baseFreeParameter = {};
                let lightCongestionParameter = {};
                let moderateCongestionParameter = {};
                let seriousCongestionParameter = {};
                let yearParameter = [];
                //????????????
                let freeParameter_tmp = this.reverseMatrix(tmp_free);
                let baseFreeParameter_tmp = this.reverseMatrix(tmp_baseFree);
                let lightCongestionParameter_tmp = this.reverseMatrix(tmp_lightCongestion);
                let moderateCongestionParameter_tmp = this.reverseMatrix(tmp_moderateCongestion);
                let seriousCongestionParameter_tmp = this.reverseMatrix(tmp_seriousCongestion);

                // ??????????????????????????????????????????{timekey : [??????1???????????????2???????????????3????????????????????????]}
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

            // ?????????????????????????????????????????????
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
                    // ['??????', '??????', '??????', '??????', '?????????', '??????', '??????', '?????????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????', '??????'];
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
                                title: {text: "??????????????????????????????????????????   ?????? " + time},
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
                            subtext: '???????????????????????????????????????'
                        },
                        tooltip: {},
                        legend: {
                            x: 'right',
                            data: ['??????', '????????????', '????????????', '????????????', '????????????'],
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
                                name: '??????',
                                // max: 400
                            }
                        ],
                        series: [
                            {name: '??????', type: 'bar'},
                            {name: '????????????', type: 'bar'},
                            {name: '????????????', type: 'bar'},
                            {name: '????????????', type: 'bar'},
                            {name: '????????????', type: 'bar'},
                            {
                                name: '????????????',
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
<template>
    <div>
        <el-container style="height: 100%; border: 1px solid #eee">
            <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
                <el-menu :unique-opened="true" default-active="roadAllData" @select="selectMenu" @close="">
                    <el-menu-item index="roadAllData" @click="">
                        <template slot="title">
                            <i class="el-icon-tickets"></i>实时道路数据
                        </template>
                    </el-menu-item>
                    <el-menu-item index="realtime" @click="">
                        <template slot="title">
                            <i class="el-icon-timer"></i>实时道路画像
                        </template>
                    </el-menu-item>
                    <el-menu-item index="historyRoad" @click="">
                        <template slot="title">
                            <i class="el-icon-time"></i>历史道路画像
                        </template>
                    </el-menu-item>
                </el-menu>
            </el-aside>


            <el-container>
                <el-main>
                    <div id="select" v-if="status!=='roadAllData'">
                        <div class="search selectRoad" style="margin-left: auto;" @submit.prevent="selectRoadSubmit">
                            <form>
                                <input
                                        v-model="selectValue"
                                        type="text"
                                        placeholder="请输入道路名"
                                        name="crid"

                                        @keyup.enter="selectRoadSubmit">
                                <button type="button" @click="selectRoadSubmit"></button>
                            </form>
                        </div>
                    </div>

                    <el-table

                            v-loading="tableLoading"
                            :data="tableData"
                            fixed
                            border
                            style="width: 100%;">

                        <el-table-column prop="name" label="道路名" style="width: 30%;height: 20%">
                        </el-table-column>
                        <el-table-column prop="speedAvg" label="平均速度(km/h)" style="width: 30%;height: 20%">
                            <template slot-scope="scope">
                                <span>{{scope.row.speedAvg | rounding}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="tpi" label="交通运行指数" style="height: 20%">
                            <template slot-scope="scope">
                                <span>{{scope.row.tpi | rounding}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="dtp" label="交通延时指数" style="height: 20%">
                            <template slot-scope="scope">
                                <span>{{scope.row.dtp | rounding}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="roadStatus" label="交通状况" style="height: 20%">
                        </el-table-column>
                        <el-table-column v-if="status==='historyRoad'" prop="time" label="日期" style="height: 20%">
                        </el-table-column>

                    </el-table>

                    <div class="block">
                        <el-pagination
                                background
                                :hide-on-single-page="true"
                                :page-size="pageSize"
                                @current-change="handleCurrentChange"
                                :current-page.sync="currentPage"
                                layout="prev, pager, next, jumper"
                                :total="total">
                        </el-pagination>
                    </div>
                    <div id="drawRoadStatus" v-if="status==='realtime'" style="width: auto;height: 500px">
                    </div>
                    <div id="drawRoadTPI" v-if="status==='historyRoad'" style="width: auto;height: 500px">
                    </div>
                    <div id="drawRoadDTP" v-if="status==='historyRoad'" style="width: auto;height: 500px">
                    </div>
                    <div id="drawRoadSpeed" v-if="status==='historyRoad'" style="width: auto;height: 500px">
                    </div>
                </el-main>

            </el-container>
        </el-container>
    </div>
</template>

<script>
    let clearId= ["drawRoadStatus","drawRoadTPI","drawRoadDTP","drawRoadSpeed"];
    export default {
        name: "RoadSearch",
        data() {
            return {
                testMessage: "",
                tableData: [],
                selectValue: "",
                selectIndex: "roadAllData",
                currentPage: 1,
                total: 0,
                pageSize: 10,
                tableLoading: true,
                status: "roadAllData",
            }
        },
        // 过滤器
        filters: {
            rounding (value) {
                return value.toFixed(3)
            }
        },
        mounted() {
            // this.drawRealTimeRadar();
        },
        created() {
            // 初始化数据
            this.getRealTimeAllRoadData(1);
        },
        methods: {
            // 菜单选择
            selectMenu(value) {
                this.currentPage=1;
                this.selectValue = "";
                this.status = value;
                for (let index in clearId){
                    try {
                        let myChart = this.$echarts
                            .init(document.getElementById(clearId[index]));
                        myChart.clear();
                        myChart.dispose();
                    }catch (e) {

                    }

                }

                if (value === "roadAllData") {
                    this.getRealTimeAllRoadData(1);
                } else if (value === "realtime") {
                    this.total = 1;
                    this.tableData = [];
                } else {
                    // document.getElementById("drawRoadStatus").innerHTML="";
                    this.total = 1;
                    this.tableData = [];
                }
            },

            //  获取所有道路数据
            getRealTimeAllRoadData(pageNum) {
                this.tableLoading = true;
                this.$axios.get("/realtime/road/overall", {
                    params: {
                        pageNum: pageNum,
                        pageSize: this.pageSize
                    }
                }).then(response => {
                    this.tableData = response.data.list;
                    // 获取总数
                    this.total = response.data.total;
                    this.tableLoading = false;

                }).catch(error => console.log(error, "get RealTime All Road Data error")); // 失败的返回
            },

            //  搜索单个道路
            selectRoadSubmit() {
                //如果是查询全部数据
                if (this.status === "roadAllData") {
                    this.tableLoading = true;
                    this.$axios.get("/realtime/road/single", {
                        params: {
                            name: this.selectValue
                        }
                    }).then(response => {
                        this.tableData = response.data.data;
                        // 获取总数
                        this.total = 0;
                        this.tableLoading = false;
                    }).catch(error => console.log(error, "select Road Data Status roadAllData error")); // 失败的返回
                }

                // 如果是查询当前状态
                else if (this.status === "realtime") {
                    this.tableLoading = true;

                    this.$axios.get("/realtime/road/single", {
                        params: {
                            name: this.selectValue
                        }
                    }).then(response => {
                        this.tableData = response.data.data;

                        let seriesDataVale=[response.data.data[0].speedAvg,response.data.data[0].dtp,response.data.data[0].tpi];
                        // 获取总数
                        this.total = 0;

                        this.drawRealTimeRadar(seriesDataVale);

                        this.tableLoading = false;
                    }).catch(error => console.log(error, "select Road Data Status roadAllData error")); // 失败的返回
                }
                // 历史状态
                else {
                    this.getHistoryRoadStatus(1);
                }
            },

            // 获取历史道路状态数据
            getHistoryRoadStatus(pageNum){
                this.tableLoading = true;

                // 获取当前时间戳
                let endTimestamp = Date.parse(new Date());

                // 获取开始时间戳
                let startTimestamp = Date.parse(new Date(new Date().setHours(0, 0, 0, 0)))-1000*60*60*24*6;

                let SpeedData = [];

                let TPIData = [];

                let DTPData = [];



                this.$axios.get("history/road/status", {
                    params: {
                        name: this.selectValue,
                        startTime:startTimestamp,
                        endTime:endTimestamp,
                        pageNum: pageNum,
                        pageSize: this.pageSize
                    }
                }).then(response => {
                    this.tableData = response.data.list;
                    // 获取总数
                    this.total = response.data.total;

                    this.tableLoading = false;
                }).catch(error => console.log(error, "select Road History Status Data  error")); // 失败的返回

                // /history/road/tpi
                this.$axios.get("history/road/tpi", {
                    params: {
                        name: this.selectValue,
                        startTime:startTimestamp,
                        endTime:endTimestamp,
                    }
                }).then(response => {
                    let resultData = response.data.data;
                    let xAxisData = [];
                    for (let index in resultData){
                        TPIData.push(resultData[index].tpi);
                        let time = new Date(resultData[index].timestamp);
                        let timeFormat = (time.getMonth()+1)+"月"+ time.getDate()+"日    "+time.getHours()+":"+time.getMinutes();
                        xAxisData.push(timeFormat)
                    }
                    this.drawPolylineChart(xAxisData,TPIData,this.selectValue+"一周内TPI变化","drawRoadTPI");

                }).catch(error => console.log(error, "select Road History tpi Data  error")); // 失败的返回


                // /history/road/dtp
                this.$axios.get("history/road/dtp", {
                    params: {
                        name: this.selectValue,
                        startTime:startTimestamp,
                        endTime:endTimestamp,
                    }
                }).then(response => {
                    let resultData = response.data.data;
                    let xAxisData = [];
                    for (let index in resultData){
                        DTPData.push(resultData[index].dtp);
                        let time = new Date(resultData[index].timestamp);
                        let timeFormat = (time.getMonth()+1)+"月"+ time.getDate()+"日    "+time.getHours()+":"+time.getMinutes();
                        xAxisData.push(timeFormat)
                    }
                    this.drawPolylineChart(xAxisData,DTPData,this.selectValue+"一周内DTP变化","drawRoadDTP");

                }).catch(error => console.log(error, "select Road History dtp Data  error")); // 失败的返回


                // /history/road/speed
                this.$axios.get("history/road/speed", {
                    params: {
                        name: this.selectValue,
                        startTime:startTimestamp,
                        endTime:endTimestamp,
                    }
                }).then(response => {
                    let resultData = response.data.data;
                    let xAxisData = [];
                    for (let index in resultData){
                        SpeedData.push(resultData[index].speedAvg);
                        let time = new Date(resultData[index].timestamp);
                        let timeFormat = (time.getMonth()+1)+"月"+ time.getDate()+"日    "+time.getHours()+":"+time.getMinutes();
                        xAxisData.push(timeFormat)
                    }
                    this.drawPolylineChart(xAxisData,SpeedData,this.selectValue+"一周内速度变化","drawRoadSpeed");

                }).catch(error => console.log(error, "select Road History speed Data  error")); // 失败的返回

            },


            //  变更页数
            handleCurrentChange(val) {

                if (this.status === "roadAllData"){
                    this.getRealTimeAllRoadData(val);
                }else {
                    this.getHistoryRoadStatus(val);
                }

            },


            // 绘制实时雷达图
            drawRealTimeRadar(seriesDataVale) {
                let myChart = this.$echarts
                    .init(document.getElementById("drawRoadStatus"));
                myChart.clear();
                let option = {
                    title: {
                        text: '道路实时指数雷达图',
                        x: 'center',
                    },
                    tooltip: {},
                    radar: {
                        // shape: 'circle',
                        indicator: [
                            {name: '速度', max: 120},
                            {name: '交通延时指数', max: 1},
                            {name: '交通运行指数', max: 10},

                        ]
                    },
                    series:
                        [{
                            name: '交通指数',
                            type: 'radar',
                            // areaStyle: {normal: {}},
                            itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: [
                                {
                                    value: seriesDataVale,
                                        // [45, 90, 90],
                                    name: '相关指数',
                                },
                            ]
                        }]
                };
                myChart.setOption(option);
            },

            // 折线图
            drawPolylineChart(xAxisData,seriesData,title,id) {
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts
                    .init(document.getElementById(id));
                myChart.clear();
                // 指定图表的配置项和数据
                let option = {
                    title: {
                        left: 'center',
                        text: title,
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
                    xAxis: {
                        data: xAxisData,
                    },
                    yAxis: {},
                    series:{
                        name: title,
                        type: 'line',
                        data: seriesData
                    }


                };
                myChart.setOption(option);
                // myChart.autoResize()
            },
        }
    }
</script>

<style scoped>
    * {
        box-sizing: border-box;
    }

    #select {
        width: auto;
        height: auto;
        margin-left: auto;
    }

    div.search {
        padding: 10px 0;
    }

    form {
        position: relative;
        width: 20px;
        height: 32px;
        margin-left: auto;
    }

    input, button {
        border: none;
        outline: none;
    }

    input {
        width: 50px;
        height: 32px;
        padding-left: 70%;
    }

    button {
        height: 32px;
        width: 42px;
        cursor: pointer;
        position: absolute;
    }

    /*搜索框*/
    .selectRoad form {
        height: 32px;
    }

    .selectRoad input {
        width: 200px;
        border-radius: 42px;
        border: 2px solid #324B4E;
        background: #ffffff;
        transition: .3s linear;
        float: right;
    }

    .selectRoad input:focus {
        width: 300px;
    }

    .selectRoad button {
        background: none;
        top: -2px;
        right: 20px;
    }

    .selectRoad button:before {
        content: "Search";
        color: #324b4e;
    }

</style>
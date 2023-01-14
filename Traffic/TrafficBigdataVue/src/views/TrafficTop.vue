<template>
    <div >
        <el-container style="height: 700px; border: 1px solid #eee" >
            <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
                <el-menu :unique-opened="true" default-active="city" @open="getOpen" @select="getRouter" @close="">
                    <el-submenu index="adCode" >
                        <template slot="title"><i class="el-icon-location"></i>各地区</template>
                        <el-menu-item  v-for="(item) in adCodeMaps" :index="item.adCode">
                            {{item.mapTo}}
                        </el-menu-item>
                    </el-submenu>
                    <el-submenu index="direction">
                        <template slot="title">
                            <i class="el-icon-discover"></i>各方向
                        </template>
                        <el-menu-item  v-for="(item) in directionMaps" :index="item.direction">
                            {{item.direction}}
                        </el-menu-item>
                    </el-submenu>
                    <el-submenu index="grade">
                        <template slot="title">
                            <i class="el-icon-star-off"></i>各等级</template>
                        <el-menu-item  v-for="(item) in gradeMaps" :index="item.grade">
                            {{item.roadType}}
                        </el-menu-item>
                    </el-submenu>
                    <el-menu-item index="city" @click="getCityTopData">
                        <template slot="title">
                            <i class="el-icon-map-location" ></i>全市总体</template>
                    </el-menu-item>
                </el-menu>
            </el-aside>
            <el-container>

                <el-main>
                    <div class="block">
                        <span class="demonstration">TOP</span>
                        <el-slider
                                v-model="value"
                                :max="40"
                                @change = "operaSlider"
                        >
                        </el-slider>
                    </div>
                    <el-table :data="tableData">
                        <el-table-column prop="index" type="index"  label="排名">
                        </el-table-column>
                        <el-table-column prop="name" label="道路">
                        </el-table-column>
                        <el-table-column prop="specificInformation" label="路段" min-width="150">
                        </el-table-column>
                        <el-table-column prop="speed" label="速度" min-width="40">
                        </el-table-column>
                        <el-table-column prop="tpi" label="交通运行指数">
                        </el-table-column>
                        <el-table-column prop="dtp" label="交通延时指数">
                        </el-table-column>
                        <el-table-column prop="status" label="交通状况">
                        </el-table-column>
                    </el-table>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "trafficTOP",
        created() {
            //初始化数据
            this.index="city";
            this.getCityTopData()
        },
        data() {
            return {
                // 获取行政区编号对应之行政区
                adCodeMaps:require('../assets/AdCodeMap.json').data,
                // 各方向
                directionMaps:require('../assets/DirectionMap.json').data,
                // 各等级道路
                gradeMaps:require('../assets/GradeMap.json').data,
                tableData: [],
                value:10,
                index:"city",
                path:""
            }
        },methods:{
            //  获取城市总体TOP
            getCityTopData(){
                this.index="city";
                this.$axios({
                    method: "get",
                    url: "/top/city?num="+this.value, // 接口地址
                }).then(response => {
                    this.tableData = response.data.data;   // 成功的返回
                }).catch(error => console.log(error, "get City Top Data error")); // 失败的返回
            },
            //  获取当前导航栏位置
            getRouter(path){
                this.path = path;
                //  不同条件获取不同信息
                if (path!=null && path!=="" && path!=="city"){
                    if (this.index==="adCode"){
                        this.getOtherData("/top/region?num","adCode")
                    }else if (this.index==="direction"){
                        this.getOtherData("/top/direction?num","direction")
                    }else if (this.index==="grade"){
                        this.getOtherData("/top/grade?num","grade")
                    }
                }else {
                    //  未选菜单清空
                    this.path="";
                    this.tableData=[]
                }
            },
            //  获取打开的一级菜单
            getOpen(index){
                if (index!=null && index!=="" && index==="city"){
                    this.index = index;
                    this.getCityTopData();
                }else {
                    this.index=index;
                    this.tableData=[]
                }
            },
            //  获取其他分组TOP数据
            getOtherData(baseUrl,options){
                this.$axios({
                    method: "get",
                    url: baseUrl+"="+this.value+"&"+options+"="+this.path, // 接口地址
                }).then(response => {
                    this.tableData = response.data.data;   // 成功的返回
                }).catch(error => console.log(error, "get Other Data error")); // 失败的返回
            },
            //  滑动滑块相关操作
            operaSlider(){
                if (this.index==="city"){
                    this.getCityTopData();
                }else if (this.index==="adCode"){
                    this.getOtherData("/top/region?num","adCode");
                }else if (this.index==="direction"){
                    this.getOtherData("/top/direction?num","direction")
                }else if (this.index==="grade"){
                    this.getOtherData("/top/grade?num","grade")
                }
                else {
                    this.tableData=[]
                }
            }
        }
    }
</script>

<style scoped>

</style>
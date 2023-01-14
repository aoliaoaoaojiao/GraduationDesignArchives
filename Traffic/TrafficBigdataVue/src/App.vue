<template>
    <div>
        <el-menu :default-active="this.$route.path" router
                 class="el-menu-demo"
                 mode="horizontal"
                 @select="handleSelect"
                 background-color="#292c2d"
                 text-color="#fff"
                 active-text-color="#ffd04b"
        >
            <el-menu-item v-for="(item,i) in this.navList" :key="i" :index="item.name" >
                <template slot="title">
                    <span >{{ item.navItem }}</span>
                </template>
            </el-menu-item>

        </el-menu>
        <router-view></router-view>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                navList:[
                    {name:'/',navItem:'城市总体交通状况'},
                    {name:'/region',navItem:'各区交通状况'},
                    {name:'/grade',navItem:'各级道路交通状况'},
                    {name:'/direction',navItem:'各方向交通状况'},
                    {name:'/road',navItem:'道路交通信息'},
                    {name:'/TOP',navItem:'拥堵TOP'},
                    {name:'/heatmap',navItem:'交通热力图'},

                ]
            };
        },
        methods: {
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
            }
        }
    }
</script>


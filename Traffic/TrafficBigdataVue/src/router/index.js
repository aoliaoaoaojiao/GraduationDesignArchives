import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter);

const routes = [
    {
        path: '/',
        name: 'HomeIndex',
        component: () => import('../views/HomeIndex.vue')
    },
    {
        path: '/heatmap',
        name: 'HeatMap',
        component: () => import('../views/RealTimeHeatMap.vue')
    },
    {
        path: '/region',
        name: 'RegionTraffic',
        component: () => import('../views/RegionTraffic.vue')
    },
    {
        path: '/grade',
        name: 'GradeTraffic',
        component: () => import('../views/GradeTraffic')
    },
    {
        path: '/direction',
        name: 'DirectionTraffic',
        component: () => import('../views/DirectionTraffic')
    },
    {
        path: '/TOP',
        name: 'trafficTOP',
        component: () => import('../views/TrafficTop')
    }, {
        path: '/road',
        name: 'roade',
        component: () => import('../views/RoadTraffic')
    }
];

const router = new VueRouter({
    mode: 'history',
    routes
});

export default router

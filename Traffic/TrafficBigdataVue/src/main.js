import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
//引入echars
import echarts from "echarts";
import axios from 'axios';

axios.defaults.baseURL="http://127.0.0.1:8018/api/";
Vue.prototype.$axios = axios;
Vue.prototype.$echarts = echarts;

import BaiduMap from 'vue-baidu-map'

Vue.use(BaiduMap, {
  // ak 是在百度地图开发者平台申请的密钥 详见 http://lbsyun.baidu.com/apiconsole/key */
  ak: 'Cz4UXoDozUtL1GBPrj8rVjuMEBzSHPjk'
});

Vue.use(ElementUI);

Vue.config.productionTip = false;


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');

import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import store from './store'
import '@/permission'
import md5 from 'js-md5'
import BaiduMap from 'vue-baidu-map'

Vue.config.productionTip = false;
const options = {
  namespace: 'pro__',
  name: 'ls',
  storage: 'local'
};
Vue.use(Antd);

Vue.directive('title', {
  inserted: function (el, binding) {
    document.title = el.dataset.title
  }
});
/*md5密码加密*/
Vue.prototype.$md5=md5;
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');

Vue.use(BaiduMap, {
  // ak 是在百度地图开发者平台申请的密钥 详见 http://lbsyun.baidu.com/apiconsole/key */
  ak: '20GQXz2psIWxeRZUFXUrdR8qXc998HAO'
});

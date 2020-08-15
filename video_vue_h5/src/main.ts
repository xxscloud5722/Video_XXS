import './api';
import 'vant/lib/index.css';

import Vue from 'vue';

import Vant, {
  ImagePreview,
  Lazyload,
} from 'vant';
import Vconsole from 'vconsole';
import VueRouter from 'vue-router';

// 全局注册
import App from './app.vue';
import { EventBus } from './event-bus';
import router from './router/router';
import Utils from './utils/utils';

declare const window;
declare const document;

if (window.C.VUE_ENV === 'DEV') {
    const vConsole = new Vconsole();
    console.log(vConsole);

    console.log('高度: ' + document.body.clientHeight);
    console.log('宽度: ' + document.body.clientWidth);

    console.log('位彩色: ' + window.screen.colorDepth);
    console.log('像素/英寸: ' + window.screen.deviceXDPI);
    console.log('屏幕可用工作区高度: ' + window.screen.availHeight);
    console.log('屏幕可用工作区宽度: ' + window.screen.availWidth);
    console.log('屏幕高度: ' + window.screen.height);
    console.log('屏幕宽度: ' + window.screen.width);
    console.log('分辨率: ' + window.screen.width);
}


// 默认登陆
window.userInfo = Utils.getItem('userInfo');


Vue.use(VueRouter);
Vue.use(ImagePreview);
Vue.use(Lazyload, {});
Vue.use(Vant);
Vue.prototype.$eventBus = EventBus;
// tslint:disable-next-line:no-unused-expression
new Vue({
    el: '#app',
    router,
    render: h => h(App),
});

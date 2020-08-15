import VueRouter, {Route} from 'vue-router';

declare const document;

const router = new VueRouter({
    mode: 'history',
    routes: [
        {
            path: '/',
            redirect: '/video/index',
        },
        {
            path: '/video/*',
            component: () => import('../video.module/video.vue'),
            children: [
                {
                    path: '/video/index',
                    component: () => import('../video.module/video-index.vue'),
                    meta: {
                        title: '哔哩哔哩 (゜-゜)つロ 干杯~-bilibili',
                    }
                },
                {
                    path: '/video/detail/:id',
                    component: () => import('../video.module/video-detail.vue'),
                    meta: {
                        title: '哔哩哔哩 (゜-゜)つロ 干杯~-bilibili',
                    }
                },
                {
                    path: '/video/user/:id',
                    component: () => import('../video.module/video-user.vue'),
                    meta: {
                        title: '哔哩哔哩 (゜-゜)つロ 干杯~-bilibili',
                    }
                },
                {
                    path: '/video/login',
                    component: () => import('../video.module/video-login.vue'),
                    meta: {
                        title: '登陆_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili',
                    }
                },
                {
                    path: '/video/pay',
                    component: () => import('../video.module/video-pay.vue'),
                    meta: {
                        title: '收银台_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili',
                    }
                }
            ]
        }


    ],
});
router.beforeEach((to: Route, from: Route, next) => {
    // 设置头部
    if (from != null && to.meta.title) {
        document.title = to.meta.title;
    }
    next();
});
export default router;

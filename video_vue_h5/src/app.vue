<template>
    <div class="app">
        <router-view/>
    </div>
</template>

<style lang="scss">
    @import "static/font/iconfont.css";

    //定义字体
    @font-face {
        font-family: "Avenir-Heavy";
        src: url("./static/Avenir Heavy.otf");
    }

    * {
        -moz-user-select: none;
        -webkit-user-select: none;
        -ms-user-select: none;
        user-select: none;
        -webkit-touch-callout: none;
    }

    input,
    textarea {
        -moz-user-select: auto;
        -webkit-user-select: auto;
        -ms-user-select: auto;
        user-select: auto;
    }

    body,
    html,
    .app {
        padding: 0;
        margin: 0;
        background: #fff;
    }

    body,
    h1,
    h2,
    h3,
    h4,
    h5,
    h6,
    div {
        font-family: PingFangSC-Regular;
        //font-family: -apple-system, Helvetica, sans-serif;
        font-size: 0.12rem;
    }

    //如果屏幕大于
    /*@media screen and (min-width: 600px) {*/
    /*    .app:before {*/
    /*        content: "请使用手机浏览器访问";*/
    /*        padding: 1rem 0.5rem;*/
    /*        font-size: 0.2rem;*/
    /*        position: fixed;*/
    /*        z-index: 9999;*/
    /*        color: #6f7275;*/
    /*        background: #fff;*/
    /*        left: 0;*/
    /*        right: 0;*/
    /*        top: 0;*/
    /*        bottom: 0;*/
    /*        text-align: center;*/
    /*    }*/
    /*}*/

    .van-switch--on {
        .van-switch__node {
            margin-left: 0.95em !important;
        }
    }

    .van-button--normal {
        font-size: 0.14rem !important;
    }

    .user-share {
        .van-cell {
            padding: 0.05rem 0.11rem !important;
        }

        .van-button {
            height: 0.35rem !important;
            line-height: 0.35rem !important;
        }
    }

    .van-switch__node {
        width: 1.7em !important;
        height: 1.7em !important;
    }

    .digital {
        font-family: Avenir-Heavy, PingFang SC, Helvetica Neue, Arial, sans-serif;
    }

    .app-loading {
        width: 0.8rem;
        height: 0.8rem;
        position: absolute;
        left: 50%;
        top: 50%;
        margin: -0.5rem;
    }

    .van-dialog {
        border-radius: 0.02rem !important;

        & .van-dialog__header {
            padding-top: 24px;
            font-size: 0.16rem;
            font-weight: 400;
        }

        & .van-dialog__message {
            color: #666;
            font-size: 0.13rem;
        }

        & .van-dialog__confirm {
            color: #e9ac65 !important;
        }
    }

    .color-pink {
        color: #eb2f96;
    }

    .color-red {
        color: #f5222d;
    }

    .color-orange {
        color: #fa8c16;
    }

    .color-green {
        color: #52c41a;
    }

    .color-cyan {
        color: #13c2c2 !important;
    }

    .color-blue {
        color: #1890ff !important;
    }

    .color-purple {
        color: #722ed1 !important;
    }



</style>

<script lang="ts">
    import Vue from "vue";
    import Component from "vue-class-component";
    import Utils from "./utils/utils";

    @Component
    export default class App extends Vue {
        public created() {
        }

        public async mounted() {
            const code = Utils.getParam("inviteCode");
            if (code == null || code.trim().length <= 0) {
                return;
            }
            Utils.setSessionItem("inviteCode", code);
        }
    }
</script>
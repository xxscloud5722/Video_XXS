<template>
    <div class="video">
        <div class="header">
            <div class="logo">
                <img src="../static/logo.jpg" alt="" @click="onHome">
            </div>
            <div class="filling"></div>
            <!--            <div class="search">-->
            <!--                <i class="iconfont icon-sousuo"></i>-->
            <!--            </div>-->
            <div class="user" @click="onToUserInfo">

            </div>
        </div>
        <router-view/>
        <div class="footer">
            <p>如有任何疑问请咨询QQ：0910417</p>
            <p>也可以发送邮件到我们的邮箱：0910417</p>
            <p>我们的成长离不您的支持</p>
        </div>
    </div>
</template>
<style lang="scss" scoped>
    .video {
        width: 100%;
        height: 100%;
        position: absolute;

        & > .header {
            height: 0.3rem;
            display: flex;
            padding: 0.04rem 0.1rem;

            & > .logo {
                height: 0.3rem;

                & > img {
                    width: 0.7rem;
                    height: 0.3rem;
                }
            }

            & > .filling {
                width: 100%;
                flex: 1;
            }

            & > .search {
                color: #cccccc;
                width: 0.26rem;
                height: 0.26rem;
                margin: 0.02rem 0.08rem 0.02rem 0;

                & > i {
                    font-size: 0.24rem;
                }
            }

            & > .user {
                width: 0.28rem;
                height: 0.28rem;
                margin: 0.01rem 0;
                border-radius: 50%;
                background: #cfcfcf;
                background: url("../static/touxiang.jpeg") no-repeat;
                background-size: 100% auto;
            }
        }

        & > .footer {
            display: block;
            padding: 0.08rem 0;
            background: #f4f4f4;

            & > p {
                display: block;
                text-align: center;
                color: #999;
            }
        }
    }
</style>

<script lang="ts">
    import Vue from 'vue';
    import Component from 'vue-class-component';

    declare const window: any;

    @Component
    export default class VideoComponent extends Vue {
        public onToUserInfo() {
            if (window.userInfo != null) {
                const url = '/video/user/' + window.userInfo.id;
                if (this.$route.path !== url) {
                    this.$router.push(url);
                }
            } else {
                this.$router.push('/video/login');
            }
        }

        public onHome() {
            this.$router.push('/video/index');
        }
    }
</script>
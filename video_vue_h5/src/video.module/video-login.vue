<template>
    <div class="login">
        <p class="title">用户登陆</p>
        <van-form @submit="onSubmit">
            <van-field
                    v-model="user.account"
                    name="用户名"
                    label="用户名"
                    placeholder="用户名"
                    :rules="[{ required: true, message: '请填写用户名' }]"
            />
            <van-field
                    v-model="user.password"
                    type="password"
                    name="密码"
                    label="密码"
                    placeholder="密码"
                    :rules="[{ required: true, message: '请填写密码' }]"
            />
            <div style="margin: 16px;">
                <van-button round block type="info" native-type="submit" color="#fb7299">
                    注册并登陆
                </van-button>
            </div>
        </van-form>
    </div>
</template>
<style lang="scss" scoped>
    .login {
        height: 3rem;
        border-top: 1px solid #eeeeee;
        width: 100%;
        position: absolute;
        background: #fff;

        & > .title {
            color: rgba(69, 90, 100, 0.6);
            font-size: 0.12rem;
            line-height: 0.16rem;
            font-weight: 300;
            text-indent: 0.1rem;
        }
    }
</style>

<script lang="ts">
    import Vue from 'vue';
    import Component from 'vue-class-component';
    import Utils from "../utils/utils";

    declare const window: any;

    @Component({
        components: {}
    })
    export default class LoginComponent extends Vue {
        public user: any = {};

        public async onSubmit() {
            this.$toast.loading({
                message: 'Loading...',
                forbidClick: true,
            });
            const result = (await this.$api.login.execute(this.user)).asObject();
            this.$toast.clear();
            if (result.success) {
                let url = Utils.getParam('r');
                url = url == null || url.length <= 0 ? '/video/index' : url;
                window.userInfo = result.data;
                window.location.href = window.location.origin + url;
                Utils.setItem('userInfo', result.data);
            } else {
                this.$toast(result.message);
            }
        }
    }
</script>

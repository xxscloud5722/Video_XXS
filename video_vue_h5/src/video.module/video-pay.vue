<template>
    <div class="pay">
        <div class="wall"></div>
        <div class="info" v-if="userInfo != null">
            <div class="avatar">
                <img v-lazy="userInfo.avatarUrl">
            </div>
            <div class="content">
                <div class="name">{{userInfo.nickName}}</div>
                <div class="desc">{{userInfo.levelName}}</div>
            </div>
        </div>
        <div class="pay">
            <!-- 选择方案 -->
            <p class="title">套餐选择</p>
            <div class="package">
                <div class="item" :class="{'active':item.active}" v-for="item of packageList"
                     @click="onPackageChange(item)">
                    <div class="title">{{item.title}}</div>
                    <div class="price">￥<span class="digital">{{item.price}}</span>&nbsp;</div>
                    <div class="original-price digital">&nbsp;{{item.originalPrice}}&nbsp;</div>
                    <div class="tag" v-if="item.tag != null && item.tag !== ''">{{item.tag}}</div>
                </div>


                <div style="min-width: 0.06rem; display: block"></div>
            </div>

            <!-- 选择支付 -->
            <p class="title">支付方式</p>
            <van-radio-group v-model="paySelect" class="pay-list">
                <van-cell-group>
                    <van-cell
                            v-for="item of payList"
                            clickable>
                        <template #title>
                            <div class="row">
                                <img :src="item.icon" alt="">
                                {{item.name}}
                            </div>
                        </template>
                        <template #right-icon>
                            <van-radio :name="item" ref="checkboxes" :value="item.value" checked-color="#fb7299"/>
                        </template>
                    </van-cell>
                </van-cell-group>
            </van-radio-group>
            <div class="submit">
                <van-button round block color="#fb7299" @click="onSubmit">确认开通</van-button>
            </div>
        </div>
    </div>
</template>
<style lang="scss" scoped>
    .pay {
        & > .wall {
            background: url("../static/my.png") no-repeat;
            background-size: 100% auto;
            width: 100%;
            height: 0.8rem;
        }

        & > .info {
            display: flex;
            padding: 0 0.08rem;
            margin-top: -0.1rem;

            & > .avatar {
                margin: 0 0.1rem 0 0;

                & > img {
                    width: 0.8rem;
                    height: 0.8rem;
                    border-radius: 50%;
                }
            }

            & > .content {
                padding: 0.2rem 0 0 0;

                & > .name {
                    color: #212121;
                    font-size: 0.16rem;
                    line-height: 0.3rem;
                }

                & > .desc {
                    color: #999999;
                    font-size: 0.12rem;
                    font-weight: 300;
                }
            }
        }

        & > .pay {
            padding: 0.1rem 0;

            & > .package {
                padding: 0.05rem 0.08rem 0.12rem 0.12rem;
                margin: 0.04rem 0 0.2rem 0;
                overflow-y: auto;
                display: flex;


                & > .item {
                    border: 1px solid #eeeeee;
                    min-width: 0.86rem;
                    max-width: 0.86rem;
                    padding: 0.08rem 0;
                    border-radius: 0.04rem;
                    margin: 0 0.15rem 0 0;
                    flex: 1;
                    position: relative;


                    & > .title {
                        text-align: center;
                        margin: 0;
                        font-size: 0.13rem;
                        color: #212121;
                    }

                    & > .price {
                        color: #fb7299;
                        text-align: center;

                        & > span {
                            font-size: 0.24rem;
                            font-weight: bold;
                        }
                    }

                    & > .original-price {
                        color: #999999;
                        font-size: 0.11rem;
                        font-weight: 300;
                        text-align: center;
                        text-decoration: line-through;
                    }

                    & > .tag {
                        position: absolute;
                        right: 0;
                        top: 0;
                        font-size: 0.08rem;
                        font-weight: 300;
                        color: #fff;
                        background: #fb7299;
                        padding: 0.01rem 0.03rem;
                        margin: -0.04rem;
                        border-radius: 0.02rem;
                    }
                }

                & > .item.active {
                    border-color: #fb7299;
                    background: rgba(251, 114, 153, 0.06);
                }
            }

            & > .title {
                margin: 0.03rem 0;
                font-size: 0.11rem;
                color: #999999;
                text-indent: 0.08rem;
            }

            & > .pay-list {
                margin: 0.1rem 0 0.15rem 0;

                & .row {
                    position: relative;
                    padding: 0 0 0 0.28rem;

                    & > img {
                        width: 0.2rem;
                        position: absolute;
                        left: 0;
                    }
                }
            }

            & > .submit {
                margin: 0 0.08rem 0.15rem 0.08rem;
            }
        }
    }
</style>

<script lang="ts">
    import Vue from 'vue';
    import Component from 'vue-class-component';
    import CommentComponent from "../component/comment-component.vue";
    import LocalListComponent from "../component/local-list-component.vue";

    declare const window: any;

    @Component({
        components: {LocalListComponent, CommentComponent}
    })
    export default class PayComponent extends Vue {
        public packageList: any[] = [];
        public payList: any[] = [];

        public userInfo: any = null;

        public paySelect: any = null;

        public async mounted() {
            this.userInfo = window.userInfo;
            const result = (await this.$api.getProductList.execute()).asObject();
            if (result.success) {
                result.data.forEach(it => {
                    it.active = false;
                })
                result.data[0].active = true;
                this.packageList = result.data;
            }

            const payMethodResult = (await this.$api.getPayMethod.execute()).asObject();
            if (payMethodResult.success) {
                payMethodResult.data.forEach(it => {
                    if (it.value === 'WE_CHAT_PAY') {
                        this.payList.push({name: '微信', icon: require('../static/wechat.jpg'), value: 'WE_CHAT_PAY'});
                    } else if (it.value === 'ALI_PAY') {
                        this.payList.push({name: '支付宝', icon: require('../static/alipay.jpg'), value: 'ALI_PAY'});
                    }
                });
                if (this.payList.length > 0) {
                    this.paySelect = this.payList[0];
                }
            }
        }

        public async onPackageChange(item: any) {
            this.packageList.forEach(it => {
                it.active = false;
            });
            item.active = true;
        }

        public async onSubmit() {
            const item = this.packageList.find(it => {
                return it.active;
            });

            if (item == null) {
                return;
            }
            if (this.paySelect == null) {
                return;
            }

            console.log(item.id + ',' + this.paySelect.value);
        }
    }
</script>
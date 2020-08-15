<template>
    <div class="comment">
        <div class="row" v-for="item of data">
            <div class="avatar" @click="onToUserInfo(item)">
                <img v-lazy="item.userAvatarUrl" alt="">
            </div>
            <div class="content">
                <div class="nick-name" @click="onToUserInfo(item)">{{item.userNickName}}</div>
                <div class="date">{{item.date}}</div>
                <div class="message">
                    <div class="text">{{item.text}}</div>
                    <div class="reply" v-if="item.reply != null && item.reply.length > 0">
                        <div class="reply-row" v-for="r of item.reply">
                            <a class="link">{{r.userNickName}}：</a>
                            <span class="text">{{r.text}}</span>
                        </div>
                    </div>
                </div>
                <div class="cite" v-if="item.citeTitle != null && item.citeTitle !== ''">
                    <i class="iconfont icon-huati"></i> {{item.citeTitle}}
                </div>
            </div>
        </div>
    </div>
</template>

<style lang="scss" scoped>
    .comment {
        & > .row {
            display: flex;
            padding: 0.1rem 0.04rem;
            border-bottom: 1px solid #eeeeee;

            & > .avatar {
                min-width: 0.5rem;

                & > img {
                    display: block;
                    width: 0.32rem;
                    height: 0.32rem;
                    border-radius: 50%;
                    margin: 0.05rem auto;
                }
            }

            & > .content {
                padding: 0 0.12rem 0 0;

                & > .nick-name {
                    line-height: 0.2rem;
                    font-size: 0.13rem;
                    color: #757575;
                }

                & > .date {
                    line-height: 0.2rem;
                    font-size: 0.1rem;
                    color: #757575;
                }

                & > .message {
                    & > .text {
                        color: #212121;
                        line-height: 1.5;
                        font-size: 0.115rem;
                    }

                    & > .reply {
                        background: #f4f4f4;
                        padding: 0.08rem 0.06rem;
                        margin: 0.08rem 0 0 0;

                        & > .reply-row {
                            font-size: 0.11rem;
                            margin-top: 0.04rem;
                            color: #222222;

                            & > .link {
                                color: #5090cc;
                            }
                        }

                        & > .reply-row:first-child {
                            margin: 0;
                        }
                    }
                }

                & > .cite {
                    color: #0077aa;
                    height: 0.3rem;
                    line-height: 0.3rem;
                    padding: 0 0.2rem;
                    overflow: hidden;
                    position: relative;
                    font-size: 0.11rem;
                    font-weight: 300;
                    -webkit-line-clamp: 1;
                    text-overflow: ellipsis;

                    & > i {
                        font-size: 0.18rem;
                        display: block;
                        position: absolute;
                        left: 0;
                        top: -0.01rem;
                    }
                }
            }
        }

        & > .row:last-child {
            border-bottom: none;
        }
    }
</style>
<script lang="ts">
    import Vue from 'vue';
    import Component from 'vue-class-component';
    import {Model, Prop} from "vue-property-decorator";


    declare const window: any;
    @Component
    export default class CommentComponent extends Vue {

        @Prop({
            type: Array,
            default: () => []
        })
        @Model('change')
        public data: any[];


        public mounted() {
        }

        public async onToUserInfo(item: any) {
            await this.$router.push('/video/user/' + item.userId)
        }
    }
</script>
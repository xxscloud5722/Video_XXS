<template>
    <div class="video-list">
        <div :class="type.toLowerCase()" v-if="type === 'DEFAULT'">
            <div class="row">
                <template v-for="(item,index) in data">
                    <div class="item" v-if="index % 2 === 0"  @click="onToVideo(item)">
                        <div class="image">
                            <img v-lazy="item.cover" alt="">
                            <div>
                                {{item.commentCount}}
                            </div>
                            <div>
                                {{item.playCount}}
                            </div>
                        </div>
                        <p class="info" v-html="item.title"></p>
                    </div>
                </template>
            </div>
            <div class="row">
                <template v-for="(item,index) in data">
                    <div class="item" v-if="index % 2 === 1" @click="onToVideo(item)">
                        <div class="image">
                            <img v-lazy="item.cover" alt="">
                            <div>
                                {{item.commentCount}}
                            </div>
                            <div>
                                {{item.playCount}}
                            </div>
                        </div>
                        <p class="info" v-html="item.title"></p>
                    </div>
                </template>
            </div>
        </div>

        <div :class="type.toLowerCase()" v-if="type === 'CHILDREN'">
            <div class="module" v-for="(item) in data">
                <div class="title" v-if="item.title != null && item.title.length > 0">
                    <div class="name">{{item.title}}</div>
                    <div class="link">{{item.linkName}}</div>
                </div>
                <div class="list">
                    <div class="row">
                        <template v-for="(item,index) in item.videoList">
                            <div class="item" v-if="index % 2 === 0" @click="onToVideo(item)">
                                <div class="image">
                                    <img v-lazy="item.cover" alt="">
                                    <div>
                                        {{item.commentCount}}
                                    </div>
                                    <div>
                                        {{item.playCount}}
                                    </div>
                                </div>
                                <p class="info" v-html="item.title"></p>
                            </div>
                        </template>
                    </div>
                    <div class="row">
                        <template v-for="(item,index) in item.videoList">
                            <div class="item" v-if="index % 2 === 1" @click="onToVideo(item)">
                                <div class="image">
                                    <img v-lazy="item.cover" alt="">
                                    <div>
                                        {{item.commentCount}}
                                    </div>
                                    <div>
                                        {{item.playCount}}
                                    </div>
                                </div>
                                <p class="info" v-html="item.title"></p>
                            </div>
                        </template>
                    </div>
                </div>
            </div>
        </div>

        <van-empty
                v-if="data == null || data.length <= 0"
                class="video-my-custom-image"
                :image="require('../static/e.png')"
                description="咦，没有视频啦～"
        />
    </div>
</template>

<style lang="scss">
    .video-list {
        width: 95%;
        display: block;
        margin: auto;

        & > .default {
            display: flex;
            margin-bottom: 0.12rem;

            & > .row:last-child {
                margin-left: 0.08rem;
            }

            & > .row {
                flex: 1;

                & > .item {
                    margin: 0.1rem 0;

                    & > .image {
                        width: 100%;
                        height: 0.86rem;
                        background: #eeee;
                        border-radius: 0.04rem;
                        overflow: hidden;
                        & > img {
                            width: 100%;
                            height: 100%;
                            object-fit: cover;
                        }
                    }

                    & > .info {
                        font-size: 0.11rem;
                        line-height: 1.5;
                        height: 0.34rem;
                        color: #444444;
                        margin: 0.03rem 0 0 0;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        display: -webkit-box;
                        -webkit-line-clamp: 2;
                        -webkit-box-orient: vertical;
                    }
                }
            }
        }

        & > .children {
            margin-top: 0.1rem;

            & > .module {
                margin-bottom: 0.12rem;


                & > .title {
                    padding: 0.02rem 0;
                    line-height: 0.15rem;
                    display: flex;

                    & > .name {
                        font-size: 0.135rem;
                        width: 100%;
                        flex: 1;
                        text-indent: 0.04rem;
                    }

                    & > .link {
                        font-size: 0.11rem;
                        color: #999;
                        margin: 0 0.04rem 0 0;
                    }
                }

                & > .list {
                    display: flex;

                    & > .row:last-child {
                        margin-left: 0.08rem;
                    }

                    & > .row {
                        flex: 1;

                        & > .item {
                            margin: 0.1rem 0;

                            & > .image {
                                width: 100%;
                                height: 0.86rem;
                                background: #eeeeee;
                                overflow: hidden;
                                border-radius: 0.04rem;

                                & > img {
                                    width: 100%;
                                    height: 100%;
                                    object-fit: cover;
                                }
                            }

                            & > .info {
                                font-size: 0.11rem;
                                line-height: 1.5;
                                height: 0.34rem;
                                color: #444444;
                                margin: 0.03rem 0 0 0;
                                overflow: hidden;
                                text-overflow: ellipsis;
                                display: -webkit-box;
                                -webkit-line-clamp: 2;
                                -webkit-box-orient: vertical;
                            }
                        }
                    }
                }
            }
        }

        & .van-empty__image {
            width: 2rem;
            height: 1.14rem;
        }
    }
</style>
<script lang="ts">
    import Vue from 'vue';
    import Component from 'vue-class-component';
    import {Model, Prop} from "vue-property-decorator";


    declare const window: any;
    @Component
    export default class VideoListComponent extends Vue {

        @Prop({
            type: Array,
            default: () => []
        })
        @Model('change')
        public data: any[];


        @Prop({
            type: String,
            default: () => 'DEFAULT'
        })
        public type: string;

        public mounted() {
        }

        public onToVideo(item: any) {
            this.$router.push('/video/detail/' + item.id);
        }
    }
</script>
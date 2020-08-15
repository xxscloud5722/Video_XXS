<template>
    <div class="video-detail" v-if="video.detail != null">
        <div class="mp4" :class="{'full':video.full}">
            <div v-if="!video.init" class="cover" @click="onPlay">
                <img v-lazy="video.detail.cover" alt="">
            </div>
            <div class="play" v-if="!video.init" @click.stop="onPlay">
                <img src="../static/play.svg" alt="">
            </div>
            <div class="duration" v-if="!video.init && video.detail != null && video.detail.virtualDuration  != null">
                {{(video.detail.virtualDuration / 60).toFixed(0)}}:{{video.detail.virtualDuration % 60}}
            </div>
            <div class="video" v-show="video.init">
                <video webkit-playsinline playsinline preload="metadata" x5-video-player-type="h5-page"
                       :src="video.url" ref="video"
                       @timeupdate="onProgress" @pause="onPlayOrSuspend(null,false)"
                       @ended="onPlayOrSuspend(null,false,0)"
                       @click="onHidePanel(null,true)"></video>
                <!-- 控制条 -->
                <div class="panel" :class="{'full':video.full}" v-show="video.panel">
                    <div class="play" @click="onPlayOrSuspend">
                        <i class="iconfont icon-play" v-if="!video.play"></i>
                        <i class="iconfont icon-zantingtingzhi" v-if="video.play"></i>
                    </div>
                    <div class="progress">
                        <van-slider v-model="video.progress" active-color="#fb7299" :bar-height="'0.024rem'"
                                    @input="onProgressStart"
                                    @change="onProgressInputEnd"
                        >
                            <template #button>
                                <div class="progress-button"><img alt="" src="../static/progress-s.svg"></div>
                            </template>
                        </van-slider>
                    </div>
                    <div class="progress-text">
                        {{ Number((video.currentTime / 60).toFixed(0)) < 9
                        ?'0'+ (video.currentTime / 60).toFixed(0)
                        : (video.currentTime / 60).toFixed(0)
                        }}:{{video.currentTime % 60 <9 ? '0'+video.currentTime % 60: video.currentTime % 60}}
                        /
                        <template v-if="video.detail != null && video.detail.virtualDuration  != null">
                            {{
                            Number((video.detail.virtualDuration / 60).toFixed(0)) < 9
                            ? '0'+(video.detail.virtualDuration / 60).toFixed(0)
                            :(video.detail.virtualDuration / 60).toFixed(0)
                            }}:{{
                            video.detail.virtualDuration % 60 < 9
                            ? '0'+video.detail.virtualDuration % 60 : video.detail.virtualDuration % 60}}
                        </template>
                    </div>
                    <div class="full-screen" @click="onFullScreen">
                        <i class="iconfont icon-quanping1" v-if="!video.full"></i>
                        <i class="iconfont icon-zu" v-if="video.full"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="vip">
            <div class="button" @click="onBuy">VIP解锁全部</div>
        </div>
        <p class="title">{{video.detail.title}}</p>
        <div class="desc" v-if="video.detail.description != null && video.detail.description.length > 0">
            {{video.detail.description}}
        </div>
        <div class="toolbar">
            <div class="watch"><i class="iconfont icon-dianshiji"></i>{{video.detail.watchCount+1}}</div>
            <div class="clarity"><i class="iconfont icon-qingxidu"></i>{{video.detail.clarity}}</div>
            <div class="full"></div>
            <div class="date"><i class="iconfont icon-rili1"></i>{{video.detail.publishDate}}
            </div>
        </div>
        <van-tabs class="tabs" color="#fb7299" type="line" :ellipsis="true" :line-height="'0.02rem'">
            <van-tab v-for="(item,index) of menusList" :title="item.title">
                <video-list-component :data="dataList" v-if="index === 0"></video-list-component>
                <comment-component v-if="index === 1" :data="commentList"></comment-component>
            </van-tab>
        </van-tabs>
    </div>
</template>
<style lang="scss">
    .video-detail {
        & > .mp4 {
            width: 100%;
            height: 2rem;
            position: relative;

            & > .cover {
                width: 100%;
                height: 100%;
                background: #eeeeee;

                & > img {
                    width: 100%;
                    height: 100%;
                    object-fit: cover;
                }
            }

            & > .video {
                width: 100%;
                position: absolute;
                left: 0;
                right: 0;
                top: 0;
                bottom: 0;
                overflow: hidden;
                background: #000;


                & > video {
                    width: 100%;
                    height: 100%;
                }

                & > .panel {
                    position: absolute;
                    left: 0;
                    right: 0;
                    bottom: 0;
                    height: 0.28rem;
                    background: rgba(0, 0, 0, 0.1);
                    display: flex;
                    padding: 0.05rem 0.08rem;


                    & > div {
                        position: relative;

                        & > i {
                            color: #ffffff;
                            position: absolute;
                            display: block;
                            font-size: 0.16rem;
                        }
                    }

                    & > .play {
                        width: 0.28rem;

                        & > i.icon-play {
                            left: 0.056rem;
                            top: 0.04rem;
                        }

                        & > i.icon-zantingtingzhi {
                            left: 0.056rem;
                            top: 0.04rem;
                        }
                    }

                    & > .progress {
                        width: 100%;
                        flex: 1;
                        margin: 0 0.08rem;

                        & .van-slider {
                            margin: 0.13rem 0 0 0 !important;
                        }

                        & .progress-button {
                            width: 0.2rem;
                        }
                    }

                    & > .progress-text {
                        color: #fff;
                        font-size: 0.1rem;
                        line-height: 0.28rem;
                        padding: 0 0.04rem;
                    }

                    & > .full-screen {
                        width: 0.3rem;

                        & > i.icon-quanping1 {
                            left: 0.07rem;
                            top: 0.04rem;
                        }

                        & > i.icon-zu {
                            left: 0.07rem;
                            top: 0.04rem;
                        }
                    }
                }

                & > .panel.full {
                    position: absolute;
                    z-index: 2147483647;
                }
            }

            & > .play {
                width: 0.6rem;
                height: 0.6rem;
                display: block;
                position: absolute;
                left: 50%;
                top: 50%;
                margin: -0.3rem 0 0 -0.3rem;

                & > img {
                    width: 100%;
                    height: 100%;
                }
            }

            & > .duration {
                position: absolute;
                left: 0.1rem;
                bottom: 0.1rem;
                color: #fff;
                font-size: 0.1rem;
                font-weight: 300;
                padding: 0.022rem 0.04rem;
                border-radius: 0.02rem;
                background: rgba(0, 0, 0, 0.25);
            }
        }

        & > .mp4.full {
            z-index: 2147483647;
            left: 0 !important;
            right: 0 !important;
            top: 0 !important;
            bottom: 0 !important;
            height: auto;
            position: absolute;
        }

        & > .vip {
            padding: 0.06rem 0.08rem;
            margin-top: 0.08rem;

            & > .button {
                background: rgb(251, 114, 153);
                text-align: center;
                display: block;
                margin: auto;
                height: 0.36rem;
                line-height: 0.36rem;
                color: #fff;
                letter-spacing: 1px;
                font-weight: 500;
                font-size: 0.135rem;
                border-radius: 0.36rem
            }
        }

        & > .title {
            font-size: 0.16rem;
            padding: 0.04rem 0.08rem;
            margin: 0;
        }

        & > .desc {
            color: #888888;
            font-size: 0.11rem;
            line-height: 1.5;
            padding: 0.03rem 0.08rem;
        }

        & > .toolbar {
            display: flex;
            height: 0.3rem;
            line-height: 0.3rem;
            padding: 0 0.08rem;
            color: #999999;
            margin-bottom: 0.08rem;

            & > .full {
                width: 100%;
                flex: 1;
            }

            & > div {
                font-size: 0.1rem;
                font-weight: 300;
                position: relative;
                padding: 0 0 0 0.18rem;
                margin: 0 0.08rem 0 0;

                & > i {
                    color: #666666;
                    position: absolute;
                    left: 0;
                    top: 0;
                }
            }

            & > .watch {
                & > i {
                    font-size: 0.17rem;
                    margin-top: -0.01rem;
                }
            }

            & > .clarity {
                & > i {
                    font-size: 0.15rem;
                }
            }

            & > .date {
                margin: 0 !important;

                & > i {
                    font-size: 0.15rem;
                }
            }
        }

        & > .tabs {
            & .van-tabs__wrap {
                height: 0.34rem !important;
            }

            & .van-tab--active span {
                color: #fb7299;
            }

            & .van-tab .van-tab__text {
                font-size: 0.12rem;
            }
        }
    }
</style>

<script lang="ts">
    import Vue from 'vue';
    import Component from 'vue-class-component';
    import VideoListComponent from "../component/video-list-component.vue";
    import CommentComponent from "../component/comment-component.vue";
    import Utils from "../utils/utils";
    import {Watch} from "vue-property-decorator";

    declare const window: any;
    @Component({
        components: {CommentComponent, VideoListComponent}
    })
    export default class VideoDetailComponent extends Vue {
        public menusList: any = [
            {title: '相关推荐'},
            {title: '评论'}
        ];
        public dataList: any[] = [];
        public commentList: any[] = [];

        public video: any = {
            url: '',
            progressStatus: true,
            progress: 0,
            full: false,
            play: false,
            init: false,
            detail: {},
            currentTime: 0,
            panel: true,
            panelTime: null
        };

        public created() {
            window.document.body.addEventListener("fullscreenchange", () => {
                if (!window.document.webkitIsFullScreen) {
                    this.onFullScreen(true, false)
                } else {
                    this.onFullScreen(true, true)
                }
            });
        }

        public async mounted() {
            await this.load();
        }

        public async load() {
            const urls = await Utils.getUrlPath()
            const id = urls[urls.length - 1];
            const result = (await this.$api.getVideoDetail.execute({id: id})).asObject();
            if (result.success) {
                this.menusList = [
                    {title: '相关推荐'},
                    {title: '评论'}
                ];
                this.video = {
                    url: '',
                    progressStatus: true,
                    progress: 0,
                    full: false,
                    play: false,
                    init: false,
                    detail: {},
                    currentTime: 0,
                    panel: true
                };
                this.video.detail = result.data;
                if (this.video.detail == null) {
                    return;
                }
                Utils.setTitle(result.data.title);
                this.video.url = result.data.url;
                this.video.detail.publishDate = Utils.dateFormat('MM-dd', new Date(this.video.detail.createTime));
                this.menusList[1].title = this.menusList[1].title + '   ' + this.video.detail.commentList.length
                this.dataList = this.video.detail.recommendList;
                this.commentList = [];
                this.video.detail.commentList.forEach(it => {
                    this.commentList.push({
                        userAvatarUrl: it.avatarUrl,
                        userNickName: it.nickName,
                        date: it.createTime,
                        text: it.content,
                        userId: it.userId
                    });
                });
            }
        }

        @Watch('$route')
        public async route() {
            this.video.init = false;
            this.onPlayOrSuspend(null, false, 0);
            if (this.$route.path.indexOf('/video/detail') > -1) {
                await this.load();
            }
        }

        public onHidePanel(_?: any, status?: boolean) {
            if (status == true) {
                this.video.panel = true;
            }
            if (this.video.panelTime != null) {
                clearTimeout(this.video.panelTime);
            }
            this.video.panelTime = setTimeout(() => {
                this.video.panel = false;
                this.video.panelTime = null;
            }, 3000);
        }

        public onPlay() {
            this.video.init = true;
            this.video.play = true;
            this.$refs.video.play();
            this.$refs.video.controls = false;
            this.onHidePanel();
        }

        public onPlayOrSuspend(_?: any, status?: boolean, progress?: number) {
            if (this.$refs.video == null) {
                return;
            }
            this.video.play = status != undefined ? status : !this.video.play;
            if (this.video.play) {
                this.$refs.video.play();
            } else {
                if (this.video.panelTime != null) {
                    clearTimeout(this.video.panelTime);
                }
                this.video.panel = true;
                this.$refs.video.pause();
            }
            if (progress != null) {
                this.$refs.video.currentTime = progress;
            }
        }

        public onFullScreen(isSystem?: boolean, status?: boolean) {
            this.video.full = status == undefined ? !this.video.full : status;
            this.onHidePanel(null, true);
            if (isSystem !== true) {
                if (this.video.full) {
                    const element = window.document.body;
                    if (element.requestFullscreen) {
                        element.requestFullscreen();
                    } else if (element.msRequestFullscreen) {
                        element.msRequestFullscreen();
                    } else if (element.mozRequestFullScreen) {
                        element.mozRequestFullScreen();
                    } else if (element.webkitRequestFullScreen) {
                        element.webkitRequestFullScreen();
                    }
                } else {
                    const document = window.document;
                    if (document.cancelFullScreen) {
                        document.cancelFullScreen();
                    } else if (document.mozCancelFullScreen) {
                        document.mozCancelFullScreen();
                    } else if (document.webkitCancelFullScreen) {
                        document.webkitCancelFullScreen();
                    } else if (document.msExitFullscreen) {
                        document.msExitFullscreen()
                    }
                }
            }
        }

        public onProgress(event: any) {
            if (!this.video.progressStatus) {
                return;
            }
            this.video.currentTime = parseInt(event.currentTarget.currentTime);
            const playTime = event.currentTarget.currentTime;
            const totalTime = event.currentTarget.duration;
            this.video.progress = Math.floor(playTime / totalTime * 100)
            if (this.video.progress > 100) {
                this.video.progress = 100;
            }
        }

        public onProgressInputEnd() {
            const totalTime = this.$refs.video.duration;
            this.$refs.video.currentTime = this.video.progress / 100 * totalTime
            this.onPlayOrSuspend(null, true);
            this.video.progressStatus = true;
            this.onHidePanel(null, true);
        }

        public onProgressStart() {
            this.video.progressStatus = false;
            this.onHidePanel(null, true);
        }

        public onBuy() {
            if (window.useInfo == null) {
                this.$router.push('/video/login?r=/video/pay')
            } else {
                this.$router.push('/video/pay')
            }
        }

    }
</script>
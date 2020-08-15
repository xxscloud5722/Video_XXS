<template>
    <div class="video-index">
        <van-tabs class="tabs" color="#fb7299" type="line" :ellipsis="true" :line-height="'0.02rem'"
                  @change="onTabsChange(1,$event)">
            <van-tab v-for="item of menusList" :title="item.title">
                <van-tabs class="tabs" color="#fb7299" type="line" :ellipsis="true" :line-height="'0.02rem'"
                          @change="onTabsChange(2,$event)" v-if="menusList2 != null && menusList2.length > 0">
                    <van-tab v-for="item of menusList2" :title="item.title">

                        <!-- 内容 -->
                        <van-swipe class="banner" :autoplay="3000" indicator-color="white"
                                   v-if="bannerList != null && bannerList.length > 0">
                            <van-swipe-item v-for="item of bannerList">
                                <img v-lazy="item.cover" alt=""/>
                            </van-swipe-item>
                        </van-swipe>
                        <video-list-component :data="dataList" :type="'CHILDREN'"></video-list-component>

                    </van-tab>
                </van-tabs>
                <template v-else>
                    <!-- 内容 -->
                    <van-swipe class="banner" :autoplay="3000" indicator-color="white"
                               v-if="bannerList != null && bannerList.length > 0">
                        <van-swipe-item v-for="item of bannerList">
                            <img v-lazy="item.cover" alt=""/>
                        </van-swipe-item>
                    </van-swipe>
                    <video-list-component :data="dataList" :type="'CHILDREN'"></video-list-component>
                </template>
            </van-tab>
        </van-tabs>
    </div>
</template>
<style lang="scss">
    .video-index {
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

        & .banner {
            width: 96%;
            display: block;
            margin: 0.06rem auto 0 auto;
            height: 1.2rem;
            overflow: hidden;
            border-radius: 0.04rem;

            & .van-swipe-item {
                color: #fff;
                font-size: 20px;
                line-height: 1.2rem !important;
                text-align: center;
                background-color: #eee;

                & img {
                    width: 100%;
                    height: 100%;
                    object-fit: cover;
                    display: block;
                }
            }
        }
    }
</style>

<script lang="ts">
    import Vue from 'vue';
    import Component from 'vue-class-component';
    import VideoListComponent from "../component/video-list-component.vue";

    @Component({
        components: {VideoListComponent}
    })
    export default class VideoComponent extends Vue {
        public menusList: any = [];
        public menusList2: any = [];
        public menusAllList: any = [];

        public bannerList: any[] = [];
        public dataList: any[] = [];

        public async mounted() {
            const result = (await this.$api.getActivityItemList.execute()).asObject();
            if (result.success) {
                result.data.forEach(it => {
                    if (it.level === 0) {
                        this.menusList.push({title: it.name, id: it.id})
                    }
                });
                this.menusAllList = result.data;
                await this.onTabsChange(1, 0)
            }
        }

        public async onSelectTab(id: string) {
            this.menusList2 = [];
            this.menusAllList.forEach(it => {
                if (it.parentId === id) {
                    this.menusList2.push({id: it.id, title: it.name});
                }
            });
            return this.menusList2.length <= 0;
        }

        public async onTabsChange(index: number, event: number) {
            if (index === 1) {
                const flag = await this.onSelectTab(this.menusList[event].id);
                if (!flag) {
                    event = 0;
                    index = 2;
                }
            }

            const id = index === 1 ? this.menusList[event].id : this.menusList2[event].id
            this.dataList = [];
            const result = (await this.$api.getActivityItemVideoList.execute({id: id})).asObject();
            if (result.success) {
                this.dataList = result.data;
            }
        }
    }
</script>
<template>
    <div class="my" v-if="userInfo != null">
        <div class="wall"></div>
        <div class="info">
            <div class="avatar">
                <img v-lazy="userInfo.avatarUrl">
            </div>
            <div class="content">
                <div class="name">{{userInfo.nickName}}</div>
                <div class="desc">这个人很神秘，什么都没有写</div>
            </div>
        </div>
        <div class="tab">
            <div class="item active">个人动态</div>
        </div>
        <div class="">
            <comment-component :data="commentList"></comment-component>
            <van-empty
                    class="video-my-custom-image"
                    :image="require('../static/e.png')"
                    description="到达动态的尽头惹～"
            />
        </div>
    </div>
</template>
<style lang="scss">
    .my {
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

        & > .tab {
            height: 0.2rem;
            display: flex;
            padding: 0.06rem 0.1rem;
            border-top: 1px solid #eee;
            border-bottom: 1px solid #eee;
            margin-top: 0.08rem;

            & > .item {
                font-size: 0.11rem;
                line-height: 0.2rem;
            }

            & > .item.active {
                color: #fb7299;
            }
        }
    }

    .video-my-custom-image .van-empty__image {
        width: 2rem;
        height: 1.14rem;
    }
</style>

<script lang="ts">
    import Vue from 'vue';
    import Component from 'vue-class-component';
    import CommentComponent from "../component/comment-component.vue";
    import Utils from "../utils/utils";

    @Component({
        components: {CommentComponent}
    })
    export default class MyComponent extends Vue {
        public commentList: any[] = [];
        public userInfo: any = null;

        public async mounted() {
            await this.load();
        }

        public async load() {
            const urls = await Utils.getUrlPath();
            const result = (await this.$api.getUserInfo.execute({id: urls[urls.length - 1]})).asObject();
            if (result.success) {
                this.userInfo = result.data;
                Utils.setTitle(result.data.nickName + '的个人空间');
                this.commentList = [];
                result.data.commentList.forEach(it => {
                    this.commentList.push({
                        userAvatarUrl: it.avatarUrl,
                        userNickName: it.nickName,
                        date: it.createTime,
                        text: it.content,
                        userId: it.userId,

                        citeTitle: it.citeTitle,
                        citeLink: ''
                    });
                });
            }
        }

    }
</script>
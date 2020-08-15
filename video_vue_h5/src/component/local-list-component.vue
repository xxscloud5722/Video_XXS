<template>
    <div class="local-list">
        <div class="row" v-for="item of data">
            <div class="icon">
                <img v-lazy="item.icon">
            </div>
            <div class="text">{{item.name}}</div>

            <div class="check">
                <van-checkbox v-model="item.active" shape="square" checked-color=""></van-checkbox>
            </div>
        </div>
    </div>
</template>

<style lang="scss">
    .local-list {
        display: block;

        & > .row {
            display: flex;
            height: 0.2rem;
            line-height: 0.2rem;
        }
    }
</style>
<script lang="ts">
    import Vue from 'vue';
    import Component from 'vue-class-component';
    import {Model, Prop} from "vue-property-decorator";


    declare const window: any;
    @Component
    export default class LocalListComponent extends Vue {

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
    }
</script>
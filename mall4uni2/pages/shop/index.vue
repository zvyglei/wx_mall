<template>
	<view class="page">
		<!-- search -->
		<view class="" style="background-color: #fff;">
			<view class="u-m-t-20 u-m-b-0 u-m-l-20 u-m-r-20">
				<u-search shape="square" :showAction="false" clearabled placeholder="搜索商品" v-model="keywords"
					confirm-type="search" @search="toSearchConfirm"></u-search>
			</view>
		</view>
		<!-- u-grid -->
		<!-- <view class="u-m-t-30">
			<u-grid :border="false" col="5">
				<u-grid-item v-for="(listItem,listIndex) in list" :key="listIndex">
					<image :src="listItem.imgPath" class="grid-img"></image>
					<text class="grid-text">{{listItem.name}}</text>
				</u-grid-item>
			</u-grid>
		</view> -->
		<!-- goods -->
		<view style="padding: 20rpx 30rpx 30rpx 30rpx;">
			<!-- <view class="u-font-38 u-m-b-30">好物推荐</view> -->
			<view class="waterfall">
				<view class="u-p-l-10 u-p-r-10" style="width: 100%;" v-for="(listItem,listIndex) in goodsList" :key="listIndex">
					<CardGoods class="goods-item" :key="index" :data="listItem"></CardGoods>
				</view>
			</view>
			<u-loadmore v-if="nomore" :line="true" status="loadmore" loadmore-text="没有更多了" />
		</view>
	</view>
</template>

<script>
	var http = require("@/utils/http");
	import CardGoods from '@/pages/components/card.vue'
	export default {
		components: {
			CardGoods
		},
		data() {
			return {
				nomore: false,
				page: 1,
				keywords: '',
				loadStatus: 'loadmore',
				flowList: [],
				goodsList: []
			}
		},
		onLoad() {
			this.page = 1
			this.getPage()
		},
		/**
		 * 页面上拉触底事件的处理函数
		 */
		onReachBottom: function() {
			this.page++
			http.request({
				url: `/prod/pageProd?keywords=${this.keywords}&page=${this.page}`,
				method: "get",
				callBack: res => {
					if (res && res.length > 0) {
						this.goodsList.push(...res.records)
					} else {
						this.nomore = true
					}
				},
			})
		},
		methods: {
			getPage() {
				http.request({
					url: `/prod/pageProd?keywords=${this.keywords}&page=${this.page}`,
					method: "get",
					callBack: res => {
						this.goodsList = res.records
					},
				})
			},
			toSearchConfirm: function(e) {
				this.getPage()
			},
		}
	}
</script>

<style scoped>
	.grid-img {
		width: 100rpx;
		height: 100rpx;
	}

	.grid-text {
		color: #555;
		margin-top: 10rpx;
		margin-bottom: 30rpx;
	}

	.goods-item {
		width: 50%;
	}

	.waterfall {
		margin: 0 -20rpx;
		column-count: 2; //想要排成的列数
		column-gap: 0;
	}
</style>
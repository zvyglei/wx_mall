<template>
	<view style="background-color: #F8F8F8; height: 100vh;">
		<image class="user-bg u-relative" src="/static/bg/bg_score.png" style="position: relative; top:-45upx;">
			<view class="u-absolute" style="left: 50rpx; top: 50rpx; color: #fff;">
				<view class="u-font-36">当前黄金豆</view>
				<view class="u-font-32 u-m-t-10">
					<text class="">{{wxs.parsePrice(score)[0]}}</text>
					<text class="">.{{wxs.parsePrice(score)[1]}}</text>
				</view>
			</view>
			<view class="u-absolute"
				style="left: 0; top: 200rpx; background: #fff; padding: 10rpx 35rpx; border-top-right-radius: 30rpx; border-bottom-right-radius: 30rpx; color: #3c3c3c;">
				<view class="u-font-18">账单记录</view>
			</view>
		</image>
		<view class="card u-p-b-30 u-p-l-30 u-p-r-30 u-p-t-10 u-m-30"
			style="position: relative; top:-125upx; background-color: #fff; border-radius: 5rpx; height: 77vh; overflow: auto;">
			<view v-for="(item, index) in data" :key="index" style="border-bottom: 1px solid #e2e2e2;">
				<view class="u-flex u-m-b-10 u-m-t-20 u-font-18" style="justify-content: space-between;">
					<text>{{ item.billDesc }}</text>
					<text>{{ item.billType === 0 ? '+' : '-' }}{{wxs.parsePrice(item.score)[0]}}.{{wxs.parsePrice(item.score)[1]}}</text>
				</view>
				<view class="u-m-b-20" style="color:#757575;">
					{{ item.createTime }}
				</view>
			</view>
		</view>
	</view>
</template>

<script module="wxs" lang="wxs" src="@/wxs/number.wxs"></script>
<script>
	var http = require("@/utils/http.js");
	export default {
		data() {
			return {
				score: 0,
				data: []
			}
		},
		/**
		 * 生命周期函数--监听页面显示
		 */
		async onShow() {
			uni.showLoading();
			this.data = {}
			await http.request({
				url: "/p/user/bill",
				method: "get",
				callBack: res => {
					this.data = res
				},
			})
			http.request({
				url: "/p/user/userInfo",
				method: "get",
				callBack: res => {
					this.score = res.score
				},
			})
			uni.hideLoading();
		},
		methods: {

		}
	}
</script>

<style scoped>
	.user-bg {
		width: 100vw;
		height: 380rpx;
		background-size: cover;
	}
</style>
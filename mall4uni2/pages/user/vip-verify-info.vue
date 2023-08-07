<template>
	<view class="register">
		<view class="con">
			<view class="title">
				<view>{{ '您的认证信息' }}</view>
			</view>
			<u--form labelPosition="left" ref="uForm" labelWidth="auto">
				<u-form-item label="身份证号:" borderBottom>
					<text>{{ userVerifyInfo.idCard }}</text>
				</u-form-item>
				<u-form-item label="真实姓名:" borderBottom>
					<text>{{ userVerifyInfo.realName }}</text>
				</u-form-item>
			</u--form>
			<u--form labelPosition="top" ref="uForm" labelWidth="auto">
				<u-form-item label="身份证人像面照片:" borderBottom>
					<image :src="userVerifyInfo.idCardImg1" mode="widthFix" style="width: 88vw;"></image>
				</u-form-item>
				<u-form-item label="身份证国徽面照片:">
					<image :src="userVerifyInfo.idCardImg1" mode="widthFix" style="width: 88vw;"></image>
				</u-form-item>
			</u--form>
		</view>
	</view>
</template>

<script>
	var http = require("@/utils/http.js");
	export default {
		data() {
			return {
				userVerifyInfo: {}
			}
		},
		/**
		 * 生命周期函数--监听页面显示
		 */
		onShow: function() {
			uni.showLoading();
			this.userVerifyInfo = {}
			http.request({
				url: "/p/user/userVerifyInfo",
				method: "get",
				callBack: res => {
					this.userVerifyInfo = res
				},
			})
			uni.hideLoading();
		},
		methods: {

		}
	}
</script>

<style lang="scss" scoped>
	page {
		background: #fff;
		height: 100%;
	}

	.con {
		padding: 50rpx;
	}

	.title {
		left: 50rpx;
		top: 50rpx;
		color: rgba(16, 16, 16, 1);
		font-size: 50rpx;
		text-align: left;
		margin-bottom: 30rpx;
		line-height: 1.7em;
		font-family: PingFangSC-bold;
	}

	.title view:first-child {
		height: 84rpx;
	}

	/deep/ .u-form-item__body__right__message {
		margin-left: 0 !important;
	}
</style>
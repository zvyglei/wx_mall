<template>
	<view class="con">
		<view class="title">
			<view>账号转账</view>
		</view>
		<!-- 账号转账 -->
		<u--form labelPosition="top" labelWidth="auto" :model="formData" :rules="rules" ref="uForm">
			<u-form-item label="对方手机号码" prop="userMobile" borderBottom ref="item1">
				<u--input v-model="formData.userMobile" border="none" placeholder="请输入对方手机号码" maxlength="11" font-size="16"></u--input>
			</u-form-item>
			<u-form-item label="对方姓名" prop="realName" borderBottom ref="item1">
				<u--input v-model="formData.realName" border="none" placeholder="请输入对方姓名" font-size="16"></u--input>
			</u-form-item>
			<u-form-item label="转账数量" prop="score" borderBottom ref="item1">
				<u--input v-model="formData.score" border="none" placeholder="请输入转账数量" font-size="16"></u--input>
			</u-form-item>
		</u--form>
		<view class="u-m-t-30" style="color: #999">当前余额：{{ score || '0' }}</view>
		<view class="u-m-t-30">
			<u-button type="error" text="确认转账" @tap="transfer"></u-button>
		</view>
	</view>
</template>

<script>
	var http = require("@/utils/http");
	export default {
		data() {
			return {
				score: 0,
				formData: {
					userMobile: '',
					realName: '',
					score: undefined
				},
				rules: {
					'userMobile': {
						type: 'number',
						required: true,
						message: '对方手机号码不能为空',
						trigger: ['blur']
					},
					'realName': {
						type: 'string',
						required: true,
						message: '对方姓名不能为空',
						trigger: ['blur']
					},
					'score': {
						type: 'number',
						required: true,
						message: '转账数量不能为空',
						trigger: ['blur']
					}
				},
			}
		},
		/**
		 * 生命周期函数--监听页面加载
		 */
		onLoad: function(options) {
			this.score = options.score || 0
		},
		methods: {
			transfer() {
				this.$refs.uForm.validate().then(res => {
					uni.showLoading();
					var data = JSON.parse(JSON.stringify(this.formData))
					http.request({
						url: "/p/user/transfer",
						method: "post",
						data: data,
						callBack: res => {
							uni.hideLoading();
							uni.showToast({
								title: "转账成功"
							})
							setTimeout(() => {
								wx.switchTab({
									url: '/pages/me/index'
								});
							}, 1000)
						},
					})
				})
			}
		}
	}
</script>


<style scoped>
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
		width: 435rpx;
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
</style>
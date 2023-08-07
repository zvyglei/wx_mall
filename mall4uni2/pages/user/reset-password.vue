<template>
	<view class="register">
		<view class="con">
			<view class="title">
				<view>{{ '修改密码' }}</view>
			</view>
			<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
			<u--form labelPosition="left" :model="formData" :rules="rules" ref="uForm">
				<u-form-item prop="passWord" ref="passWord">
					<u-input border="bottom" :password="!showPassWord" placeholder="请输入6-20位新登录密码" clearable
						v-model="formData.passWord" font-size="16">
						<template slot="suffix">
							<u-icon v-if="!showPassWord" name="eye" size="22" color="#999" @tap="passWordEye"></u-icon>
							<u-icon v-else name="eye-fill" size="22" color="#2979ff" @tap="passWordEye"></u-icon>
						</template>
					</u-input>
				</u-form-item>
				<u-form-item prop="passWord2" ref="passWord2">
					<u-input border="bottom" :password="!showPassWord2" placeholder="新登录密码确认" clearable
						v-model="formData.passWord2" font-size="16">
						<template slot="suffix">
							<u-icon v-if="!showPassWord2" name="eye" size="22" color="#999" @tap="passWordEye2"></u-icon>
							<u-icon v-else name="eye-fill" size="22" color="#2979ff" @tap="passWordEye2"></u-icon>
						</template>
					</u-input>
				</u-form-item>
			</u--form>
			<view class="u-m-t-30">
				<u-button type="primary" text="修改密码" @tap="submitForm()"></u-button>
			</view>
		</view>
	</view>
</template>

<script>
	var http = require("@/utils/http");
	var config = require("@/utils/config");
	import {
		encrypt
	} from '@/utils/crypto.js'
	export default {
		data() {
			return {
				idCardImg1: '/static/user/sfz1.png',
				idCardImg2: '/static/user/sfz2.png',
				formData: {
					idCard: '',
					realName: '',
				},
				rules: {
					'passWord': {
						type: 'string',
						min: 6,
						max: 20,
						required: true,
						message: '新登录密码不能为空，且长度在6-20位',
						trigger: ['blur', 'change']
					},
					'passWord2': [{
						type: 'string',
						min: 6,
						max: 20,
						required: true,
						message: '确认密码不能为空，且长度在6-20位',
						trigger: ['blur', 'change']
					}, {
						validator: (rule, value, callback) => {
							return value === this.formData.passWord
						},
						message: '确认密码与登录密码不一致'
					}, ],
				},
			};
		},
		methods: {
			passWordEye() {
				this.showPassWord = !this.showPassWord
			},
			passWordEye2() {
				this.showPassWord2 = !this.showPassWord2
			},
			submitForm() {
				this.$refs.uForm.validate().then(res => {
					var data = JSON.parse(JSON.stringify(this.form))
					data.oldPassWord = encrypt(data.oldPassWord)
					data.passWord = encrypt(data.passWord)
					data.passWord2 = encrypt(data.passWord2)
					http.request({
						url: "/user/updatePwd",
						method: "post",
						data: data,
						callBack: res => {
							uni.hideLoading();
							uni.showToast({
								title: "修改成功，请重新登录",
								icon: "none",
								duration: 1500
							})
							setTimeout(() => {
								this.toLogin()
							}, 1000)
							// wx.setStorageSync('loginResult', res);
							// wx.setStorageSync('token', 'bearer' + res.access_token);
						}
					})
				})
			},
		},
		onReady() {
			this.$refs.uForm.setRules(this.rules)
		},
	};
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
<template>
	<view class="con">
		<view class="title">
			<view>{{ form.userName ? '' : '你好，' }}</view>
			<view>{{ form.userName ? '账号密码登录' : '欢迎来到积分商城' }}</view>
		</view>
		<!-- 登录 -->
		<u--form labelPosition="left" :model="formData" :rules="rules" ref="uForm">
			<u-form-item prop="userName">
				<u-input type="number" border="bottom" placeholder="请输入手机号码" clearable maxlength="11" v-model="formData.userName"
					font-size="16">
					<template slot="prefix">
						<text class="u-m-r-10">
							+86 |
						</text>
					</template>
				</u-input>
			</u-form-item>
			<u-form-item prop="passWord">
				<u-input border="bottom" :password="!showPassword" placeholder="请输入6-20位密码" clearable v-model="formData.passWord"
					font-size="16">
					<template slot="suffix">
						<u-icon v-if="!showPassword" name="eye" size="22" color="#999" @tap="passWordEye"></u-icon>
						<u-icon v-else name="eye-fill" size="22" color="#2979ff" @tap="passWordEye"></u-icon>
					</template>
				</u-input>
			</u-form-item>
			<view class="u-m-t-30">
				<u-button type="primary" text="用户登录" @tap="login"></u-button>
			</view>
		</u--form>
		<view class="u-flex u-m-t-10">
			<view class="u-flex-1">
				<u--text @tap="toRegitser" :text="'注册账号'" :type="'primary'"></u--text>
			</view>
			<view class="">
				<u--text @tap="forgotPassword" :text="'忘记密码？'" :type="'info'"></u--text>
			</view>
		</view>
	</view>
</template>

<script>
	var config = require("@/utils/config");
	var http = require("@/utils/http");
	import {
		encrypt
	} from '@/utils/crypto.js'
	export default {
		data() {
			return {
				formData: {
					userName: '',
					passWord: '',
				},
				showPassword: false,
				rules: {
					'userName': {
						type: 'number',
						required: true,
						message: '手机号码不能为空',
						trigger: ['blur']
					},
					'passWord': {
						type: 'string',
						min: 6,
						max: 20,
						required: true,
						message: '登录密码不能为空，长度6-20位',
						trigger: ['blur']
					}
				},
			};
		},
		components: {},
		props: {},
		computed: {},
		/**
		 * 生命周期函数--监听页面加载
		 */
		onLoad: function(options) {
			if (options.isPersonalCenter) {
				this.isPersonalCenter = options.isPersonalCenter
			}
		},
		/**
		 * 生命周期函数--监听页面显示
		 */
		onShow: function() {
			this.showPassword = false
		},
		onReady() {
			this.$refs.uForm.setRules(this.rules)
		},
		methods: {
			passWordEye() {
				this.showPassword = !this.showPassword
			},
			/**
			 * 登录
			 */
			login() {
				this.$refs.uForm.validate().then(res => {
					var data = JSON.parse(JSON.stringify(this.formData))
					data.passWord = encrypt(data.passWord)
					data.payPassWord = encrypt(data.payPassWord)
					http.request({
						url: "/login",
						method: "post",
						data: data,
						callBack: res => {
							http.loginSuccess(res, () => {
								uni.showToast({
									title: "登录成功",
									icon: 'none',
									complete: () => {
										setTimeout(() => {
											wx.switchTab({
												url: '/pages/index/index'
											});
										}, 1000)
									}
								})
							})
						},
					})
				})
			},
			
			/**
			 * 去注册
			 */
			toRegitser() {
				uni.navigateTo({
					url: "/pages/user/register"
				})
			},
			/**
			 * 重置密码
			 */
			forgotPassword() {
				uni.navigateTo({
					url: "/pages/user/forget-password"
				})
			},
			
			/**
			 * 回到首页
			 */
			toIndex() {
				wx.switchTab({
					url: '/pages/index/index'
				});
			}
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
		height: 160rpx;
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

	.forgot-passWord {
		display: content;
	}

	/deep/ .u-form-item__body__right__message {
		margin-left: 0 !important;
	}
</style>
<template>
	<!--pages/register/register.wxml-->

	<view class="register">
		<view class="con">
			<view class="title">
				<view>{{ '' }}</view>
				<view>{{ '找回密码' }}</view>
			</view>
			<!-- 注册 -->
			<u--form labelPosition="left" :model="form" :rules="rules" ref="uForm">
				<u-form-item prop="userMobile" ref="userMobile">
					<u-input type="number" border="bottom" placeholder="请输入手机号码" clearable maxlength="11" v-model="form.userMobile" font-size="16">
						<template slot="prefix">
							+86 | </text>
						</template>
					</u-input>
				</u-form-item>
				<u-form-item prop="code" ref="code">
					<u-input type="number" border="bottom" placeholder="请输入6位数字验证码" clearable v-model="form.code" font-size="16">
						<template slot="suffix">
							<u-code ref="uCode" @change="codeChange" seconds="60" changeText="X秒重新获取"></u-code>
							<u--text @tap="getCode" :text="tips" :type="'primary'"></u--text>
						</template>
					</u-input>
				</u-form-item>
			</u--form>
			<view class="u-m-t-30">
				<u-button type="primary" text="找回密码" @tap="submitForm()"></u-button>
			</view>
			
			<!-- 显示密码 -->
			<u-modal :show="show" :content='content'></u-modal>
		</view>
	</view>
</template>

<script>
	// pages/register/register.js
	// var http = require("../../utils/http");
	// var util = require("../../utils/util.js");
	// import { encrypt } from '../../utils/crypto.js'

	export default {
		data() {
			return {
				show: false,
				content: undefined,
				form: {
					userMobile: undefined,
					code: undefined
				},
				rules: {
					'form.userMobile': {
						type: 'number',
						required: true,
						message: '手机号码不能为空',
						trigger: ['blur', 'change']
					},
					'form.code': {
						type: 'number',
						len: 6,
						required: true,
						message: '验证码不能为空，且长度为6',
						trigger: ['blur', 'change']
					},
				},
				codeTextType: 'primary',
				showPassword: false,
				showPassword2: false,
				tips: '',
				value: ''
			};
		},

		components: {},
		props: {},

		computed: {},

		/**
		 * 生命周期函数--监听页面加载
		 */
		onLoad: function(options) {},

		/**
		 * 生命周期函数--监听页面初次渲染完成
		 */
		onReady: function() {},

		/**
		 * 生命周期函数--监听页面显示
		 */
		onShow: function() {},

		/**
		 * 生命周期函数--监听页面隐藏
		 */
		onHide: function() {},

		/**
		 * 生命周期函数--监听页面卸载
		 */
		onUnload: function() {},

		/**
		 * 页面相关事件处理函数--监听用户下拉动作
		 */
		onPullDownRefresh: function() {},

		/**
		 * 页面上拉触底事件的处理函数
		 */
		onReachBottom: function() {},

		/**
		 * 用户点击右上角分享
		 */
		onShareAppMessage: function() {},
		methods: {
			submitForm() {
				var password = 123456
				this.content = `您的密码是：${password}`
				this.show = true
			},
			passwordEye() {
				this.showPassword = !this.showPassword
			},
			passwordEye2() {
				this.showPassword2 = !this.showPassword2
			},
			codeChange(text) {
				this.tips = text;
			},
			getCode() {
				if (this.$refs.uCode.canGetCode) {
					// 模拟向后端请求验证码
					uni.showLoading({
						title: '正在获取验证码'
					})
					setTimeout(() => {
						uni.hideLoading();
						// 这里此提示会被this.start()方法中的提示覆盖
						uni.$u.toast('验证码已发送');
						// 通知验证码组件内部开始倒计时
						this.$refs.uCode.start();
					}, 500);
				} else {
					uni.$u.toast('倒计时结束后再发送');
				}
			},

			/**
			 * 去登陆
			 */
			toLogin: function() {
				uni.navigateTo({
					url: "/pages/accountLogin/accountLogin"
				});
			},

			/**
			 * 回到首页
			 */
			toIndex: function() {
				uni.switchTab({
					url: '/pages/index/index'
				});
				// this.$Router.pushTab('/pages/index/index')
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
</style>
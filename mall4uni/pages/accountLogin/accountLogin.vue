<template>
	<view class="con">
		<view class="title">
			<view>{{ principal ? '' : '你好，' }}</view>
			<view>{{ principal ? '账号密码登录' : '欢迎来到黄金豆商城' }}</view>
		</view>
		<!-- 登录 -->
		<view class="login-form">
			<view :class="['item',errorTips==1? 'error':'']">
				<view class="account">
					<input type="number" @input="getInputVal" maxlength="11" data-type="account" placeholder-class="inp-palcehoder" placeholder="请输入手机号码"
					  style="width: calc(100% - 32rpx)">
					</input>
					<image v-if="principal" src="/static/icon/clear.png" style="width: 38rpx; height: 32rpx;" @click="principal == ''"></image>
				</view>
				<view class="error-text" v-if="errorTips==1"><text class="warning-icon">!</text>请输入手机号码</view>
			</view>
			<view :class="['item',errorTips==2? 'error':'']">
				<view class="account">
					<input :password="!showPassword" @input="getInputVal" data-type="password" placeholder-class="inp-palcehoder" placeholder="请输入密码"
						style="width: calc(100% - 100rpx)">
					</input>
					<view v-if="credentials" style="width: 100rpx;">
						<image src="/static/icon/clear.png" style="margin-right: 20rpx; width: 32rpx; height: 32rpx;" @click="credentials == undefined"></image>
						<image v-if="!showPassword" src="/static/icon/eye.png" style="width: 32rpx; height: 32rpx;" @click="changePassword"></image>
						<image v-else src="/static/icon/eye-active.png" style="width: 32rpx; height: 32rpx;" @click="changePassword"></image>
					</view>
				</view>
				<view class="error-text" v-if="errorTips==2"><text class="warning-icon">!</text>请输入密码</view>
			</view>
		</view>

		<view>
			<button :class="['authorized-btn', principal && credentials ? 'authorized-btn-active' : 'authorized-btn-inactive']"
				:disabled="!principal || !credentials" @tap="login">登录</button>
			<view class="operate">
				<view class="to-register" @tap="toRegitser"><text>注  册</text></view>
				<view class="forgot-password" @tap="forgotPassword"><text>忘记密码？</text></view>
			</view>
		</view>

	</view>
</template>

<script>
	var http = require("../../utils/http");
	var util = require('../../utils/util.js');
	import { encrypt } from '../../utils/crypto.js'
	import {
		AppType
	} from '../../utils/constant.js'
	export default {
		data() {
			return {
				principal: '', // 账号
				credentials: '', // 密码
				// isPersonalCenter: false, //是否从个人中心页面跳转过来
				errorTips: 0, //错误提示
				showPassword: false
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
		 * 生命周期函数--监听页面初次渲染完成
		 */
		onReady: function() {},

		/**
		 * 生命周期函数--监听页面显示
		 */
		onShow: function() {
			//头部导航标题
			uni.setNavigationBarTitle({
				title: "用户登录"
			});
			this.showPassword = false
		},

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
			changePassword: function() {
					this.showPassword = !this.showPassword;
			},
			/**
			 * 输入框的值
			 */
			getInputVal: function(e) {
				const type = e.currentTarget.dataset.type;
				if (type == 'account') {
					this.setData({
						principal: e.detail.value
					});
				} else if (type == 'password') {
					this.setData({
						credentials: e.detail.value
					});
				}
			},

			/**
			 * 登录
			 */
			login() {
				console.log('login');
				if (this.principal.length == 0) {
					this.setData({
						errorTips: 1
					})
					return
				} else if (this.credentials.length == 0) {
					this.setData({
						errorTips: 2
					})
					return
				} else {
					this.setData({
						errorTips: 0
					})
					var params = {
						url: "/login",
						method: "post",
						data: {
							"userName": this.principal,
							"passWord": encrypt(this.credentials)
						},
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
										},1000)

									}
								})
							})
						},
					}
					http.request(params)
				}
			},

			/**
			 * 去注册
			 */
			toRegitser() {
				uni.navigateTo({
					url: "/pages/register/register"
				})
			},

			/**
			 * 回到首页
			 */
			toIndex() {
				wx.switchTab({
					url: '/pages/index/index'
				});
				// this.$Router.pushTab('/pages/index/index')
			}

		},
		watch: {
			principal(nv, ov) {
				this.errorTips = 0
			}
		}
	};
</script>
<style>
	@import "./accountLogin.css";
</style>

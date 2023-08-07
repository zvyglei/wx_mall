<template>
	<!--pages/register/register.wxml-->

	<view class="register">
		<view class="con">
			<view class="title">
				<view>{{ principal ? '' : '你好' }}</view>
				<view>{{ '欢迎来到黄金豆商城' }}</view>
			</view>
			<!-- 登录 -->
			<view class="login-form">
				<view style="height: 84rpx;"></view>
				<view :class="['item',errorTips==1? 'error':'']">
					<view class="account">
						<input type="tel" @input="getInputVal" maxlength="11" data-type="account" placeholder-class="inp-palcehoder" placeholder="请输入手机号码"
							style="width: calc(100% - 170rpx);"></input>
						<text class="uni-icon" v-if="principal" @click="principal == undefined">&#xe434;</text>
					</view>
					<view class="error-text" v-if="errorTips==1"><text class="warning-icon">!</text>请输入手机号码</view>
				</view>
				<view class="tip">未注册的手机号验证后自动创建账户</view>
			</view>

			<view>
				<button :class="['authorized-btn', principal && principal.length == 11 ? 'authorized-btn-active' : 'authorized-btn-inactive']"
					:disabled="!principal" @tap="login">获取短信验证码</button>
				<view class="operate">
					<view class="to-register" @tap="toLogin"><text>密码登录</text></view>
				</view>
			</view>

		</view>
	</view>
</template>

<script>
	// pages/register/register.js
	var http = require("../../utils/http");
	var util = require("../../utils/util.js");
	import { encrypt } from '../../utils/crypto.js'

	export default {
		data() {
			return {
				errorTips: 0, // 输入错误提示:  1账号输入  2密码输入
				principal: '', // 账号
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
		onShow: function() {
			//头部导航标题
			uni.setNavigationBarTitle({
				title: "用户注册"
			});
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
			 * 注册
			 */
			toRegister() {

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
					uni.showLoading();
					var params = {
						url: "/user/register",
						method: "post",
						data: {
							// appType: 1,
							// 应用类型 1小程序 2微信公众号 3 PC 4 H5
							userName: this.principal,
							passWord: encrypt(this.credentials),
						},
						callBack: res => {
							uni.hideLoading();
							uni.showToast({
								title: "注册成功，请登录",
								icon: "none",
								duration: 1500
							})
							setTimeout(() => {
								uni.navigateTo({
									url: "/pages/accountLogin/accountLogin"
								})
							}, 1800)
							// wx.setStorageSync('loginResult', res);
							// wx.setStorageSync('token', 'bearer' + res.access_token);
						}
					};
					http.request(params);
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
<style>
	@import "./register.css";
</style>

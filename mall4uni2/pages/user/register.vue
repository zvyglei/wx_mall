<template>
	<view class="register">
		<view class="con">
			<view class="title">
				<view>{{ '用户注册' }}</view>
			</view>
			<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
			<u--form labelPosition="left" :model="formData" :rules="rules" ref="uForm">
				<u-form-item prop="userMobile">
					<u-input type="number" border="bottom" placeholder="请输入手机号码" clearable maxlength="11"
						v-model="formData.userMobile" font-size="16">
						<template slot="prefix">
							<text class="u-m-r-10">
								+86 |
							</text>
						</template>
					</u-input>
				</u-form-item>
				<u-form-item prop="code" ref="code">
					<u-input type="number" border="bottom" maxlength="6" placeholder="请输入6位数字验证码" clearable
						v-model="formData.code" font-size="16">
						<template slot="suffix">
							<u-code ref="uCode" @change="codeChange" seconds="60" changeText="X秒重新获取"></u-code>
							<u--text @tap="getCode" :text="tips" :type="'primary'"
								v-if="formData.userMobile && formData.userMobile.length === 11"></u--text>
						</template>
					</u-input>
				</u-form-item>
				<u-form-item prop="passWord" ref="passWord">
					<u-input border="bottom" :password="!showPassWord" placeholder="请输入6-20位登录密码" clearable
						v-model="formData.passWord" font-size="16">
						<template slot="suffix">
							<u-icon v-if="!showPassWord" name="eye" size="22" color="#999" @tap="passWordEye"></u-icon>
							<u-icon v-else name="eye-fill" size="22" color="#2979ff" @tap="passWordEye"></u-icon>
						</template>
					</u-input>
				</u-form-item>
				<u-form-item prop="passWord2" ref="passWord2">
					<u-input border="bottom" :password="!showPassWord2" placeholder="登录密码确认" clearable
						v-model="formData.passWord2" font-size="16">
						<template slot="suffix">
							<u-icon v-if="!showPassWord2" name="eye" size="22" color="#999" @tap="passWordEye2"></u-icon>
							<u-icon v-else name="eye-fill" size="22" color="#2979ff" @tap="passWordEye2"></u-icon>
						</template>
					</u-input>
				</u-form-item>
				<u-form-item prop="payPassWord" ref="payPassWord">
					<u-input type="number" border="bottom" :password="!showPayPassWord" placeholder="请输入6位数字支付密码" clearable
						v-model="formData.payPassWord" font-size="16" maxlength="6">
						<template slot="suffix">
							<u-icon v-if="!showPayPassWord" name="eye" size="22" color="#999" @tap="passWordEye3"></u-icon>
							<u-icon v-else name="eye-fill" size="22" color="#2979ff" @tap="passWordEye3"></u-icon>
						</template>
					</u-input>
				</u-form-item>
				<!-- <u-form-item prop="refereeCode" ref="refereeCode">
					<u-input type="number" border="bottom" v-model="formData.refereeCode" placeholder="选填:邀请码(6位)" clearable
						font-size="16" maxlength="6"></u-input>
				</u-form-item> -->
			</u--form>
			<!-- <view class="u-m-t-20">
				<u-checkbox-group>
					<u-checkbox :customStyle="{marginBottom: '8px'}" @change="changeUserSerive">
						<u--text text="用户服务协议" type="primary" @tap="showUserService"></u--text>
					</u-checkbox>
					<u-checkbox :customStyle="{marginBottom: '8px'}" @change="changePrivacy">
						<u--text text="隐私政策" type="primary" @tap="showPrivacy"></u--text>
					</u-checkbox>
				</u-checkbox-group>
			</view> -->
			<view class="u-m-t-30">
				<u-button type="primary" text="注 册" @tap="register"></u-button>
			</view>
			<view class="u-flex u-m-t-10">
				<u--text @tap="toLogin" :text="'用户登录'" :type="'primary'"></u--text>
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
				tips: '',
				idCardImg1: '/static/user/sfz1.png',
				idCardImg2: '/static/user/sfz2.png',
				formData: {
					userMobile: '',
					code: '',
					passWord: '',
					passWord2: '',
					payPassWord: ''
				},
				showPassWord: false,
				showPassWord2: false,
				showPayPassWord: false,
				rules: {
					'userMobile': {
						type: 'number',
						required: true,
						message: '手机号码不能为空',
						trigger: ['blur', 'change']
					},
					'code': {
						type: 'number',
						len: 6,
						required: true,
						message: '验证码不能为空，且长度为6',
						trigger: ['blur', 'change']
					},
					'passWord': {
						type: 'string',
						min: 6,
						max: 20,
						required: true,
						message: '登录密码不能为空，且长度在6-20位',
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
					'payPassWord': {
						type: 'string',
						len: 6,
						required: true,
						message: '支付密码不能为空，且为6位数字',
						trigger: ['blur', 'change']
					},
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
			passWordEye3() {
				this.showPayPassWord = !this.showPayPassWord
			},
			codeChange(text) {
				this.tips = text;
			},
			getCode() {
				if (this.$refs.uCode.canGetCode) {
					uni.showLoading({
						title: '正在获取验证码'
					})
					http.request({
						url: "/sms/send",
						method: "post",
						data: {
							type: 'register',
							mobile: this.formData.userMobile
						},
						callBack: res => {
							setTimeout(() => {
								uni.hideLoading();
								// 这里此提示会被this.start()方法中的提示覆盖
								uni.$u.toast('验证码已发送');
								// 通知验证码组件内部开始倒计时
								this.$refs.uCode.start();
							}, 500);
						}
					})
				} else {
					uni.$u.toast('倒计时结束后再发送');
				}
			},

			register() {
				this.$refs.uForm.validate().then(res => {
					uni.showLoading();
					var ths = this
					var data = JSON.parse(JSON.stringify(this.formData))
					data.passWord = encrypt(data.passWord)
					data.payPassWord = encrypt(data.payPassWord)
					wx.login({
						success(wxres) {
							data.wxcode = wxres.code
							http.request({
								url: "/user/register",
								method: "post",
								data: data,
								callBack: res => {
									uni.hideLoading();
									uni.showToast({
										title: "注册成功，请登录",
										icon: "none",
										duration: 1500
									})

									uni.hideLoading();

									setTimeout(() => {
										ths.toLogin()
									}, 1000)
									// wx.setStorageSync('loginResult', res);
									// wx.setStorageSync('token', 'bearer' + res.access_token);
								}
							})
						}
					})
				})
			},

			/**
			 * 去登陆
			 */
			toLogin: function() {
				uni.navigateTo({
					url: "/pages/user/login"
				});
			},

			/**
			 * 回到首页
			 */
			toIndex: function() {
				uni.switchTab({
					url: '/pages/index/index'
				});
			}
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
<template>
	<view class="register">
		<view class="con">
			<view class="title">
				<view>{{ '' }}</view>
				<view>{{ '重置密码' }}</view>
			</view>
			<!-- 注册 -->
			<u--form labelPosition="left" :model="formData" :rules="rules" ref="uForm">
				<u-form-item prop="userMobile" ref="userMobile">
					<u-input type="number" border="bottom" placeholder="请输入手机号码" clearable maxlength="11"
						v-model="formData.userMobile" font-size="16">
						<template slot="prefix">
							+86 |
						</template>
					</u-input>
				</u-form-item>
				<u-form-item prop="code" ref="code">
					<u-input type="number" border="bottom" maxlength="6" placeholder="请输入6位数字验证码" clearable v-model="formData.code" font-size="16">
						<template slot="suffix">
							<u-code ref="uCode" @change="codeChange" seconds="60" max changeText="X秒重新获取"></u-code>
							<u--text @tap="getCode" :text="tips" :type="'primary'"
								v-if="formData.userMobile && formData.userMobile.length === 11"></u--text>
						</template>
					</u-input>
				</u-form-item>
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
	var config = require("@/utils/config");
	var http = require("@/utils/http");
	import {
		encrypt
	} from '@/utils/crypto.js'
	export default {
		data() {
			return {
				tips: '',
				showPassWord: false,
				showPassWord2: false,
				idCardImg1: '/static/user/sfz1.png',
				idCardImg2: '/static/user/sfz2.png',
				formData: {
					userMobile: '',
					code: '',
				},
				rules: {
					'userName': {
						type: 'number',
						required: true,
						message: '手机号码不能为空',
						trigger: ['blur']
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
				}
			}
		},
		methods: {
			passWordEye() {
				this.showPassWord = !this.showPassWord
			},
			passWordEye2() {
				this.showPassWord2 = !this.showPassWord2
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
							type: 'forget',
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
			submitForm() {
				var data = JSON.parse(JSON.stringify(this.formData))
				data.passWord = encrypt(data.passWord)
				this.$refs.uForm.validate().then(res => {
					http.request({
						url: "/user/updatePwd",
						method: "post",
						data: data,
						callBack: res => {
							uni.showToast({
								title: "修改成功，请登录",
								icon: "none",
								duration: 1500
							})
							
							uni.hideLoading();
							
							setTimeout(() => {
								this.toLogin()
							}, 1000)
						}
					})
				})
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

	/deep/ .u-form-item__body__right__message {
		margin-left: 0 !important;
	}
</style>

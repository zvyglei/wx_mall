<template>
	<view class="register">
		<view class="con">
			<view class="title">
				<view>{{ '会员认证' }}</view>
			</view>
			<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
			<u--form labelPosition="left" :model="formData" :rules="rules" ref="uForm">
				<u-form-item prop="idCard">
					<u-input v-model="formData.idCard" border="bottom" placeholder="请输入身份证号" clearable font-size="16"
						maxlength="18"></u-input>
				</u-form-item>
				<u-form-item prop="realName">
					<u-input v-model="formData.realName" border="bottom" placeholder="请输入真实姓名" clearable font-size="16"
						maxlength="5"></u-input>
				</u-form-item>
				<u-form-item>
					<u-upload @afterRead="afterRead1" name="idCardImg1" previewImage :maxCount="1">
						<image :src="idCardImg1" mode="widthFix" style="width: 88vw;"></image>
					</u-upload>
				</u-form-item>
				<u-form-item>
					<u-upload @afterRead="afterRead2" name="idCardImg2" previewImage :maxCount="1">
						<image :src="idCardImg2" mode="widthFix" style="width: 88vw"></image>
					</u-upload>
				</u-form-item>
				<view class="u-m-t-30">
					<u-button type="primary" text="提交认证" @tap="submit"></u-button>
				</view>
			</u--form>
		</view>
	</view>
</template>

<script>
	var config = require("@/utils/config");
	var http = require("@/utils/http");
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
					'idCard': [{
							type: 'regexp',
							required: true,
							message: '身份证号不能为空',
							trigger: ['blur']
						},
						{
							validator: (rule, value, callback) => {
								return RegExp(
									/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/
								).test(value)
							},
							message: '身份证号格式错误',
							trigger: ['blur']
						}
					],
					'realName': [{
							type: 'string',
							required: true,
							message: '真实姓名不能为空',
							trigger: ['blur', 'change']
						},
						{
							validator: (rule, value, callback) => {
								return RegExp(
									/[\u4e00-\u9fa5]/
								).test(value)
							},
							message: '姓名必须是中文',
							trigger: ['blur']
						}
					]
				},
			};
		},
		methods: {
			// 新增图片
			async afterRead1(file) {
				this.formData.idCardImg1 = file.url
				uni.uploadFile({
					url: config.domain + '/api/file/upload/element', // 仅为示例，非真实的接口地址
					filePath: file.file.url,
					name: 'file',
					success: (res) => {
						var pic = JSON.parse(res.data).data
						this.formData.idCardImg1 = pic
						this.idCardImg1 = config.previewUrl + pic
					}
				});
			},
			// 新增图片
			async afterRead2(file) {
				this.formData.idCardImg2 = file.url
				uni.uploadFile({
					url: config.domain + '/api/file/upload/element', // 仅为示例，非真实的接口地址
					filePath: file.file.url,
					name: 'file',
					success: (res) => {
						var pic = JSON.parse(res.data).data
						this.formData.idCardImg2 = pic
						this.idCardImg2 = config.previewUrl + pic
					}
				});
			},
			submit() {
				this.$refs.uForm.validate().then(res => {
					if (!this.formData.idCardImg1) {
						uni.$u.toast('身份证人像面未上传')
						return
					}
					if (!this.formData.idCardImg2) {
						uni.$u.toast('身份证国徽面未上传')
						return
					}
					http.request({
						url: "/p/user/verify",
						method: "post",
						data: this.formData,
						callBack: res => {
							uni.$u.toast('提交成功，请等待审核');
							setTimeout(() => {
								wx.switchTab({
									url: '/pages/me/index'
								});
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
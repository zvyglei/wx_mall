<template>
	<view class="register">
		<view class="con">
			<view class="title">
				<view>{{ '会员认证' }}</view>
			</view>
			<!-- 会员认证 -->
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
					<u-upload @afterRead="afterRead1" name="sfz1" previewImage :maxCount="1">
						<image :src="sfz1" mode="widthFix" style="width: 88vw;"></image>
					</u-upload>
				</u-form-item>
				<u-form-item>
					<u-upload @afterRead="afterRead2" name="sfz2" previewImage :maxCount="1">
						<image :src="sfz2" mode="widthFix" style="width: 88vw"></image>
					</u-upload>
				</u-form-item>
			</u--form>
			<view class="u-m-t-30">
				<u-button type="primary" text="提交认证" @tap="submit"></u-button>
			</view>
		</view>
	</view>
</template>

<script>
	var config = require("@/utils/config");
	var http = require("@/utils/http");
	export default {
		data() {
			return {
				sfz1: '/static/user/sfz1.png',
				sfz2: '/static/user/sfz2.png',
				formData: {
					idCard: undefined,
					realName: undefined
				},
				rules: {
					'idCard': {
						type: 'string',
						required: true,
						message: '身份证号不能为空',
						trigger: ['blur', 'change']
					},
					'realName': {
						type: 'string',
						required: true,
						message: '真实姓名不能为空',
						trigger: ['blur', 'change']
					},
				}
			};
		},

		components: {},
		props: {},

		computed: {},


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
			// 新增图片
			async afterRead1(file) {
				this.formData.sfz1 = file.url
				uni.uploadFile({
					url: config.domain + '/api/file/upload/element', // 仅为示例，非真实的接口地址
					filePath: file.file.url,
					name: 'file',
					success: (res) => {
						var pic = JSON.parse(res.data).data
						this.formData.sfz1 = pic
						this.sfz1 = config.previewUrl + pic
					}
				});
			},
			// 新增图片
			async afterRead2(file) {
				this.formData.sfz2 = file.url
				uni.uploadFile({
					url: config.domain + '/api/file/upload/element', // 仅为示例，非真实的接口地址
					filePath: file.file.url,
					name: 'file',
					success: (res) => {
						var pic = JSON.parse(res.data).data
						this.formData.sfz2 = pic
						this.sfz2 = config.previewUrl + pic
					}
				});
			},
			submit() {
				this.$refs.uForm.validate()
				// this.$refs.uForm.validate().then(res => {
				// 	if (!this.formData.sfz1) {
				// 		uni.$u.toast('身份证人像面未上传')
				// 		return
				// 	}
				// 	if (!this.formData.sfz2) {
				// 		uni.$u.toast('身份证国徽面未上传')
				// 		return
				// 	}
				// 	uni.$u.toast('ok')
				// })
			}
		},
		onLoad: function(options) {
			this.$refs.uForm.setRules(this.rules)
		},
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
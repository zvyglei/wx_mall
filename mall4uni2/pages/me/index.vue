<template>
	<view class="content" style="padding-top: 0;">
		<!-- 导航栏 -->
		<!-- <u-navbar :is-back="false" title="" title-color="white" :background="background" :border-bottom="false">
			<view class="slot-wrap">
				
				<view class="map-wrap"></view>		
				<view class="search-wrap" ></view>
				<view class="navbar-right" >
					<view class="message-box right-item">
						<u-icon name="bell" size="26"></u-icon>
						<u-badge count="18" size="mini" :offset="[-15, -15]"></u-badge>
					</view>
					<view class="dot-box right-item">
						<u-icon name="setting" size="26"></u-icon>
						<u-badge size="mini" :is-dot="true" :offset="[-6, -6]"></u-badge>
					</view>
				</view>
				
			</view>
		</u-navbar> -->


		<!-- 用户背景 -->
		<view class="user-bg">
			<view class="u-flex user-box u-p-l-30 u-p-r-30 u-p-t-20 u-p-b-30" @click="toLogin">
				<button class="avatar-wrapper u-m-30" open-type="chooseAvatar" @chooseavatar="onChooseAvatar"
					style="border-radius: 50%; padding: 0">
					<image v-if="userInfo.pic" :src="userInfo.pic" class="avatar" style="border: 1rpx solid #eee;"></image>
					<u-avatar v-else size="68" :fade="true" duration="450" :lazy-load="true"></u-avatar>
				</button>
				<view class="u-flex-1">
					<view v-if="userInfo.verifyStatus == 0" class="u-font-16 u-p-b-20">游客</view>
					<view v-if="userInfo.verifyStatus == 1" class="u-font-16 u-p-b-20">游客 | 待审核</view>
					<view v-else-if="userInfo.verifyStatus == 2" class="u-font-16 u-p-b-20"
						style="color: #f6e5a3; font-weight: bold;">
						{{ userInfo.realName || '-' }}&nbsp;|&nbsp;
						<text class="u-font-16 u-m-l-10">代理</text>
					</view>
					<view class="u-font-14 ">{{ userInfo.userMobile ? '手机号码：' : '点击登录' }}<text
							style="letter-spacing: 1px;">{{ userInfo.userMobile }}</text></view>
				</view>
				<view class="u-m-l-10 u-p-10" v-if="!userInfo.userMobile">
					<u-icon name="arrow-right" color="#ffffff" size="18"></u-icon>
				</view>
			</view>
		</view>

		<!-- vip -->
		<!-- <view class="vip-card u-p-l-40 u-p-r-30 u-p-b-30">
			<view class="u-m-r-10 u-p-t-30 u-flex" style="float: left;">
				<u-icon name="integral" color="#f6e5a3"></u-icon>
				<text class="u-m-l-20">商城尊享会员</text>
			</view>
			<view class="u-p-t-30 u-flex" style="float: right;">
				<u-icon name="level" color="#f6e5a3"></u-icon> 1
				<text class="u-font-12 u-m-l-20">钻石会员</text>
			</view>
		</view> -->

		<!-- 余额 -->
		<view class="card" style="position: relative; top:-75upx;">
			<!-- card -->
			<view class="u-flex card-hader">
				<u-icon name="red-packet" size="26"></u-icon>
				<text class="u-m-l-20">我的账户</text>
			</view>
			<view class="card-body">
				<u-grid :col="2" :border="false">
					<u-grid-item @tap="toBill">
						<text class="u-font-18 u-m-t-10" style="color: #eb2444;">
							<text class="">{{wxs.parsePrice(userInfo.score || 0 )[0]}}</text>
							<text class="">.{{wxs.parsePrice(userInfo.score || 0 )[1]}}</text>
						</text>
						<view class="grid-text">黄金豆</view>
					</u-grid-item>
					<u-grid-item @tap="toBill">
						<text class="u-font-16 u-m-t-18">查看</text>
						<view class="grid-text">积分记录</view>
					</u-grid-item>
					<!-- <u-grid-item>
						<text class="u-font-18 u-m-t-10">0</text>
						<view class="grid-text">优惠券</view>
					</u-grid-item> -->
					<!-- <u-grid-item>
						<text class="u-font-18">0</text>
						<view class="grid-text">积分</view>
					</u-grid-item> -->
				</u-grid>
			</view>
		</view>


		<!-- 订单 -->
		<view class="card" style="position: relative; top:-60upx;">
			<!-- card -->
			<view class="u-flex card-hader" @click="toOrder(0)">
				<u-icon name="order" size="26"></u-icon>
				<text class="u-m-l-20 u-flex-1">全部订单</text>
				<u-icon name="arrow-right" size="18" class="u-m-t-10" style="float: right;"></u-icon>
			</view>
			<view class="card-body">
				<u-grid :col="4" :border="false">
					<u-grid-item @click="toOrder(1)">
						<u-icon name="red-packet" size="26" class="u-m-t-10"></u-icon>
						<view class="grid-text u-font-10 u-m-t-10">待付款</view>
					</u-grid-item>
					<u-grid-item @click="toOrder(2)">
						<u-icon name="car" size="26" class="u-m-t-10"></u-icon>
						<view class="grid-text u-font-10 u-m-t-10">待发货</view>
					</u-grid-item>
					<u-grid-item @click="toOrder(3);">
						<u-icon name="bag" size="26" class="u-m-t-10"></u-icon>
						<view class="grid-text u-font-10 u-m-t-10">待收货</view>
					</u-grid-item>
					<u-grid-item @click="toOrder(5)">
						<u-icon name="checkmark-circle" size="26" class="u-m-t-10"></u-icon>
						<view class="grid-text u-font-10 u-m-t-10">已完成</view>
					</u-grid-item>
				</u-grid>
			</view>

		</view>

		<!-- 我的服务 -->
		<view class="card" style="position: relative; top:-45upx; margin-bottom: -50upx;">
			<!-- card -->
			<view class="u-flex card-hader">
				<u-icon name="grid" size="26"></u-icon>
				<text class="u-m-l-20 ">我的服务</text>
			</view>
			<view class="card-body">
				<u-grid :col="4" :border="false">
					<u-grid-item @tap="toTransfer">
						<u-icon name="rmb-circle" size="26" class="u-m-t-10"></u-icon>
						<view class="grid-text u-font-10 u-m-t-10">账号转账</view>
					</u-grid-item>
					<u-grid-item @tap="toAddr">
						<u-icon name="map" size="26" class="u-m-t-10"></u-icon>
						<view class="grid-text u-font-10 u-m-t-10">地址管理</view>
					</u-grid-item>
					<u-grid-item @tap="toVerify">
						<u-icon name="checkmark" size="26" class="u-m-t-10"></u-icon>
						<view class="grid-text u-font-10 u-m-t-10">会员认证</view>
					</u-grid-item>
					<u-grid-item @tap="toVerifyInfo">
						<u-icon name="info" size="26" class="u-m-t-10"></u-icon>
						<view class="grid-text u-font-10 u-m-t-10">认证信息</view>
					</u-grid-item>
					<u-grid-item>
						<button class="open-type-btn" open-type="contact">
							<u-icon name="chat" size="26" class="u-m-t-10" style="width: auto;"></u-icon>
							<view class="grid-text u-font-10" style="padding-top: 0;">联系客服</view>
						</button>
					</u-grid-item>
					<u-grid-item>
						<button class="open-type-btn" open-type="share">
							<u-icon name="share-square" size="26" class="u-m-t-10" style="width: auto;"></u-icon>
							<view class="grid-text u-font-10" style="padding-top: 0;">邀请好友</view>
						</button>
					</u-grid-item>
					<u-grid-item @tap="toResetPassword">
						<u-icon name="edit-pen" size="26" class="u-m-t-10"></u-icon>
						<view class="grid-text u-font-10 u-m-t-10">修改密码</view>
					</u-grid-item>
					<u-grid-item @tap="logout">
						<u-icon name="man-delete" size="26" class="u-m-t-10"></u-icon>
						<view class="grid-text u-font-10 u-m-t-10">退出登录</view>
					</u-grid-item>
				</u-grid>
			</view>
		</view>

	</view>
</template>

<script module="wxs" lang="wxs" src="@/wxs/number.wxs"></script>
<script>
	// import searchNavbar from '@/components/search-navbar.vue';
	var http = require("@/utils/http");
	export default {
		data() {
			return {
				avatarUrl: undefined,
				userInfo: {},
				background: {
					backgroundImage: 'linear-gradient(90deg, rgb(42,132,255), rgb(234,175,200))'
				},
				show: true,
			}
		},
		// onShareAppMessage:function(){
		//     wx.showShareMenu({
		//       withShareTicket:true,
		//       menu:['shareAppMessage','shareTimeline']
		//     })

		//     return {
		//       title:'分享时标题',

		//       imageUrl:''
		//     }
		//   },
		//   //用户点击右上角分享朋友圈
		//   onShareTimeline:function(){
		//     return {
		//       title:'分享时标题',
		//       query:{
		//         key:value
		//       },
		//       imageUrl:'分享时图片底图'
		//     }
		//   },


		async onShow() {
			uni.showLoading();
			this.userInfo = {}
			await http.request({
				url: "/p/user/userInfo",
				method: "get",
				callBack: res => {
					this.userInfo = res
				},
			})
			uni.hideLoading();
		},
		methods: {
			onChooseAvatar(e) {
				this.userInfo.pic = e.detail.avatarUrl
				http.request({
					url: "/p/user/setUserInfo",
					method: "PUT",
					data: e.detail
				});
			},
			testtt() {
				// wx.getSetting({
				//   withSubscriptions: true,
				//   success (res) {
				//     // console.log(res.authSetting)
				//     // res.authSetting = {
				//     //   "scope.userInfo": true,
				//     //   "scope.userLocation": true
				//     // }
				//     // console.log(res.subscriptionsSetting)
				//     // res.subscriptionsSetting = {
				//     //   mainSwitch: true, // 订阅消息总开关
				//     //   itemSettings: {   // 每一项开关
				//     //     SYS_MSG_TYPE_INTERACTIVE: 'accept', // 小游戏系统订阅消息
				//     //     SYS_MSG_TYPE_RANK: 'accept'
				//     //     zun-LzcQyW-edafCVvzPkK4de2Rllr1fFpw2A_x0oXE: 'reject', // 普通一次性订阅消息
				//     //     ke_OZC_66gZxALLcsuI7ilCJSP2OJ2vWo2ooUPpkWrw: 'ban',
				//     //   }
				//     // }

				//   }
				// })
				// wx.login({
				// 	success(res) {
				// 		http.request({
				// 			url: "/sms/wxmsg?code=" + res.code,
				// 			method: "get",
				// 			callBack: res => {

				// 			},
				// 		})
				// 	}
				// })
				// wx.requestSubscribeMessage({
				// 	tmplIds: ['73l3g4BHdbnl2WeLzEhWeyhHSrrb4E3mcuxGrWkvZ4I'],
				// 	success (res) {
				// 		wx.login({
				// 			success(res) {
				// 				http.request({
				// 					url: "/sms/wxmsg?code=" + res.code,
				// 					method: "get",
				// 					callBack: res => {

				// 					},
				// 				})
				// 			}
				// 		})
				// 	}
				// })


				wx.getUserProfile({
					desc: '用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
					success: (res) => {
						console.log(res);
						this.userInfo.pic = res.userInfo.avatarUrl
					}
				})

				// wx.login({
				//   success (res) {
				// 		console.log(res);
				//     // if (res.code) {
				//     //   //发起网络请求
				//     //   wx.request({
				//     //     url: 'https://example.com/onLogin',
				//     //     data: {
				//     //       code: res.code
				//     //     }
				//     //   })
				//     // } else {
				//     //   console.log('登录失败！' + res.errMsg)
				//     // }
				//   }
				// })
				// wx.getSetting({
				//   success(res) {
				//     if (!res.authSetting['scope.userInfo']) {
				//       wx.authorize({
				//         scope: 'scope.userInfo',
				//         success () {
				//           wx.getUserInfo({
				//             success: function(res) {
				//               var userInfo = res.userInfo
				//               var nickName = userInfo.nickName
				//               var avatarUrl = userInfo.avatarUrl
				//               var gender = userInfo.gender //性别 0：未知、1：男、2：女
				//               var province = userInfo.province
				//               var city = userInfo.city
				//               var country = userInfo.country
				//             }
				//           })
				//         }
				//       })
				//     }
				//   }
				// })
			},
			toLogin() {
				if (!this.userInfo.userMobile) {
					uni.navigateTo({
						url: "/pages/user/login"
					})
				}
			},
			toBill() {
				uni.navigateTo({
					url: "/pages/user/bill"
				})
			},
			toOrder(sts) {
				uni.navigateTo({
					url: "/pages/order/list?sts=" + (sts || 0)
				})
			},
			toTransfer() {
				http.request({
					url: "/p/user/transfer/2",
					method: "get",
					callBack: res => {
						if (res === 1) {
							uni.navigateTo({
								url: "/pages/me/transfer?score=" + this.userInfo.score
							})
						} else {
							uni.$u.toast('暂停使用')
						}
					}
				})
			},
			toAddr() {
				uni.navigateTo({
					url: "/pages/address/list"
				})
				// uni.navigateTo({
				// 	url: '/pages/address/delivery-address?order=1'
				// });
			},
			toVerify() {
				if (this.userInfo.verifyStatus == 1) {
					uni.$u.toast('已提交认证，请等待审核！')
					return
				}
				if (this.userInfo.verifyStatus == 2) {
					uni.$u.toast('您已通过认证！')
					return
				}
				uni.navigateTo({
					url: "/pages/user/vip-verify"
				})
			},
			toVerifyInfo() {
				if (this.userInfo.verifyStatus == 0) {
					uni.$u.toast('请先进行认证')
					return
				}
				if (this.userInfo.verifyStatus == 1) {
					uni.$u.toast('已提交认证，请等待审核！')
					return
				}
				uni.navigateTo({
					url: "/pages/user/vip-verify-info"
				})
			},
			toResetPassword() {
				if (!this.userInfo.userMobile) {
					uni.$u.toast('您未登录')
					return
				}
				uni.navigateTo({
					url: "/pages/user/reset-password"
				})
			},
			logout() {
				http.request({
					url: "/logOut",
					method: "post",
					callBack: res => {
						uni.showToast({
							title: "退出成功",
							icon: 'none',
							complete: () => {
								setTimeout(() => {
									wx.switchTab({
										url: '/pages/index/index'
									});
								}, 1000)
							}
						})
					},
				})
			}
		},
		components: {}
	}
</script>

<style lang="scss" scoped>
	.avatar {
		width: 120rpx;
		height: 120rpx;
		border-radius: 50%;
		display: block;
		border: 1rpx solid #eee;
	}

	.open-type-btn {
		text-align: center;
		text-align: -webkit-center;
		padding-top: 20rpx;
		background: none;
	}

	.open-type-btn:after {
		border: none;
	}

	.open-type-btn /deep/.u-icon--right {
		flex-direction: column;
	}

	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		background-color: #F8F8F8;
	}

	.logo {
		height: 200rpx;
		width: 200rpx;
		margin-top: 100rpx;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 50rpx;
	}

	.text-area {
		display: flex;
		justify-content: center;
	}

	.title {
		font-size: 28rpx;
		color: $u-content-color;
	}

	.link-demo {
		margin-top: 80rpx;
	}

	// 
	.user-bg {
		background: linear-gradient(90deg, rgb(42, 132, 255), rgb(234, 175, 200));
		width: 100vw;
		height: 280upx;
	}

	// 导航栏
	.navbar-right {
		margin-right: 24rpx;
		display: flex;
	}

	.search-wrap {
		margin: 0 20rpx;
		flex: 1;
	}

	.right-item {
		margin: 0 12rpx;
		position: relative;
		color: #ffffff;
		display: flex;
	}

	.message-box {}

	.slot-wrap {
		display: flex;
		align-items: center;
		flex: 1;
	}

	.map-wrap {
		display: flex;
		align-items: center;
		padding: 4px 6px;
		// background-color: rgba(240,240, 240, 0.35);
		color: #fff;
		font-size: 20rpx;
		border-radius: 100rpx;
		margin-left: 30rpx;
	}

	.map-wrap-text {
		padding: 0 6rpx;
	}

	.camera {
		width: 54px;
		height: 44px;

		&:active {
			background-color: #ededed;
		}
	}

	.user-box {
		// background-color: #fff;
		color: #FFFFFF;
	}

	.vip-card {
		background-image: url(/static/vip-card.png);
		background-size: cover;
		color: #f6e5a3;
		width: 90vw;
		position: relative;
		top: -100upx;
	}

	.u-card-wrap {
		background-color: $u-bg-color;
		padding: 1px;
	}

	.u-body-item {
		font-size: 32rpx;
		color: #333;
		padding: 20rpx 10rpx;
	}

	.u-body-item image {
		width: 120rpx;
		flex: 0 0 120rpx;
		height: 120rpx;
		border-radius: 8rpx;
		margin-left: 12rpx;
	}

	.card {
		box-shadow: 0px 2px 8px 0px rgba(27, 25, 86, 0.06);
	}

	.card-hader {
		text-align: left;
		width: 92vw;
		padding: 15upx 20upx;
		background-color: #FFFFFF;
		border: 1upx solid #FDF6EC;
	}

	.card-body {
		width: 92vw;
		padding: 15upx 20upx;
		background-color: #FFFFFF;
		border: 1upx solid #FDF6EC;
		border-top: none;
	}

	.grid-text {
		font-size: 14px;
		color: #555;
		padding: 10rpx 0 20rpx 0rpx;
		/* #ifndef APP-PLUS */
		box-sizing: border-box;
		/* #endif */
	}
</style>
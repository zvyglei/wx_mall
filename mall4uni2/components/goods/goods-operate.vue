<template>
	<view class="slot">
		<view class="'left">
			<view class="item" @tap="toHome">
				<!-- <u-icon size="24" name="server-fill"></u-icon>
				<text>客服</text> -->
				<u-icon size="28" name="home"></u-icon>
				<text>首页</text>
			</view>
			<view class="item" @tap="toCart">
				<!-- <u-icon size="24" name="star-fill" color="#409eff" v-if="data.isCollection"></u-icon>
				<u-icon size="24" name="star" v-else></u-icon>
				<text v-if="data.isCollection">已收藏</text>
				<text v-else>收藏</text> -->
				<u-icon size="28" name="shopping-cart"></u-icon>
				<text>购物车</text>
			</view>
			<!-- <view class="item" @click="goShoppingCart">
				<u-icon size="36" name="shopping-cart"></u-icon>
				<text>购物车</text>
			</view> -->
		</view>
		<view class="right">
			<u-button v-if="data.flashSale === 0"  style="width: 248rpx;" type="info" @click="addShoppingCart">加入购物车</u-button>
			<u-button v-if="data.flashSale === 0" style="width: 248rpx;" type="primary" @click="buyNow">立即购买</u-button>
			<view v-else class="">
				<u-button v-show="!lose" style="width: 248rpx;" type="error" @click="buyNow" :disabled="!start && (timeData.hours > 0 || timeData.minutes > 0 || timeData.seconds > 0)">
					<u-count-down v-if="!start" :time="startCount" format="HH:mm:ss" @change="onChange" @finish="onFinish">
						<view class="time" style="width: 280rpx;">
							{{ timeData.hours >= 10 ? timeData.hours: '0' + timeData.hours}} :
							{{ timeData.minutes >= 10 ? timeData.minutes: '0' + timeData.minutes }} :
							{{ timeData.seconds >= 10 ? timeData.seconds: '0' + timeData.seconds }}
							开始抢购
						</view>
					</u-count-down>
					<u-count-down v-if="start" :time="saleCount" format="HH:mm:ss" @change="onSaleChange" @finish="onSaleFinish" :disabled="saleTimeData.hours > 0 || saleTimeData.minutes > 0 || saleTimeData.seconds > 0">
						<view class="time" style="width: 280rpx;">
							{{ saleTimeData.hours >= 10 ? saleTimeData.hours: '0' + saleTimeData.hours}} :
							{{ saleTimeData.minutes >= 10 ? saleTimeData.minutes: '0' + saleTimeData.minutes }} :
							{{ saleTimeData.seconds >= 10 ? saleTimeData.seconds: '0' + saleTimeData.seconds }}
							已开抢
						</view>
					</u-count-down>
				</u-button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'goods-operate',
		props: {
			// 数据源
			data: {
				type: Object,
				default: () => {
					return {};
				}
			}
		},
		data() {
			return {
				lose: false,
				start: false,
				timeData: {},
				saleTimeData: {},
				startCount: 0,
				saleCount: 0
			}
		},
		/**
		 * 生命周期函数--监听页面显示
		 */
		onLoad: function() {

		},
		methods: {
			onChange(e) {
				if (e.hours > 0 || e.minutes > 0 || e.seconds > 0) {
					this.timeData = e
					this.start = false
				}
			},
			onFinish() {
				this.start = true
				this.saleCount = new Date(this.data.flashSaleEnd) - new Date()
			},
			onSaleChange(e) {
				this.saleTimeData = e
			},
			onSaleFinish() {
				this.lose = true
			},
			init(data) {
				if (data.flashSale === 1) {
					var start = new Date(data.flashSaleStart)
					var end = new Date(data.flashSaleEnd)
					if (start > new Date()) {
						this.startCount = start - new Date()
						this.start = false
					} else if (start < new Date() && new Date() < end) {
						this.saleCount = end - new Date()
						this.start = true
					}
				}
			},
			// 加入购物车
			addShoppingCart() {
				this.$emit('addShoppingCart');
			},

			// 立即购买
			buyNow() {
				
				this.$emit('buyNow');
			},

			// 去首页
			toHome() {
				wx.switchTab({
					url: '/pages/index/index'
				});
			},

			// 去购物车
			toCart() {
				wx.switchTab({
					url: '/pages/cart/basket'
				});
			}
		}
	};
</script>

<style lang="scss" scoped>
	/deep/ .u-count-down {
		color: #fff !important;
		font-size: 28rpx !important;
		width: 150rpx;
	}

	.slot {
		position: fixed;
		bottom: 0;
		left: 0;
		width: 100%;
		min-height: 100rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		background-color: #ffffff;
		z-index: 199;
		box-shadow: 0px 2px 8px 0px rgba(27, 25, 86, 0.06);
		padding-bottom: constant(safe-area-inset-bottom);
		padding-bottom: env(safe-area-inset-bottom);

		.left {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.item {
				color: #171717;
				display: flex;
				justify-content: center;
				align-items: center;
				flex-wrap: wrap;

				text {
					padding-top: 4rpx;
					text-align: center;
					width: 100%;
					font-size: 20rpx;
				}
			}
		}

		.right {
			display: flex;
			align-items: center;
			white-space: nowrap;

			/deep/button {
				padding: 0 40rpx;
				font-size: 30rpx;
				height: 100%;
				line-height: 100rpx;
				border-radius: 0 !important;

				&::after {
					border: initial;
				}
			}
		}
	}

	.goods-list {
		background-color: #fff;
		padding: 0 30rpx 30rpx 30rpx;
		margin-bottom: 16rpx;
	}

	.details-html {
		background-color: #fff;
		padding: 0 30rpx 30rpx 30rpx;
		margin-bottom: 100rpx;
	}

	.view-more {
		display: flex;
		justify-content: center;
		align-items: center;
		padding-top: 24rpx;
	}
</style>
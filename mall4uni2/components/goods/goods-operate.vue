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
			<u-button style="width: 248rpx;" type="info" @click="addShoppingCart">加入购物车</u-button>
			<u-button style="width: 248rpx;" type="primary" @click="buyNow">立即购买</u-button>
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
		methods: {
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
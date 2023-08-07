<template>
	<view class="slot" :style="{'filter': this.data.flashSale === 1 && !flashSale() ? 'contrast(0.5)' : ''}"
		@click="toDetail(data.prodId)">
		<view style="position: relative;">
			<image style="width: 100%; height: 340rpx;" :src="data.pic"></image>
			<view class="" v-if="data.flashSale">
				<image v-if="flashSale()" style="height: 50rpx; width: 80rpx; position: absolute; left: 10rpx; top: 10rpx;"
					src="@/static/goods/flash-sale.png"></image>
				<image v-else style="height: 100rpx; width: 100rpx; position: absolute; left: 0; top: 0;" class="flash-sale"
					src="@/static/goods/lose.png"></image>
			</view>
		</view>
		<view class="inner">
			<text class="title">{{ data.prodName }}</text>
			<view class="flash-sale-time u-font-27" v-if="data.flashSale === 1">
				<text>{{ data.flashSaleStart }}</text>
				<text>&nbsp;开抢</text>
			</view>
			<view class="info">
				<view class="money">
					<view class="now">
						<image src="/static/jindou.png" style="width: 30rpx; height: 30rpx; margin-bottom: -6rpx;"></image>
						<text class="">{{wxs.parsePrice(data.price)[0]}}</text>
						<text class="">.{{wxs.parsePrice(data.price)[1]}}</text>
					</view>
					<view class="old u-m-l-15" v-if="data.oriPrice">
						<text class="">{{wxs.parsePrice(data.oriPrice)[0]}}</text>
						<text class="">.{{wxs.parsePrice(data.oriPrice)[1]}}</text>
					</view>
				</view>
				<view class="sale">
					<text>{{ data.flashSale === 1 ? '已抢购' : '已兑换' }}</text>
					<text>{{ data.soldNum }}</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script module="wxs" lang="wxs" src="@/wxs/number.wxs"></script>
<script>
	export default {
		props: {
			// 数据源
			data: {
				type: Object,
				default: () => {
					return {};
				}
			},
			// 是否显示原价
			showOldMoney: {
				type: Boolean,
				default: false
			}
		},
		methods: {
			flashSale() {
				return new Date(this.data.flashSaleEnd) > new Date()
			},
			toDetail(prodId) {
				if (this.data.flashSale === 0 || this.flashSale()) {
					uni.navigateTo({
						url: `/pages/shop/detail?prodId=${prodId}`
					});
				}
			}
		}
	};
</script>

<style lang="scss" scoped>
	.slot {
		display: inline-block;
		overflow: hidden;
		margin-bottom: 16rpx;
		width: 100%;

		.pic {
			overflow: hidden;
			width: 100%;
			background-color: red;
		}

		.inner {
			padding: 10rpx 0;
		}

		.flash-sale-time {
			display: flex;
			justify-content: space-between;
			margin-top: -10rpx;
			color: #fd4558;
		}

		.title {
			width: 100%;
			word-break: break-all;
			white-space: pre-wrap;
			font-weight: 400;
			font-size: 28rpx;
			color: #171716;
			margin-bottom: 12rpx;
			// 默认单行，如果需要瀑布流则改成多行
			display: -webkit-box;
			overflow: hidden;
			-webkit-line-clamp: 2;
			-webkit-box-orient: vertical;
		}

		.info {
			display: flex;
			justify-content: space-between;
			align-items: flex-end;

			.money {
				display: flex;
				align-items: flex-end;

				.now {
					text {
						font-size: 28rpx;
						color: #fd4558;
					}
				}

				.old {
					text {
						font-size: 24rpx;
						color: #8f92a1;
						text-decoration-line: line-through;
					}
				}
			}

			.sale {
				font-size: 26rpx;
				font-weight: 400;
				color: #8f92a1;
			}
		}
	}
</style>
<template>
	<view class="slot">
		<view class="count">
			<view class="price">
				<image src="/static/jindou.png" style="width: 38rpx; height: 38rpx; margin-bottom: -6rpx;"></image>
				<text>{{ data.price }}</text>
				<text>￥{{ data.oriPrice }}</text>
			</view>
			<view class="sale">
				<text>已兑换</text>
				<text>{{ data.soldNum || 0 }}</text>
			</view>
		</view>
		<view class="title">
			<text>{{ data.prodName }}</text>
		</view>
		<view class="brief">
			<text>{{ data.brief }}</text>
		</view>
	</view>
</template>

<script>
	export default {
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
			return {};
		}
	};
</script>

<style lang="scss" scoped>
	.slot {
		margin-bottom: 16rpx;
		background-color: #ffffff;
	}

	.count {
		display: flex;
		justify-content: space-between;
		align-items: flex-end;
		padding: 26rpx 30rpx;

		.price {
			text:nth-child(1) {
				font-size: 28rpx;
				color: #fd4558;
			}

			text:nth-child(2) {
				font-size: 40rpx;
				color: #fd4558;
				margin-right: 24rpx;
			}

			text:nth-child(3) {
				font-size: 28rpx;
				color: #cccccc;
				text-decoration-line: line-through;
			}
		}

		.sale {
			text:nth-child(1) {
				font-size: 28rpx;
				color: #696969;
				margin-right: 8rpx;
			}

			text:nth-child(2) {
				font-size: 28rpx;
				color: #696969;
			}
		}
	}

	.title {
		padding: 0 30rpx 16rpx 30rpx;

		text {
			vertical-align: middle;
			font-size: 32rpx;
			font-weight: 500;
			color: #16162e;
		}
	}
	
	.brief {
		padding: 0 30rpx 16rpx 30rpx;
		text {
			vertical-align: middle;
			font-size: 28rpx;
			color: #8f92a1;
		}
	}
</style>
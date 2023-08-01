<template>
	<view class="slot" @click="toDetail(data.prodId)">
		<image style="width: 100%; height: 340rpx;" :src="data.pic"></image>
		<view class="inner">
			<text class="title">{{ data.prodName }}</text>
			<view class="info">
				<view class="money">
					<view class="now">
						<image src="/static/jindou.png" style="width: 30rpx; height: 30rpx; margin-bottom: -6rpx;"></image>
						<text>{{ data.price }}</text>
					</view>
					<view class="old u-m-l-15" v-if="data.oriPrice">
						<text>￥{{ data.oriPrice }}</text>
					</view>
				</view>
				<view class="sale">
					<text>已兑换</text>
					<text>{{ data.soldNum }}</text>
				</view>
			</view>
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
			},
			// 是否显示原价
			showOldMoney: {
				type: Boolean,
				default: false
			}
		},
		methods: {
			toDetail(prodId) {
				uni.navigateTo({
					url: `/pages/shop/detail?prodId=${prodId}`
				});
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
					text:nth-child(1) {
						font-size: 20rpx;
						color: #fd4558;
					}

					text:nth-child(2) {
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
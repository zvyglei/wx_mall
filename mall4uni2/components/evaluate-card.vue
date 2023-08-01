<template>
	<view class="slot" :style="showBorderBottom ? '' : 'border-bottom:none;margin-bottom:0'">
		<view class="user">
			<view class="info">
				<u-avatar size="50" src="/static/user/1.png"></u-avatar>
				<view class="basic">
					<view class="name">{{ data.userName }}</view>
					<view class="evaluate">
						<u-rate v-model="data.star" readonly active-color="#409eff" inactive-color="#b2b2b2"></u-rate>
					</view>
				</view>
			</view>
			<view class="date">{{ data.date }}</view>
		</view>
		<view class="evaluate">{{ data.content }}</view>
	</view>
</template>

<script>
	export default {
		name: 'evaluate-card',
		props: {
			// 数据源
			data: {
				type: Object,
				default: () => {
					return {};
				}
			},
			// 是否显示底部边框
			showBorderBottom: {
				type: Boolean,
				default: false
			}
		},
		data() {
			return {};
		}
	};
</script>

<style lang="scss" scoped>
	.slot {
		border-bottom: 1rpx solid #efefef;
		margin-bottom: 24rpx;
	}

	.user {
		display: flex;
		justify-content: space-between;
		align-items: flex-end;

		.info {
			display: flex;
			justify-content: flex-start;

			.basic {
				margin-left: 16rpx;
				display: flex;
				justify-content: flex-start;
				flex-wrap: wrap;
				align-content: space-between;

				.name {
					width: 100%;
					font-size: 28rpx;
					color: #171717;
					// 为了与 u-rate 组件左侧视觉对齐
					padding-left: 10rpx;
				}

				.evaluate {}
			}
		}

		.date {
			font-size: 26rpx;
			color: #8f92a1;
		}
	}

	.evaluate {
		padding-top: 26rpx;
		padding-bottom: 24rpx;
		font-size: 28rpx;
		color: #171717;
	}

	.pictures {}
</style>
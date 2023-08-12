<template>
	<u-popup :show="show" mode="bottom" round="10" closeable safeAreaInsetBottom @close="show=false">
		<view class="info">
			<view class="pic"><u-image :src="pic" width="190rpx" height="190rpx"></u-image></view>
			<view class="desc">
				<view class="price">
					<image src="/static/jindou.png" style="width: 38rpx; height: 38rpx; margin-bottom: -6rpx;"></image>
					<text>{{wxs.parsePrice(data.price)[0]}}.{{wxs.parsePrice(data.price)[1]}}</text>
				</view>
				<!-- <view class="select">
					<text>已选</text>
					<text>{{ selectSkuLabel }}</text>
				</view> -->
				<view class="select">
					<text>库存</text>
					<text>{{ stocks }}</text>
				</view>
			</view>
		</view>
		<view class="sku" v-show="data.skuList && data.skuList.length > 1">
			<view class="title">规格</view>
			<view class="list">
				<view class="u-page__tag-item" v-for="(item, index) in data.skuList" :key="index">
					<u-tag class="item" :text="item.properties" size="large" :plain="item.skuId !== selectSkuValue"
						:name="item.skuId" @click="selectSku(item)">
					</u-tag>
				</view>
			</view>
		</view>
		<view class="num">
			<view class="title">数量</view>
			<u-number-box v-model="num" @change="changeNum" :max="stocks" :disabled="data.flashSale === 1"></u-number-box>
		</view>
		<view class="operate" v-if="data.flashSale === 0">
			<u-button style="width: 50%;" type="info" @click="addCartHandle" throttleTime='50'>加入购物车</u-button>
			<u-button style="width: 50%;" type="primary" @click="bugNowHandle" throttleTime='50'>立即购买</u-button>
		</view>
	</u-popup>
</template>

<script module="wxs" lang="wxs" src="@/wxs/number.wxs"></script>
<script>
	export default {
		data() {
			return {
				show: false,
				// 数据源
				data: {},
				pic: '',
				stocks: 0,
				// 已选择的sku
				selectSkuLabel: '',
				selectSkuValue: '0',
				// 数量
				num: 1
			};
		},
		methods: {
			// 打开popup
			open(data, selectSku) {
				this.data = data;
				this.pic = data.pic;
				this.stocks = data.totalStocks;
				this.selectSkuValue = selectSku.skuId;
				this.show = true;
			},

			// 切换sku
			selectSku(item) {
				this.pic = item.pic
				this.stocks = item.stocks;
				this.selectSkuValue = item.skuId;
				this.$emit('change', item);
			},

			// 数量改变时
			changeNum(e) {
				this.$emit('changeNum', e.value)
			},

			// 关闭
			close() {},

			// 加入购物车
			addCartHandle() {
				this.$emit('addShoppingCart')
			},

			// 立即购买
			bugNowHandle() {
				this.$emit('buyNow')
			}
		}
	};
</script>

<style lang="scss" scoped>
	.info {
		padding: 30rpx 30rpx 0 30rpx;
		display: flex;
		align-items: flex-end;

		.pic {
			margin-right: 30rpx;
		}

		.desc {
			.price {
				margin-bottom: 20rpx;

				text:nth-child(1) {
					font-size: 24rpx;
					color: #fd4558;
					vertical-align: bottom;
				}

				text:nth-child(2) {
					font-size: 36rpx;
					color: #fd4558;
					vertical-align: bottom;
				}
			}

			.select {
				text:nth-child(1) {
					font-size: 24rpx;
					color: #696969;
					margin-right: 20rpx;
				}

				text:nth-child(2) {
					font-size: 24rpx;
					color: #171717;
				}
			}
		}
	}

	.sku {
		padding: 30rpx 30rpx 0 30rpx;

		.title {
			font-size: 28rpx;
			color: #17171C;
			margin-bottom: 16rpx;
		}

		.list {
			display: flex;
			justify-content: flex-start;
			align-items: center;
			flex-wrap: wrap;

		}
	}

	.u-page__tag-item {
		margin-bottom: 24rpx;
		margin-right: 20rpx;
		// background-color: $app-theme-sku-gray-color;
		// font-size: 24rpx;
		// color: $app-theme-text-black-color;
		// border: 1rpx solid $app-theme-sku-gray-color;
		// &.select {
		// 	color: $app-theme-color;
		// 	border: 1rpx solid $app-theme-color;
		// 	background-color: rgba($app-theme-color, 0.08);
		// }
	}

	.num {
		padding: 30rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;

		.title {
			font-size: 28rpx;
			color: #171717;
		}
	}

	.operate {
		display: flex;
		align-items: center;

		/deep/button {
			height: 100rpx;
			line-height: 100rpx;
			border-radius: 0 !important;

			&::after {
				border: initial;
			}
		}
	}
</style>
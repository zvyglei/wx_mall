<template>
	<view class="page">
		<!-- 购物车列表 -->
		<view class="cart-list">
			<view class="item" v-for="(goods, index) in goodsList" :key="index">
				<view class="left">
					<u-checkbox-group @change="checkGoods(goods)">
						<u-checkbox icon-size="16" :checked="goods.checked"></u-checkbox>
					</u-checkbox-group>
				</view>
				<view class="right">
					<view class="img">
						<image src="https://acbigdata.haier.net/lyyy/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20220419151456.png"
							mode=""></image>
					</view>
					<view class="info">
						<view class="title">{{ goods.name }}</view>
						<view class="sku">{{ goods.sku }}</view>
						<view class="operate">
							<view class="price">
								<text>￥</text>
								<text>{{ goods.price }}</text>
							</view>
							<view class="num"><u-number-box v-model="goods.num"></u-number-box></view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 底部按钮 -->
		<view class="btn">
			<view class="inner">
				<view class="check">
					<u-checkbox-group>
						<u-checkbox icon-size="16" v-model="checkAllStauts" @change="checkAllGoods"></u-checkbox>
					</u-checkbox-group>
					<text>全选</text>
				</view>
				<view class="right">
					<view class="price">
						<text>合计：</text>
						<text>￥</text>
						<text>{{ totalPrice }}</text>
					</view>
					<u-button type="primary"
						@click="goPayResult">去结算({{ goodsList.filter(item => item.checked).length }})</u-button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	// 组件
	export default {
		data() {
			return {
				totalPrice: 0,
				checkAllStauts: false,
				goodsList: []
			};
		},
		computed: {
			checkAll() {
				this.totalPrice = 0
				this.goodsList.map(item => (item.checked ? (this.totalPrice += Number(item.price) * item.num) : null));
				if (this.goodsList.filter(item => item.checked).length == this.goodsList.length) {
					this.checkAllStauts = true;
				} else {
					this.checkAllStauts = false;
				}
			}
		},
		methods: {
			checkGoods(goods) {
				goods.checked = !goods.checked
				this.totalPrice = 0
				this.goodsList.map(item => (item.checked ? (this.totalPrice += Number(item.price) * item.num) : null));
				if (this.goodsList.filter(item => item.checked).length == this.goodsList.length) {
					this.checkAllStauts = true;
				} else {
					this.checkAllStauts = false;
				}
			},
			checkAllGoods() {
				if (this.checkAllStauts) {
					this.goodsList.forEach(item => item.checked = false);
				} else {
					this.goodsList.forEach(item => item.checked = true);
				}
			},
			goPayResult() {
				uni.navigateTo({
					url: '/pages/order/submit'
				});
			}
		}
	};
</script>

<style lang="scss" scoped>
	.page {
		background-color: #F8F8F8;
		padding: 24rpx;
		height: 92vh;
		overflow: auto;
	}

	/deep/.u-icon {
		display: flex !important;
	}

	.cart-list {
		.item {
			display: flex;
			align-items: center;
			background-color: #ffffff;
			padding: 30rpx 24rpx 30rpx 24rpx;
			border-bottom: 1rpx solid #efefef;

			.left {
				display: flex;
				align-items: center;
				width: 46rpx;
			}

			.right {
				width: calc(100% - 46rpx);
				display: flex;
				align-items: center;

				.img {
					width: 160rpx;
					height: 160rpx;
					margin-right: 20rpx;

					image {
						width: 100%;
						height: 100%;
					}
				}

				.info {
					width: 418rpx;

					.title {
						font-size: 28rpx;
						font-family: PingFang-SC-Regular, PingFang-SC;
						font-weight: 400;
						color: #171717;
						padding-bottom: 16rpx;
					}

					.sku {
						font-size: 22rpx;
						font-family: PingFang-SC-Regular, PingFang-SC;
						font-weight: 400;
						background: #fafafa;
						border-radius: 2px;
						color: #696969;
						padding: 4rpx 16rpx;
					}

					.operate {
						padding-top: 30rpx;
						display: flex;
						align-items: center;
						justify-content: space-between;

						.price {
							text:nth-child(1) {
								font-size: 24rpx;
								font-family: PingFangSC-Medium, PingFang SC;
								font-weight: 500;
								color: #fd4558;
							}

							text:nth-child(2) {
								font-size: 36rpx;
								font-family: PingFangSC-Regular, PingFang SC;
								font-weight: 400;
								color: #fd4558;
							}
						}

						.num {}
					}
				}
			}
		}
	}

	.btn {
		position: fixed;
		bottom: 0;
		left: 0;
		width: 100%;
		background-color: #ffffff;

		.inner {
			border-top: 1rpx solid #e6e6e6;
			display: flex;
			justify-content: space-between;
			align-items: center;

			.check {
				padding-left: 30rpx;
				display: flex;
				align-items: center;

				text {
					font-size: 28rpx;
					font-family: PingFangSC-Regular, PingFang SC;
					font-weight: 400;
					color: #171717;
				}
			}

			.right {
				display: flex;
				align-items: center;
				white-space: nowrap;

				.price {
					margin-right: 54rpx;

					text:nth-child(1) {
						font-size: 28rpx;
						font-family: PingFangSC-Regular, PingFang SC;
						font-weight: 400;
						color: #171717;
					}

					text:nth-child(2) {
						font-size: 24rpx;
						font-family: PingFangSC-Medium, PingFang SC;
						font-weight: 500;
						color: #fd4558;
					}

					text:nth-child(3) {
						font-size: 36rpx;
						font-family: PingFangSC-Regular, PingFang SC;
						font-weight: 400;
						color: #fd4558;
					}
				}

				/deep/button {
					padding: 0 40rpx;
					font-size: 30rpx;
					height: 100%;
					line-height: 100rpx;
					border-radius: 0 !important;
				}
			}
		}
	}
</style>
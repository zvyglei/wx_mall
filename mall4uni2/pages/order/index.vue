<template>
	<view class="page">
		<view class="" style="background-color: #fff;">
			<view class="u-m-t-20 u-m-b-0 u-m-l-20 u-m-r-20">
				<u-search shape="square" :showAction="false" clearabled placeholder="搜索全部订单" v-model="keyword"></u-search>
			</view>
			<view class="u-m-b-20">
				<u-tabs :list="list1" :scrollable="false" lineWidth="30"></u-tabs>
			</view>
		</view>
		<view class="u-m-30 u-p-b-30">
			<u-list @scrolltolower="scrolltolower">
				<u-list-item v-for="(item, index) in orderList" :key="index">
					<view class="card-body u-p-20 u-m-b-30">
						<view class="u-flex">
							<u--image :src="item.goods.img" width="180rpx" height="180rpx" radius="5"></u--image>
							<view class="info">
								<view class="title">{{ item.goods.title }}</view>
								<view class="desc">
									<view class="sku item">
										<text>规格</text>
										<text>{{ item.goods.sku }}</text>
									</view>
									<view class="express item">
										<text>运费</text>
										<text>{{ item.goods.express || '包邮' }}</text>
									</view>
								</view>
								<view class="integral">
									<image src="/static/jindou.png" style="width: 30rpx; height: 30rpx; margin-bottom: -8rpx;"></image>
									<text class="u-m-r-10">1500</text>
									<text class="price">￥</text>
									<text class="price">180.00</text>
								</view>
							</view>
							<view class="status">
								<view class="status-label">
									<slot><u--text type="primary" text="待付款"></u--text></slot>
								</view>
								<view class="num">
									<text>×</text>
									<text>1</text>
								</view>
							</view>
						</view>
						<u-line class="u-m-t-20 u-m-b-20"></u-line>
						<view class="" style="text-align: right;">
							<u-button type="info" size="small" shape="circle" text="取消订单" plain></u-button>
							<u-button type="primary" size="small" shape="circle" text="去付款"></u-button>
						</view>
					</view>
				</u-list-item>
			</u-list>
			<u-loadmore :status="status" />
		</view>
	</view>
</template>

<script>
	export default {
		components: {},
		data() {
			return {
				list1: [{
					name: '全部',
				}, {
					name: '待付款',
				}, {
					name: '待发货'
				}, {
					name: '待收货'
				}, {
					name: '待评价'
				}, {
					name: '退款售后'
				}],
				// 加载前值为loadmore，加载中为loading，没有数据为nomore
				status: 'nomore',
				current: 0,
				orderList: [],
			};
		},
		onLoad(ops) {
		},
		methods: {
			// 切换tab的回调
			changeTab(e) {
				const {
					tab,
					index
				} = e;
				this.tabIndex = index;
				let arr = [];
				for (var i = 0; i < orderList.length; i++) {
					console.log(orderList[i]);
					if (tab.value == '') {
						arr.push(orderList[i]);
						continue;
					}
					if (orderList[i].status == tab.value) {
						arr.push(orderList[i]);
					}
				}
				this.orderList = arr;
			},
			scrolltolower() {},
		}
	};
</script>

<style lang="scss">
	.page {
		background-color: #F8F8F8;
	}

	.card-body {
		background-color: #FFFFFF;
		border: 1upx solid #FDF6EC;
		box-shadow: 0px 2px 8px 0px rgba(27, 25, 86, 0.06);
	}

	::v-deep .u-tabs__wrapper__nav__item__text {
		font-size: 26rpx !important;
	}

	::v-deep .u-tabs__wrapper__nav__item {
		padding: 0 !important;
	}

	::v-deep .u-line {
		margin: 20rpx 0 !important;
	}
	
	::v-deep .u-button {
		display: inline-grid !important;
		width: auto !important;
		margin-left: 20rpx;
	}
	
	::v-deep .u-button--plain.u-button--info {
		color: #7b7b7b !important;
		border-color: #b1b1b1 !important;
	}

	.info {
		width: 600rpx;
		margin: 0 20rpx;

		.title {
			color: #171717;
			width: 100%;
			display: -webkit-box;
			overflow: hidden;
			-webkit-line-clamp: 2;
			-webkit-box-orient: vertical;
			font-size: 28rpx;
			font-weight: 400;
			margin-bottom: 14rpx;
		}

		.desc {
			display: flex;
			justify-content: flex-start;
			align-items: flex-end;
			margin-bottom: 18rpx;

			.item {
				color: #6A696C;
				margin-right: 24rpx;

				text:nth-child(1) {
					font-size: 24rpx;
					margin-right: 8rpx;
				}

				text:nth-child(2) {
					font-size: 24rpx;
				}
			}
		}
		
		.integral {
			color: #171717;
			font-size: 28rpx;
		}

		.price {
			color: #969696;
			font-size: 22rpx;
			text-decoration: line-through;
		}
	}

	.status {
		width: 140rpx;

		.status-label {
			width: 100%;
			text-align: right;
			font-size: 28rpx;
			margin-bottom: 100rpx;
			height: 28rpx;
		}

		.num {
			width: 100%;
			text-align: right;

			text:nth-child(1) {
				font-size: 24rpx;
				vertical-align: bottom;
			}

			text:nth-child(2) {
				font-size: 28rpx;
				vertical-align: bottom;
			}
		}
	}
</style>
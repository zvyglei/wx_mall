<template>
	<view style="padding: 30rpx;">
		<!-- 轮播图 -->
		<u-swiper :list="indexImgs" :height="180"></u-swiper>
		<!-- u-grid -->
		<view style="padding-bottom: 32rpx;">
			<u-grid :border="false" col="3">
				<u-grid-item v-for="(listItem,listIndex) in list" :key="listIndex">
					<image :src="listItem.imgPath" class="grid-img" @tap="gridClick(listItem.toUrl)"
						:style="[{ background: listItem.bg }, { 'box-shadow': '0 0 5px 0 ' + listItem.bg + 'a1'}]"></image>
					<text class="grid-text">{{listItem.title}}</text>
				</u-grid-item>
			</u-grid>
			<u-toast ref="uToast" />
		</view>

		<!-- 用户兑换记录 -->
		<view class="card">
			<view class="card-hader">
				<text>这些人已兑换</text>
			</view>
			<view class="card-body u-p-t-20">
				<swiper class="tab-right" vertical="true" autoplay="true" circular="true" interval="2000"
					display-multiple-items='3' style="height: 100%;">
					<view class="right-item record" style="font-size: 28rpx;">
						<swiper-item class="" v-for="(item, index) in records" :key="index">
							<view class="u-m-b-10" style="color: #757575;">
								<text class="u-m-r-10">用户：{{item.realName || '李**'}}</text>
								<text class="u-m-r-10">手机号：{{item.userMobile || '192****2372'}}</text>
								<text class="u-m-r-10">成功兑换！！！</text>
							</view>
							<view class="u-flex">
								<u--image :src="item.pic || '/static/goods/meigui.png'" width="90rpx" height="90rpx" radius="5"
									class="u-flex-1"></u--image>
								<view class="info">
									<view class="title u-flex-1">{{ item.prodName || '平阴新玫瑰花平阴新玫瑰花养颜好气色养颜好气色' }}</view>
									<view class="status">
										<view class="num">
											<text>×</text>
											<text>{{item.prodCount || '1'}}</text>
										</view>
									</view>
								</view>
								<view class="price">
									<view class="price-text">
										<text class="price">￥</text>
										<text class="price">{{item.oriPrice || '127.00'}}</text>
									</view>
									<view class="integral">
										<image src="/static/jindou.png" style="width: 30rpx; height: 30rpx; margin-bottom: -8rpx;"></image>
										<text>{{item.price || '1270'}}</text>
									</view>
								</view>
							</view>
						</swiper-item>
					</view>
				</swiper>
			</view>
		</view>
	</view>
</template>

<script>
	var http = require("@/utils/http");
	export default {
		data() {
			return {
				// noticeContentList: [ '寒雨连江夜入吴','平明送客楚山孤','洛阳亲友如相问', '一片冰心在玉壶'  ],
				// indexImgs: [
				// 	'https://cdn.uviewui.com/uview/swiper/swiper1.png',
				// 	'https://cdn.uviewui.com/uview/swiper/swiper2.png',
				// 	'https://cdn.uviewui.com/uview/swiper/swiper3.png',
				// ],
				indexImgs: [],
				records: [],
				list: [{
						bg: '#0aaffd',
						imgPath: '/static/grid/pintuan.png',
						title: '拼团'
					},
					{
						bg: '#fedc3a',
						imgPath: '/static/grid/kanjia.png',
						title: '砍价'
					},
					{
						bg: '#fe8c17',
						imgPath: '/static/grid/miaosha.png',
						title: '秒杀'
					},
					{
						bg: '#787afb',
						imgPath: '/static/grid/yuyue.png',
						title: '在线预约'
					},
					{
						bg: '#f76658',
						imgPath: '/static/grid/jifen.png',
						title: '积分商城',
						toUrl: '/pages/shop/index'
					},
					{
						bg: '#30d2cc',
						imgPath: '/static/grid/haodian.png',
						title: '好店推荐'
					},
				],
			}
		},
		onLoad() {
			http.request({
				url: "/indexImgs",
				method: "get",
				callBack: res => {
					this.indexImgs = res.map(x => x.imgUrl)
				}
			})
			http.request({
				url: "/orderRecords",
				method: "get",
				callBack: res => {
					this.records = res.length > 4 ? res : [1, 2, 3, 4, 5]
				}
			})

			uni.showModal({
				title: "温馨提示",
				content: "尊贵的女士/先生，欢迎您来到江涛人力家园，感谢多年以来你对我们公司的大力支持，在这里我们为你准备了丰富多样的产品任你挑选免费包邮到家，感谢你的一路相伴，让我们不离不弃共创美好未来!",
				confirmText: "我知道了",
				showCancel: false,
				success: res => {

				}
			})
		},
		onShow() {
			
		},
		methods: {
			gridClick(url) {
				if (!url) {
					uni.showToast({
						title: '暂未开放',
						icon: 'none'
					})
					return
				}
				uni.switchTab({
					url: url
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.record-title {
		height: 90rpx;
		line-height: 90rpx;
		background-color: rgba(234, 84, 4, 1);
		color: #fff;
		font-size: 36rpx;
		text-align: center;
		font-family: -apple-system;
	}

	.grid-img {
		width: 54rpx;
		height: 54rpx;
		padding: 22rpx;
		border-radius: 40%;
		margin-top: 32rpx;
	}

	.grid-text {
		font-size: 28rpx;
		color: #555;
		margin-top: 10rpx;
	}

	.card {
		box-shadow: 0px 2px 8px 0px rgba(27, 25, 86, 0.06);
	}

	.card-hader {
		background: linear-gradient(90deg, #2a84ff, #eaafc8);
		border-radius: 4px 4px 0 0;
		color: #fff;
		text-align: left;
		padding: 18rpx 20upx;
		background-color: #FFFFFF;
		border: 1upx solid #FDF6EC;
	}

	.card-body {
		height: 36.5vh;
		color: #373737;
		padding: 0 20upx;
		background-color: #FFFFFF;
		border: 1upx solid #FDF6EC;
		border-top: none;
	}

	.card-body .record:not(:first-child) {
		border-top: 1rpx solid #efefef;
	}

	.info {
		width: 800rpx;
		margin: 0 20rpx;

		.title {
			width: 90%;
			display: -webkit-box;
			overflow: hidden;
			-webkit-line-clamp: 1;
			-webkit-box-orient: vertical;
			font-size: 28rpx;
			font-weight: 400;
			margin-bottom: 14rpx;
		}
	}

	.price {
		width: 150rpx;
		text-align: right;

		.price-text {
			margin-bottom: 14rpx;
			color: #969696;
			text-decoration: line-through;
		}
	}
</style>
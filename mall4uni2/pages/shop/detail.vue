<template>
	<view class="page">
		<!-- 轮播图 -->
		<u-swiper height="420" :list="imgs" imgMode="scaleToFill" @change="e => currentNum = e.current" :autoplay="false"
			indicatorStyle="right: 20px">
			<view slot="indicator" class="indicator-num">
				<text class="indicator-num__text">{{ currentNum + 1 }}/{{ imgs.length }}</text>
			</view>
		</u-swiper>
		<!-- 商品信息 -->
		<GoodsInfo :data="goodsDetail"></GoodsInfo>
		<!-- 选择项 -->
		<GoodsSelect :selectedSku="selectedSku" :selected-address="selectedAddr.addr" @openSku="openSkuPopup"></GoodsSelect>
		<!-- 评价 -->
		<!-- <TitleOperate :title="'商品评价（' + evaluateData.length + '条）'" showMore moreLabel=" " titleSize="32rpx" align="center"
			@clickMore="$u.route({ url: '/pages/index/index' })"></TitleOperate>
		<view class="evaluate-list">
			<EvaluateCard :data="evaluate" v-for="(evaluate, evaluateIndex) in evaluateData" :key="evaluateIndex"
				:showBorderBottom="evaluateIndex != evaluateData.length - 1"></EvaluateCard>
		</view> -->
		<!-- 商品详情 -->
		<TitleOperate title="商品详情" titleSize="32rpx" align="center"></TitleOperate>
		<u-parse class="details-html" :content="goodsDetail.content"></u-parse>

		<!-- 底部操作按钮 -->
		<GoodsOperate :data="goodsDetail" @addShoppingCart="addShoppingCart" @buyNow="buyNow" ref="goodsOperate"></GoodsOperate>
		<!-- 选择sku -->
		<GoodsSelectSku ref="GoodsSelectSku" @addShoppingCart="addShoppingCart" @buyNow="buyNow" @change="changeSku" @changeNum="changeNum">
		</GoodsSelectSku>
	</view>
</template>

<script>
	var http = require("@/utils/http");
	import GoodsInfo from '@/components/goods/goods-info.vue';
	import GoodsSelect from '@/components/goods/goods-select.vue';
	import GoodsOperate from '@/components/goods/goods-operate.vue';
	import GoodsSelectSku from '@/components/goods/goods-select-sku.vue';
	import TitleOperate from '@/components/title-operate.vue';
	import EvaluateCard from '@/components/evaluate-card.vue';
	export default {
		components: {
			GoodsInfo,
			GoodsSelect,
			GoodsOperate,
			GoodsSelectSku,
			TitleOperate,
			EvaluateCard
		},
		data() {
			return {
				currentNum: 0,
				imgs: [],
				prodNum: 1,
				goodsDetail: {},
				evaluateData: [{
					star: 4,
					content: '非常好用!会推荐给家人',
					date: '2022-01-12',
					userName: '演示用户',
					pics: ['https://s1.ax1x.com/2022/07/07/jdWtRe.png']
				}],
				// 已选择sku
				selectedSku: {
					label: '',
					value: 0
				},
				// 已选择地址
				selectedAddr: {
					addrId: 0,
					addr: undefined
				}
			}
		},
		onLoad(option) {
			http.request({
				url: `/prod/prodInfo?prodId=${option.prodId}`,
				method: "get",
				callBack: res => {
					this.goodsDetail = res
					this.$refs.goodsOperate.init(res)
					var skuList = this.goodsDetail.skuList
					if (skuList.length == 1) {
						this.selectedSku = {
							skuId: skuList[0].skuId,
							label: skuList[0].properties
						}
					}
					this.imgs = res.imgs.split(',')
				},
			})
			
			http.request({
				url: `/p/address/addrCommonInfo`,
				method: "get",
				callBack: res => {
					this.selectedAddr = {
						addrId: res.addrId,
						addr: res.addr
					}
				},
			})
		},
		methods: {
			// 打开选择sku的弹窗
			openSkuPopup() {
				this.$refs.GoodsSelectSku.open(this.goodsDetail, this.selectedSku);
			},
			changeSku(item) {
				this.selectedSku = {	
					skuId: item.skuId,
					label: item.properties
				}
				this.goodsDetail.price = item.price
			},
			changeNum(num) {
				this.prodNum = num
				console.log(this.prodNum);
			},
			addShoppingCart() {
				if (!this.selectedSku.skuId) {
					uni.showToast({
						title: "商品规格未选择",
						icon: 'error',
					})
					return
				}
				if (!this.selectedAddr.addrId) {
					uni.showToast({
						title: "地址未选择",
						icon: 'error',
					})
					return
				}
				// var ths = this;
				uni.showLoading({
				  mask: true
				});
				var params = {
				  url: "/p/shopCart/changeItem",
				  method: "POST",
				  data: {
				    basketId: 0,
				    count: this.prodNum,
				    prodId: this.goodsDetail.prodId,
				    // shopId: this.shopId,
				    shopId: 0,
				    skuId: this.selectedSku.skuId
				  },
				  callBack: res => {
				    //console.log(res);
				    // this.setData({
				    //   totalCartNum: this.totalCartNum + this.prodNum
				    // });
				    uni.hideLoading();
				    uni.showToast({
				      title: "加入购物车成功",
				      icon: "none"
				    });
				  }
				};
				http.request(params);
			},
			buyNow() {
				if (!this.selectedSku.skuId) {
					uni.showToast({
						title: "商品规格未选择",
						icon: 'error',
					})
					return
				}
				if (!this.selectedAddr.addrId) {
					uni.showToast({
						title: "地址未选择",
						icon: 'error',
					})
					return
				}
				
				uni.setStorageSync("orderItem", JSON.stringify({
				  prodId: this.goodsDetail.prodId,
				  skuId: this.selectedSku.skuId,
				  prodCount: 1,
				  shopId: this.goodsDetail.shopId
				}));
				uni.navigateTo({
					url: '/pages/order/submit?orderEntry=1'
				});
			}
		}
	}
</script>

<style lang="scss" scoped>
	.indicator {
		@include flex(row);
		justify-content: center;

		&__dot {
			height: 6px;
			width: 6px;
			border-radius: 100px;
			background-color: rgba(255, 255, 255, 0.35);
			margin: 0 5px;
			transition: background-color 0.3s;

			&--active {
				background-color: #ffffff;
			}
		}
	}

	.indicator-num {
		padding: 2px 0;
		background-color: rgba(0, 0, 0, 0.35);
		border-radius: 100px;
		width: 35px;
		@include flex;
		justify-content: center;

		&__text {
			color: #FFFFFF;
			font-size: 12px;
		}
	}


	.page {
		background-color: #F8F8F8;
		overflow: auto;
	}

	.evaluate-list {
		background-color: #fff;
		padding: 0 30rpx 30rpx 30rpx;
		margin-bottom: 16rpx;
	}

	.details-html {
		background-color: #fff;
		padding: 0 30rpx 30rpx 30rpx;
		margin-bottom: 100rpx;

		img {
			width: 100%;
		}
	}
</style>
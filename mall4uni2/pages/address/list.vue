<template>
	<view class="page">
		<view class="list">
			<AddressCard v-for="(item, index) in list" :isBack="isBack" :key="index" :data="item" :showEdit="!isSelect"
				:showBorderBottom="index != list.length - 1"></AddressCard>
		</view>
		<view class="btn" v-if="!isSelect">
			<u-button type="primary" shape="circle" @click="toAddOrUpdate">
				<text>新建收货地址</text>
			</u-button>
		</view>
	</view>
</template>

<script>
	var http = require("@/utils/http");
	import AddressCard from '@/pages/components/address-card.vue';
	export default {
		components: {
			AddressCard
		},
		data() {
			return {
				isSelect: false,
				isBack: false,
				list: []
			};
		},
		onLoad(ops) {
			if (ops.isSelect) {
				this.isSelect = ops.isSelect;
				this.isBack = ops.isBack;
			}
			this.getList()
		},
		onShow() {
		  this.getList()
		},
		methods: {
			getList() {
				http.request({
					url: "/p/address/list",
					method: "get",
					callBack: res => {
						this.list = res
					},
				})
			},
			toAddOrUpdate() {
				uni.navigateTo({
					url: '/pages/address/add-or-update?type=add'
				});
			}
		}
	};
</script>

<style lang="scss" scoped>
	.page {
		background-color: #fff;
	}

	.list {
		padding: 0 30rpx;
	}

	.btn {
		position: absolute;
		bottom: env(safe-area-inset-bottom);
		left: 0;
		padding: 30rpx 30rpx 0 30rpx;
		width: 100%;

		text {
			margin-left: 8rpx;
		}
	}
</style>
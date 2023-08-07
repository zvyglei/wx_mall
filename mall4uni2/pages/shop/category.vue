<template>
<!--pages/category/category.wxml-->

<view class="container">


<!-- 头部搜索区 -->

  <view class="search-bar">
    <view class="search-box">
      <!-- <image src="/static/images/icon/search.png" class="search-img"></image> -->
      <!-- <text class="sear-input">搜索您想要的商品</text> -->
			<u-search shape="square" :showAction="false" clearabled placeholder="搜索您想要的商品" v-model="keywords"
				confirm-type="search" @search="toSearchConfirm"></u-search>
    </view>
  </view>
<!-- 滚动内容区 -->
  <view class="main">
    <!-- 左侧菜单start -->
    <scroll-view scroll-y="true" class="leftmenu">
      <block v-for="(item, index) in categoryList" :key="index">
        <view :class="'menu-item ' + (selIndex==index?'active':'') + ' '" :data-index="index" :data-id="item.categoryId" @tap="onMenuTab">
          {{item.categoryName}}
        </view>
      </block>
      <view v-if="!categoryList || !categoryList.length" class="ca-empty">暂无分类</view>
    </scroll-view>
    <!-- 左侧菜单end -->

    <!-- 右侧内容start -->
    <scroll-view scroll-y="true" class="rightcontent">
    <!-- <block wx:for='{{ productList}}' wx:key=''> -->
      <view v-if="productList.length" class="cont-item">
        <block v-for="(item, index) in productList" :key="index">
          <view class="show-item" @tap="toProdPage" :data-prodid="item.prodId">
            <view class="more-prod-pic">
              <image :src="item.pic" class="more-pic" mode="widthFix"></image>
            </view>
            <view class="prod-text-right">
              <view class="prod-text more">{{item.prodName}}</view>
              <view class="cate-prod-info">{{item.brief}}</view>
              <view class="prod-price more">
                <text class="symbol">￥</text> <text class="big-num">{{wxs.parsePrice(item.price)[0]}}</text><text class="small-num">.{{wxs.parsePrice(item.price)[1]}}</text> 
              </view>
            </view>
          </view>
         
        </block>
      </view>
      <view v-if="!productList.length" class="cont-item empty">{{categoryList && categoryList.length ? '该分类下暂无商品' : '暂无商品'}}</view>
     <!-- </block> -->
    </scroll-view>
    <!-- 右侧内容end -->
    <!-- </block> -->
  </view>

</view>
</template>

<script module="wxs" lang="wxs" src="../../wxs/number.wxs"></script>

<script>
// pages/category/category.js
var http = require("../../utils/http.js");
var config = require("../../utils/config.js");

export default {
  data() {
    return {
      selIndex: 0,
      categoryList: [],
      productList: [],
      categoryImg: '',
      prodid: '',
			keywords: '',
    };
  },

  components: {},
  props: {},

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
		var ths = this; //加载分类列表
		
		var params = {
		  url: "/category/categoryInfo",
		  method: "GET",
		  data: {
		    parentId: ''
		  },
		  callBack: function (res) {
				console.log(res);
				ths.categoryList = res
		    ths.getProdList(res[0].categoryId);
		  }
		};
		http.request(params);
	},

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {},

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {},

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {},

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {},

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {},
  methods: {
		toSearchConfirm: function(e) {
			console.log(123123);
			this.getProdList(this.categoryList[this.selIndex].categoryId)
		},
    /**
     * 分类点击事件
     */
    onMenuTab: function (e) {
      var id = e.currentTarget.dataset.id;
      var index = e.currentTarget.dataset.index; // this.getProdList(id);

      this.getProdList(this.categoryList[index].categoryId);
			this.selIndex = index
    },

    getProdList(categoryId) {
			var ths = this
      //加载分类列表
      var params = {
        url: "/prod/pageProd",
        method: "GET",
        data: {
          categoryId: categoryId,
					keywords: ths.keywords
        },
        callBack: res => {
          ths.productList = res.records
        }
      };
      http.request(params);
    },

    //跳转商品详情页
    toProdPage: function (e) {
      var prodid = e.currentTarget.dataset.prodid;
			uni.navigateTo({
				url: `/pages/shop/detail?prodId=${prodid}`
			});
    }
  }
};
</script>
<style>
@import "./category.css";
</style>
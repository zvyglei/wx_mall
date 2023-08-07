<template>
  <div class="mod-prod-info">
    <el-form :model="dataForm" ref="dataForm" label-width="120px">
      <el-form-item label="商品图片">
        <mul-pic-upload v-model="dataForm.imgs" />
      </el-form-item>
      <el-row>
        <el-col :span="6">
          <el-form-item label="状态">
            <el-radio-group v-model="dataForm.status">
              <el-radio :label="1">上架</el-radio>
              <el-radio :label="0">下架</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="产品分类" :rules="[{ required: true, message: '请选择产品分类', trigger: 'change'}]"
                        prop="categoryId">
            <el-cascader expand-trigger="hover" :options="category.list" :props="category.props"
                         v-model="category.selected" change-on-select @change="handleCategoryChange">
            </el-cascader>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="抢购开始时间" prop="flashSaleStart" :rules="[
                      { required: true, message: '抢购开始时间不能为空'}
                    ]">
            <el-date-picker v-model="dataForm.flashSaleStart" type="datetime" placeholder="选择日期时间"
                            default-time="12:00:00" value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="抢购时间" prop="flashSaleTime" :rules="[
                      { required: true, message: '抢购时间不能为空'}
                    ]">
            <el-input-number v-model="dataForm.flashSaleTime" @change="handleChange" :min="10"></el-input-number> 秒
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="18">
          <el-form-item label="指定可抢会员" prop="prodName">
            <el-select v-model="dataForm.flashSaleUser" multiple collapse-tags style="width: 100%" filterable remote
                       clearable :remote-method="remoteMethod" placeholder="请输入手机号或真实姓名查询">
              <el-option v-for="item in saleUsers" :key="item.userId" :label="`${item.realName}（${item.userMobile}）`"
                         :value="item.userId">
              </el-option>
            </el-select>
            <div style="color: #8a919f">不指定则所有会员都可以参与抢购！</div>
          </el-form-item>
        </el-col>
        <el-col :span="18">
          <el-form-item label="商品名称" prop="prodName" :rules="[
                      { required: true, message: '商品名称不能为空'},
                      { pattern: /\s\S+|S+\s|\S/, message: '请输入正确的商品名称', trigger: 'blur' }
                    ]">
            <el-input v-model="dataForm.prodName" placeholder="商品名称" maxlength="50"></el-input>
          </el-form-item>
        </el-col>
        <!-- <el-form-item label="商品标签" :rules="[{ required: true, message: '请选择商品标签'}]">
        <el-col :span="8">
          <el-select v-model="dataForm.tagList"
                    multiple
                    style="width: 250px"
                    placeholder="请选择">
            <el-option v-for="item in this.tags"
                      :key="item.id"
                      :label="item.title"
                      :value="item.id">
            </el-option>
          </el-select>
        </el-col>
      </el-form-item> -->
        <el-col :span="18">
          <el-form-item label="商品卖点" prop="brief" :rules="[
                      { required: false, pattern: /\s\S+|S+\s|\S/, message: '请输入正确的商品卖点', trigger: 'blur' }
                    ]">
            <el-input v-model="dataForm.brief" type="textarea" :autosize="{minRows: 2, maxRows: 4}" placeholder="商品卖点">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- <el-form-item label="配送方式">
        <el-checkbox v-model="dataForm.deliveryMode.hasShopDelivery">商家配送</el-checkbox>
        <el-checkbox v-model="dataForm.deliveryMode.hasUserPickUp">用户自提</el-checkbox>
      </el-form-item> -->
      <!-- <prod-transport v-show="dataForm.deliveryMode.hasShopDelivery" v-model="dataForm.deliveryTemplateId">
      </prod-transport> -->
      <sku-tag ref="skuTag" :skuList="dataForm.skuList" @change="skuTagChangeSkuHandler"></sku-tag>
      <sku-table ref="skuTable" v-model="dataForm.skuList" :prodName.sync="dataForm.prodName"></sku-table>
      <el-form-item label="商品详情" prop="content">
        <tiny-mce v-model="dataForm.content" ref="content" style="width:1000px"></tiny-mce>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import { treeDataTranslate, idList } from '@/utils'
  import MulPicUpload from '@/components/mul-pic-upload'
  // import ProdTransport from './prod-transport'
  import SkuTag from './sku-tag'
  import SkuTable from './sku-table'
  import TinyMce from '@/components/tiny-mce'
  import { Debounce } from '@/utils/debounce'

  export default {
    data () {
      return {
        imageList: [],
        resourcesUrl: process.env.VUE_APP_RESOURCES_URL,
        // 分类树展示与回显
        category: {
          list: [],
          selected: [],
          props: {
            value: 'categoryId',
            label: 'categoryName'
          }
        },
        saleUsers: [],
        // 规格列表
        dataForm: {
          prodName: '',
          brief: '',
          pic: '',
          imgs: '',
          categoryId: undefined,
          prodId: 0,
          skuList: [],
          tagList: [],
          content: '',
          status: 1,
          flashSaleStart: undefined,
          flashSaleTime: 10,
          deliveryMode: {
            hasShopDelivery: false,
            hasUserPickUp: false
          },
          deliveryTemplateId: null
        },
        tags: [],
        resourcesUrl: process.env.VUE_APP_RESOURCES_URL
      }
    },
    components: {
      MulPicUpload,
      // ProdTransport,
      TinyMce,
      SkuTag,
      SkuTable
    },
    computed: {
      defalutSku () {
        return this.$store.state.prod.defalutSku
      }
    },
    activated () {
      this.dataForm.prodId = this.$route.query.prodId
      this.getDataList()
    },
    methods: {
      remoteMethod (query) {
        this.$http({
          url: this.$http.adornUrl(`/admin/user/search?keywords=${query}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({ data }) => {
          this.saleUsers = data
        })
      },
      // 获取分类数据
      getDataList () {
        // this.getTagList()
        this.getCategoryList().then(() => {
          if (this.dataForm.prodId) {
            // 获取商品数据
            this.$http({
              url: this.$http.adornUrl(`/prod/prod/info/${this.dataForm.prodId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({ data }) => {
              this.dataForm = data
              this.dataForm.deliveryMode = JSON.parse(data.deliveryMode)
              this.$refs.skuTag.init(data.skuList)
              this.$refs.skuTable.init()
              this.category.selected = idList(this.category.list, this.dataForm.categoryId, 'categoryId', 'children').reverse()
              this.dataForm.tagList = data.tagList
            })
          } else {
            this.$nextTick(() => {
              this.$refs['dataForm'].resetFields()
              this.$refs.skuTag.init()
              this.dataForm.pic = ''
              this.dataForm.imgs = ''
            })
          }
        })
      },
      // 获取分类信息
      getCategoryList () {
        return this.$http({
          url: this.$http.adornUrl('/prod/category/listCategory'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({ data }) => {
          this.category.list = treeDataTranslate(data, 'categoryId', 'parentId')
        })
      },
      // 选择分类改变事件
      handleCategoryChange (val) {
        this.dataForm.categoryId = val[val.length - 1]
      },
      // 表单提交
      dataFormSubmit: Debounce(function () {
        this.$refs['dataForm'].validate((valid) => {
          if (!valid) {
            return
          }
          if (!this.dataForm.imgs) {
            this.errorMsg('请选择图片上传')
            return
          }
          // if (!this.dataForm.deliveryMode) {
          //   this.errorMsg('请选择配送方式')
          //   return
          // }
          // if (this.dataForm.deliveryMode.hasShopDelivery && !this.dataForm.deliveryTemplateId) {
          //   this.errorMsg('请选择运费模板')
          //   return
          // }
          let param = Object.assign({}, this.dataForm)
          // 设置价格和库存
          this.paramSetPriceAndStocks(param)

          param.deliveryMode = undefined
          param.deliveryModeVo = this.dataForm.deliveryMode
          // 商品主图
          param.pic = this.dataForm.imgs.split(',')[0]
          // 抢购
          param.flashSale = 1
          this.$http({
            url: this.$http.adornUrl(`/prod/prod`),
            method: param.prodId ? 'put' : 'post',
            data: this.$http.adornData(param)
          }).then(({ data }) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 500,
              onClose: () => {
                this.visible = false
                this.$store.commit('common/removeMainActiveTab')
                this.$router.push({ name: 'prod-prodFlashSaleList' })
                this.$emit('refreshDataList')
              }
            })
          })
        })
      }),
      // // 限制图片上传大小
      // beforeAvatarUpload (file) {
      //   const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif' || file.type === 'image/jpg'
      //   if (!isJPG) {
      //     this.$message.error('上传图片只能是jpeg/jpg/png/gif 格式!')
      //   }
      //   const isLt2M = file.size / 1024 / 1024 < 2
      //   if (!isLt2M) {
      //     this.$message.error('上传图片大小不能超过 2MB!')
      //   }
      //   return isLt2M && isJPG
      // },
      paramSetPriceAndStocks (param) {
        // 获取规格属性信息
        // param.skuList = this.$refs.prodSpec.getTableSpecData()
        // 商品库存
        param.totalStocks = 0
        // 商品价格
        param.price = 0
        // 商品原价
        param.oriPrice = 0
        // 商品实际库存
        for (let i = 0; i < param.skuList.length; i++) {
          const element = param.skuList[i]
          if (element.status !== 1) {
            continue
          }
          if (param.price === 0) {
            param.price = element.price ? Number.parseFloat(element.price) : 0
          }
          // 商品价格为最低价的那件商品的价格
          param.price = Math.min(param.price, element.price)
          if (param.price === element.price) {
            param.oriPrice = element.oriPrice ? Number.parseFloat(element.oriPrice) : 0
          }
          param.totalStocks += element.stocks ? Number.parseInt(element.stocks) : 0
        }
        // 如果sku没有商品名称，则使用商品的商品名称
        if (param.skuList.length === 1) {
          param.skuList[0].prodName = this.dataForm.prodName
        }
      },
      skuTagChangeSkuHandler (skuList) {
        const prodName = this.dataForm.prodName
        skuList.forEach(sku => {
          if (sku.properties) {
            sku.skuName = ''
            let properties = sku.properties.split(';')
            for (const propertiesKey in properties) {
              sku.skuName += properties[propertiesKey].split(':')[1] + ' '
            }
            sku.prodName = prodName + ' ' + sku.skuName
          }
        })
        this.dataForm.skuList = skuList
      },
      errorMsg (message) {
        this.$message({
          message: message,
          type: 'error',
          duration: 500
        })
      },
      // 获取所有的分组信息
      getTagList () {
        this.$http({
          url: this.$http.adornUrl('/prod/prodTag/listTagList'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({ data }) => {
          this.tags = data
        })
      }
    }
  }
</script>

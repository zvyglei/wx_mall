<template>
  <div class="mod-user">
    <avue-crud ref="crud" :page="page" :data="dataList" :option="tableOption" @search-change="searchChange"
               @selection-change="selectionChange" @on-load="getDataList">
      <!--      <template slot="menuLeft">-->
      <!--        <el-button type="danger"-->
      <!--                   @click="deleteHandle()"-->
      <!--                   v-if="isAuth('admin:user:delete')"-->
      <!--                   size="small"-->
      <!--                   :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
      <!--      </template>-->
      <template slot-scope="scope" slot="pic">
        <span class="avue-crud__img" v-if="scope.row.pic">
          <i :src="scope.row.pic" class="el-icon-document"></i>
        </span>
        <span v-else>-</span>
      </template>
      <template slot-scope="scope" slot="score">
        <span style="font-size: 14px; color: #fd5050;">{{ scope.row.score }}</span>
      </template>
      <template slot-scope="scope" slot="sex">
        {{ scope.row.sex === 'F' ? '女' : '男' }}
      </template>
      <template slot-scope="scope" slot="status">
        <el-tag v-if="scope.row.status === 0" size="small" type="danger">禁用</el-tag>
        <el-tag v-else size="small">正常</el-tag>
      </template>
      <template slot-scope="scope" slot="verifyStatus">
        <el-tag v-if="scope.row.verifyStatus === 0" size="small" type="info">未认证</el-tag>
        <el-tag v-else-if="scope.row.verifyStatus === 1" size="small" type="danger">待审核</el-tag>
        <el-tag v-else-if="scope.row.verifyStatus === 2" size="small">已认证</el-tag>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button type="primary" icon="el-icon-edit" size="small" v-if="isAuth('admin:user:update')"
                   @click.stop="addOrUpdateHandle(scope.row.userId)">编辑</el-button>
        <el-button type="warning" icon="el-icon-circle-check" size="small"
                   v-if="scope.row.verifyStatus === 1 && isAuth('admin:user:examine')"
                   @click.stop="examineHandle(scope.row.userId)">审核</el-button>
        <!-- <el-button type="warning" icon="el-icon-circle-check" size="small"
                   @click.stop="examineHandle(scope.row.userId)">审核</el-button> -->
        <el-button type="success" icon="el-icon-circle-plus-outline" size="small" v-if="isAuth('admin:user:charge')"
                   @click.stop="scoreCharge(scope.row)">充值</el-button>
        <el-button type="danger" icon="el-icon-remove-outline" size="small" v-if="isAuth('admin:user:charge')"
                   @click.stop="scoreReduce(scope.row)">扣除</el-button>
        <!--        <el-button type="danger"-->
        <!--                   icon="el-icon-delete"-->
        <!--                   size="small"-->
        <!--                   v-if="isAuth('admin:user:delete')"-->
        <!--                   @click.stop="deleteHandle(scope.row.userId)">删除</el-button>-->
      </template>
    </avue-crud>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    <examine ref="examine" @refreshDataList="getDataList"></examine>
    <!-- 充值弹窗 -->
    <el-dialog :title="scoreChargeTitle" :visible.sync="scoreChargeShow" width="500px">
      <el-form ref="scoreChargeForm" :model="scoreChargeForm" label-width="80px">
        <el-form-item label="充值数量" required prop="score" :rules="[
            { required: true, message: '请输入充值数量', trigger: 'blur' }
          ]">
          <el-input-number v-model="scoreChargeForm.score" :precision="2" :step="0.1" :min="0" :max="99999999">
          </el-input-number>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="scoreChargeForm.chargeRemark" placeholder="请输入备注信息"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="scoreChargeSubmit">立即充值</el-button>
          <el-button @click="scoreChargeShow = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  import { tableOption } from '@/crud/user/user'
  import AddOrUpdate from './user-add-or-update'
  import examine from './examine.vue'
  export default {
    data () {
      return {
        dataList: [],
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        tableOption: tableOption,
        page: {
          total: 0, // 总页数
          currentPage: 1, // 当前页数
          pageSize: 10 // 每页显示多少条
        },
        scoreChargeShow: false,
        scoreChargeTitle: '',
        scoreChargeForm: {
          score: 0.00,
          chargeRemark: undefined,
          userId: undefined,
          wxOpenId: undefined
        }
      }
    },
    components: {
      AddOrUpdate,
      examine
    },
    methods: {
      // 获取数据列表
      getDataList (page, params, done) {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/admin/user/page'),
          method: 'get',
          params: this.$http.adornParams(
            Object.assign(
              {
                current: page == null ? this.page.currentPage : page.currentPage,
                size: page == null ? this.page.pageSize : page.pageSize
              },
              params
            )
          )
        }).then(({ data }) => {
          this.dataList = data.records
          this.page.total = data.total
          this.dataListLoading = false
          if (done) {
            done()
          }
        })
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 审核
      examineHandle (id) {
        this.$nextTick(() => {
          this.$refs.examine.init(id)
        })
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.userId
        })
        this.$confirm(`确定进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            this.$http({
              url: this.$http.adornUrl('/admin/user'),
              method: 'delete',
              data: this.$http.adornData(ids, false)
            }).then(({ data }) => {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 500,
                onClose: () => {
                  this.getDataList(this.page)
                }
              })
            })
          })
          .catch(() => { })
      },
      // 积分充值
      scoreCharge (row) {
        // if (row.verifyStatus === 0) {
        //   this.$message.error('用户未认证')
        //   return
        // }
        this.scoreChargeForm = {
          score: 0.00,
          remark: undefined,
          userId: row.userId,
          wxOpenId: row.wxOpenId
        }
        this.scoreChargeShow = true
        this.scoreChargeTitle = `给用户【${row.realName || '-'}（${row.userMobile}）】充值黄金豆`
        // this.$prompt(, '黄金豆充值', {
        //   confirmButtonText: '确定',
        //   cancelButtonText: '取消',
        //   inputPattern: /^[1-9]\d*$/,
        //   inputErrorMessage: '充值数量格式不正确，请输入正整数'
        // }).then(({ value }) => {
        //   this.$http({
        //     url: this.$http.adornUrl('/admin/user/charge'),
        //     method: 'post',
        //     data: { userId: row.userId, score: value, wxOpenId: row.wxOpenId }
        //   }).then(({ data }) => {
        //     this.$message({
        //       message: '操作成功',
        //       type: 'success',
        //       duration: 500,
        //       onClose: () => {
        //         this.getDataList(this.page)
        //       }
        //     })
        //   })
        // }).catch(() => {

        // });
      },
      scoreChargeSubmit () {
        this.$refs.scoreChargeForm.validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl('/admin/user/charge'),
              method: 'post',
              data: this.scoreChargeForm
            }).then(({ data }) => {
              this.scoreChargeShow = false
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 500,
                onClose: () => {
                  this.getDataList(this.page)
                }
              })
            })
          }
        });
      },
      // 积分扣除
      scoreReduce (row) {
        // if (row.verifyStatus === 0) {
        //   this.$message.error('用户未认证')
        //   return
        // }
        this.$prompt(`扣除用户【${row.realName || '-'}（${row.userMobile}）】的黄金豆`, '黄金豆扣除', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /^[1-9]\d*$/,
          inputErrorMessage: '扣除数量格式不正确，请输入正整数'
        }).then(({ value }) => {
          this.$http({
            url: this.$http.adornUrl('/admin/user/reduce'),
            method: 'post',
            data: { userId: row.userId, score: value, wxOpenId: row.wxOpenId }
          }).then(({ data }) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 500,
              onClose: () => {
                this.getDataList(this.page)
              }
            })
          })
        }).catch(() => {

        });
      },
      // 条件查询
      searchChange (params, done) {
        this.getDataList(this.page, params, done)
      },
      // 多选变化
      selectionChange (val) {
        this.dataListSelections = val
      }
    }
  }
</script>

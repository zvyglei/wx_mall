<template>
  <el-dialog :title="'会员认证审核'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <!-- 会员认证信息 -->
      <el-descriptions title="会员认证信息" :column="2">
        <el-descriptions-item label="姓名">{{examineInfo.realName}}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{examineInfo.idCard}}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions title="" :column="2" direction="vertical">
        <el-descriptions-item label="身份证人像面照片">
          <el-image :src="examineInfo.idCardImg1" style="height: 200px"></el-image>
        </el-descriptions-item>
        <el-descriptions-item label="身份证国徽面照片">
          <el-image :src="examineInfo.idCardImg2" style="height: 200px"></el-image>
        </el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">审核</el-divider>
      <!-- 审核 -->
      <el-form-item label="审核结果" size="mini" prop="verifyStatus" required>
        <el-radio-group v-model="dataForm.verifyStatus">
          <el-radio :label="2">通过</el-radio>
          <el-radio :label="0">驳回</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="审核意见" prop="realName">
        <el-input v-model="dataForm.verifyRemark" placeholder="请输入审核意见"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          userId: 0,
        },
        examineInfo: {},
        dataRule: {
          verifyStatus: [
            { required: true, message: '审核结果不能为空', trigger: 'change' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
        })
        this.dataForm = {}
        this.dataForm.userId = id || 0
        this.visible = true

        this.$http({
          url: this.$http.adornUrl('/admin/user/examine/info'),
          method: 'get',
          params: { userId: this.dataForm.userId }
        }).then(({ data }) => {
          this.examineInfo = data
        })
      },
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl('/admin/user/examine'),
              method: 'post',
              data: this.dataForm
            }).then(({ data }) => {
              this.$message({
                message: '审核成功',
                type: 'success',
                duration: 500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList', this.page)
                }
              })
            })
          }
        });
      }
    }
  }
</script>

<style>
</style>
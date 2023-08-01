<template>
  <div>
    <el-form :model="dataForm" ref="dataForm" label-width="80px">
      <el-form-item label="转账设置" prop="type">
        <el-switch v-model="dataForm.paramValue" active-color="#409eff" inactive-color="#efefef" active-value="1"
                   inactive-value="0" @change="dataFormSubmit">
        </el-switch>
        <div style="color: #989898">{{dataForm.remark}}</div>
      </el-form-item>
    </el-form>
    <!-- <el-button type="primary" @click="dataFormSubmit()">确定</el-button> -->
  </div>
</template>

<script>
  export default {
    data () {
      return {
        dataForm: {
          id: 2,
          paramKey: 'transfer',
          paramValue: false,
          remark: undefined
        }
      }
    },
    created () {
      this.$http({
        url: this.$http.adornUrl(`/sys/config/info/${this.dataForm.id}`),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({ data }) => {
        this.dataForm.paramKey = data.paramKey
        this.dataForm.paramValue = data.paramValue
        this.dataForm.remark = data.remark
      })
    },
    methods: {
      dataFormSubmit () {
        this.$http({
          url: this.$http.adornUrl(`/sys/config`),
          method: this.dataForm.id ? 'put' : 'post',
          data: this.$http.adornData({
            'id': this.dataForm.id || undefined,
            'paramKey': this.dataForm.paramKey,
            'paramValue': this.dataForm.paramValue,
            'remark': this.dataForm.remark
          })
        }).then(({ data }) => {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1000
          })
        })
      }
    }
  }
</script>

<style>
</style>
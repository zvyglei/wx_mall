export const tableOption = {
  searchMenuSpan: 6,
  columnBtn: false,
  border: true,
  // selection: true,
  index: false,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  menuWidth: 350,
  align: 'center',
  refreshBtn: true,
  searchSize: 'mini',
  addBtn: false,
  editBtn: false,
  delBtn: false,
  viewBtn: false,
  props: {
    label: 'label',
    value: 'value'
  },
  column: [{
    label: '手机号',
    prop: 'userMobile',
    width: 120,
    search: true
  }, {
    label: '黄金豆',
    prop: 'score',
    slot: true
  }, {
    label: '姓名',
    prop: 'realName',
    search: true
  }, {
    label: '身份证号',
    prop: 'idCard',
    search: true,
    width: 200
  }, {
    label: '认证状态',
    prop: 'verifyStatus',
    type: 'select',
    search: true,
    slot: true,
    dicData: [
      {
        label: '未认证',
        value: 0
      }, {
        label: '待审核',
        value: 1
      }, {
        label: '已认证',
        value: 2
      }
    ]
  }, {
    label: '账号状态',
    prop: 'status',
    width: '150',
    search: true,
    type: 'select',
    slot: true,
    dicData: [
      {
        label: '禁用',
        value: 0
      }, {
        label: '正常',
        value: 1
      }
    ]
  }, {
    label: '注册时间',
    prop: 'userRegtime',
    width: 160,
  }]
}

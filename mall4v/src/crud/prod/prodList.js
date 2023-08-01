export const tableOption = {
  searchMenuSpan: 6,
  columnBtn: false,
  border: true,
  selection: true,
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
  menuWidth: 200,
  props: {
    label: 'label',
    value: 'value'
  },
  column: [{
    label: '产品名字',
    prop: 'prodName',
    width: '300',
    align: 'left',
    search: true
  }, {
    label: '商品原价',
    prop: 'oriPrice'
  }, {
    label: '黄金豆',
    prop: 'price'
  }, {
    label: '商品库存',
    prop: 'totalStocks'
  }, {
    label: '产品图片',
    prop: 'pic',
    type: 'upload',
    width: 100,
    listType: 'picture-img'

  }, {
    width: 100,
    label: '状态',
    prop: 'status',
    search: true,
    slot: true,
    type: 'select',
    dicData: [
      {
        label: '未上架',
        value: 0
      }, {
        label: '上架',
        value: 1
      }
    ]
  }]
}

<template>
  <div class="room-list">
    <div class="filter">
    </div>
    <div class="list">
      <a-table
              :columns="columns"
              :dataSource="rooms"
      >
        <span slot="roomType" slot-scope="text">
          <span style="font-weight: bold;font-size: 15px">{{text}}</span>
        </span>
        <span slot="price" slot-scope="text">
          <span style="font-weight: bold; font-size: 18px; color: rgb(24, 144, 255)">￥{{ text }}</span>
        </span>
        <span slot="action" slot-scope="record">
          <a-button :disabled="record.curNum<=0" type="primary" @click="order(record)">预定</a-button>
        </span>
      </a-table>
    </div>
    <OrderModal></OrderModal>
  </div>
</template>
<script>
  import {mapGetters, mapActions, mapMutations} from 'vuex'
  import OrderModal from './orderModal'

  const columns = [
    {
      align: 'center',
    },
    {
      title: '房型',
      dataIndex: 'roomType',
      key: 'roomType',
      scopedSlots: {customRender: 'roomType'}
    },
    {
      title: '床型',
      dataIndex: 'bedType',
      key: 'bedType',
    },
    {
      title: '剩余房间数量',
      dataIndex: 'curNum',
      key: 'curNum',
    },
    {
      title: '建议入住人数',
      key: 'peopleNum',
      dataIndex: 'peopleNum',
    },
    {
      title: '房价',
      key: 'price',
      dataIndex: 'price',
      scopedSlots: {customRender: 'price'}
    },
    {
      title: '',
      key: 'action',
      scopedSlots: {customRender: 'action'},
    },
  ];
  export default {
    name: 'roomList',
    props: {
      rooms: {
        type: Array
      }
    },
    data() {
      return {
        columns,
      }
    },
    components: {
      OrderModal
    },
    computed: {
      ...mapGetters([
        'orderModalVisible'
      ])
    },
    monuted() {

    },
    methods: {
      ...mapMutations([
        'set_orderModalVisible',
        'set_currentOrderRoom'
      ]),
      ...mapActions([]),
      order(record) {
        this.set_currentOrderRoom(record)
        this.set_orderModalVisible(true)
      }
    }
  }
</script>

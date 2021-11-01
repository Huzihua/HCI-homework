<template>
    <div class="manageHotel-wrapper">
        <a-tabs>
            <a-tab-pane tab="酒店基本信息维护" key="1">
                <!-- <div style="width: 100%; text-align: right; margin:20px 0">
                    <a-button type="primary" @click="addHotel"><a-icon type="plus" />添加酒店</a-button>
                </div> -->
                <a-table
                    :columns="columns1"
                    :dataSource="hotelList"
                    bordered
                >
                    <span slot="action" slot-scope="record">
                        <a-button type="primary" size="small" @click="addRoom(record)">添加房型</a-button>
                        <a-divider type="vertical"></a-divider>
                        <a-button type="info" size="small" @click="showCoupon(record)">优惠策略</a-button>
                        <a-divider type="vertical"></a-divider>
                        <a-button type="primary" size="small" @click="updateHotel(record)">更新酒店</a-button>
                        <a-divider type="vertical"></a-divider>
                        <a-popconfirm
                            title="确定想删除该酒店吗？"
                            @confirm="confirmDeleteHotel(record)"
                            okText="确定"
                            cancelText="取消"
                        >
                            <a-button type="danger" size="small">删除酒店</a-button>
                        </a-popconfirm>
                    </span>
                </a-table>
            </a-tab-pane>
            <a-tab-pane tab="订单管理" key="2">
                <div style="width: 100%; text-align: right; margin:20px 0">
                    <a-button type="primary" @click="checkAllOrder"><a-icon />检查超时订单</a-button>
                </div>
                <a-table
                        :columns="columns2"
                        :dataSource="orderList"
                        bordered
                        v-show="!showDetail"
                >
                    <span slot="price" slot-scope="text">
                        <span>￥{{ text }}</span>
                    </span>
                    <span slot="roomType" slot-scope="text">
                        <span v-if="text == 'BigBed'">大床房</span>
                        <span v-if="text == 'DoubleBed'">双床房</span>
                        <span v-if="text == 'Family'">家庭房</span>
                    </span>
                    <a-tag slot="orderState" color="red" slot-scope="text" >
                        {{ text }}
                    </a-tag>
                    <span slot="action" slot-scope="record">
                        <a-button type="primary" size="small"  @click="showOrderDetails(record.id)">查看详情</a-button>
                        <a-divider type="vertical"></a-divider>
                        <a-popconfirm
                                title="你确定执行该笔订单吗？"
                                @confirm="confirmExecuteOrder(record.id)"
                                @cancel="cancelExecuteOrder"
                                okText="确定"
                                cancelText="取消"
                        >
                            <a-button type="primary" size="small" v-if="record.orderState==='已预订'">执行订单</a-button>
                        </a-popconfirm>
                        <a-divider type="vertical"></a-divider>
                        <a-popconfirm
                                title="确定办理退房吗？"
                                @confirm="checkout(record.id)"
                                @cancel="cancelCheckout"
                                okText="确定"
                                cancelText="取消"
                        >
                            <a-button type="primary" size="small" v-if="record.orderState==='已入住'">办理退房</a-button>
                        </a-popconfirm>
                        <a-divider type="vertical"></a-divider>
                        <a-popconfirm
                                title="你确定补登入住信息吗？"
                                @confirm="confirmExecuteOrder(record.id,record.userId)"
                                @cancel="cancelExecuteOrder"
                                okText="确定"
                                cancelText="取消"
                        >
                        <a-button type="danger" size="small" v-if="record.orderState==='异常'" >延迟入住</a-button>
                        </a-popconfirm>
                    </span>
                </a-table>
                <order-detail  v-if="showDetail" :back="setShowDetailFalse">
                </order-detail>
            </a-tab-pane>


        </a-tabs>
        <AddHotelModal></AddHotelModal>
        <AddRoomModal></AddRoomModal>
        <Coupon></Coupon>
        <UpdateHotelInfo></UpdateHotelInfo>
<!--        <orderDetail></orderDetail>-->

    </div>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
import AddHotelModal from './components/addHotelModal'
import AddRoomModal from './components/addRoomModal'
import Coupon from './components/coupon'
import UpdateHotelInfo from './components/updateHotel'
import orderDetail from "../order/components/orderDetail"
const moment = require('moment')
const columns1 = [
    {
        title: '酒店ID',
        dataIndex: 'id',
    },
    {
        title: '酒店名',
        dataIndex: 'name',
    },
    {
        title: '商圈',
        dataIndex: 'bizRegion',
    },
    {
        title: '地址',
        dataIndex: 'address',
    },
    {
        title: '酒店星级',
        dataIndex: 'hotelStar'
    },
    {
        title: '评分',
        dataIndex: 'rate',
    },
    {
        title: '简介',
        dataIndex: 'description',
    },
    {
      title: '操作',
      key: 'action',
      scopedSlots: { customRender: 'action' },
    },
  ];
const columns2 = [
    {
        title: '订单号',
        dataIndex: 'id',
    },
    {
        title: '客户ID',
        dataIndex: 'userId',
    },
    {
        title: '酒店名',
        dataIndex: 'hotelName',
    },
    {
        title: '房型',
        dataIndex: 'roomType',
        scopedSlots: { customRender: 'roomType' }
    },
    {
        title: '预计入住时间',
        dataIndex: 'checkInDate',
        scopedSlots: { customRender: 'checkInDate' }
    },
    {
        title: '预计离店时间',
        dataIndex: 'checkOutDate',
        scopedSlots: { customRender: 'checkOutDate' }
    },
    {
        title: '最晚入住时间',
        dataIndex: 'checkInDeadline',
        scopedSlots: { customRender: 'checkInDeadline' }
    },
    {
        title: '最晚离店时间',
        dataIndex: 'checkOutDeadline',
        scopedSlots: { customRender: 'checkOutDeadline' }
    },
    {
        title: '实际入住时间',
        dataIndex: 'actualInTime',
        scopedSlots: { customRender: 'actualInTime' }
    },
    {
        title: '实际离店时间',
        dataIndex: 'actualOutTime',
        scopedSlots: { customRender: 'actualOutTime' }
    },
    {
        title: '入住人数',
        dataIndex: 'peopleNum',
    },
    {
        title: '房价',
        dataIndex: 'price',
    },
    {
        title: '状态',
        filters: [{ text: '已预订', value: '已预订' }, { text: '已撤销', value: '已撤销' }, { text: '已入住', value: '已入住' },{ text: '待点评', value: '待点评' },{ text: '异常', value: '异常' }, {text: '已评价', value: '已评价'}],
        onFilter: (value, record) => record.orderState.includes(value),
        dataIndex: 'orderState',
        scopedSlots: { customRender: 'orderState' }
    },
    {
        title: '操作',
        key: 'action',
        scopedSlots: { customRender: 'action' },
    },
  ];
export default {
    name: 'manageHotel',
    data(){
        return {
            formLayout: 'horizontal',
            pagination: {},
            columns1,
            columns2,
            form: this.$form.createForm(this, { name: 'manageHotel' }),
            data: [],
            orderDetailInfo:{},
            showDetail:false
        }
    },
/*    props: {      //由父组件传递datasource
        orderList:{
            type: Array
        }
    },
**/
    components: {
        AddHotelModal,
        AddRoomModal,
        Coupon,
        UpdateHotelInfo,
        orderDetail,
    },
    computed: {
        ...mapGetters([
            'hotelList',
            'addHotelModalVisible',
            'addRoomModalVisible',
            'couponVisible',
            'updateHotelModalVisible',
            'deleteHotelModalVisible',
            'userId',
            'currentHotelId',
            'userOrderList',
            'orderList'
        ]),
    },
    async mounted() {
        await this.getHotelByManagerId()
        await this.getHotelOrders()
        await this.checkAllOrders()
    },
    methods: {
        ...mapMutations([
            'set_addHotelModalVisible',
            'set_addRoomModalVisible',
            'set_couponVisible',
            'set_updateHotelModalVisible',
            'set_deleteHotelModalVisible',
            'set_currentOrderId',
            'set_activeHotelId',
            'set_currentHotelId'
        ]),
        ...mapActions([
            'getHotelList',
            'getAllOrders',
            'getHotelCoupon',
            'cancelAbnormalOrder',
            'getOrderDetails',
            'deleteHotel',
            'executeOrder',
            'getHotelByManagerId',
            'getHotelOrders',
            'checkoutOrder',
            'checkAllOrders'
        ]),
        addHotel() {
            this.set_addHotelModalVisible(true)
        },
        addRoom(record) {
            this.set_activeHotelId(record.id)
            this.set_addRoomModalVisible(true)
        },
        showCoupon(record) {
            this.set_activeHotelId(record.id)
            this.set_couponVisible(true)
            this.getHotelCoupon()
        },
        confirmDeleteHotel(record){
            const data=record
            this.deleteHotel(data)
        },
        deleteOrder(){

        },
        updateHotel(record){
            this.set_currentHotelId(record.id)
            this.set_updateHotelModalVisible(true)
        },
        setShowDetailFalse(){
            // console.log("false")
            this.showDetail=false;
        },
        setShowDetailTrue(){
            this.showDetail=true;
        },
        //订单执行与退房
        confirmExecuteOrder(orderId){
            this.executeOrder(Number(orderId))
        },
        async checkout(orderId){
            await this.checkoutOrder(orderId)
            await this.getHotelByManagerId()
        },
        cancelCheckout() {

        },
        cancelExecuteOrder(){

        },
        showOrderDetails(orderId){ //查看订单详细信息
            this.set_currentOrderId(orderId)
            this.getOrderDetails()
            this.setShowDetailTrue()
            console.log(this.orderDetailInfo)
        },
        checkAllOrder(){
            this.checkAllOrders()
        }
    }
}
</script>
<style scoped lang="less">
    .manageHotel-wrapper {
        padding: 50px;
        .chart {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-top: 20px
        }
    }
</style>
<style lang="less">
    .manageHotel-wrapper {
        .ant-tabs-bar {
            padding-left: 30px
        }
    }
</style>
<style lang="less">
    
</style>
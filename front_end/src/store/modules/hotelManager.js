import {
    addRoomAPI,
    addHotelAPI,
    updateHotelAPI,
    deleteHotelAPI,
} from '@/api/hotelManager'
import {
    getAllOrdersAPI,
} from '@/api/order'
import {
    hotelAllCouponsAPI,
    hotelTargetMoneyAPI,
    hotelTimeAPI,
    hotelMultiRoomAPI,
} from '@/api/coupon'
import { message } from 'ant-design-vue'

const hotelManager = {
    state: {
        orderList: [],
        addHotelParams: {
            name: '',
            address: '',
            bizRegion:'',
            hotelStar:'',
            rate: 0,
            description:'',
            phoneNum:'',
            managerId:'',
            id:0,
        },
        addHotelModalVisible: false,
        updateHotelModalVisible:false,
        deleteHotelModalVisible:false,
        addRoomParams: {
            roomType: '',
            hotelId: '',
            price: '',
            total: 0,
            curNum: 0,
        },
        addRoomModalVisible: false,
        couponVisible: false,
        addCouponVisible: false,
        activeHotelId: 0,
        couponList: [],
    },
    mutations: {
        set_orderList: function(state, data) {
            state.orderList = data
        },
        set_addHotelModalVisible: function(state, data) {
            state.addHotelModalVisible = data
        },
        set_updateHotelModalVisible: function(state, data) {
            state.updateHotelModalVisible = data
        },
        set_deleteHotelModalVisible: function(state, data) {
            state.deleteHotelModalVisible = data
        },
        set_addHotelParams: function(state, data) {
            state.addHotelParams = {
                ...state.addHotelParams,
                ...data,
            }
        },
        set_addRoomModalVisible: function(state, data) {
            state.addRoomModalVisible = data
        },
        set_addRoomParams: function(state, data) {
            state.addRoomParams = {
                ...state.addRoomParams,
                ...data,
            }
        },
        set_couponVisible: function(state, data) {
            state.couponVisible = data
        },
        set_activeHotelId: function(state, data) {
            state.activeHotelId = data
        },
        set_couponList: function(state, data) {
            state.couponList = data
        },
        set_addCouponVisible: function(state, data) {
            state.addCouponVisible =data
        }
    },
    actions: {
        getAllOrders: async({ state, commit }) => {
            const res = await getAllOrdersAPI()
            if(res){
                commit('set_orderList', res)
            }
        },
        addHotel: async({ state, dispatch, commit }) => {
            const res = await addHotelAPI(state.addHotelParams)
            if(res){
                dispatch('getHotelList')
                commit('set_addHotelParams', {
                    name: '',
                    address: '',
                    bizRegion:'XiDan',
                    hotelStar:'',
                    rate: 0,
                    description:'',
                    phoneNum:'',
                    managerId:'',
                    id:'',
                })
                message.success('????????????')
            }else{
                message.error('????????????')
            }
        },
        updateHotel: async ({state, commit, dispatch}) => {
            const res = await updateHotelAPI(state.addHotelParams)
            if(res){
                dispatch('getHotelByManagerId')
                commit('set_addHotelParams', {
                    name: '',
                    address: '',
                    bizRegion:'XiDan',
                    hotelStar:'',
                    rate: 0,
                    description:'',
                    phoneNum:'',
                    managerId:'',
                    id:'',
                })
                message.success('????????????')
            }else{
                message.error('????????????')
            }
        },
        deleteHotel: async ({state, dispatch},data) => {
            const res = await deleteHotelAPI(data)
            if(res){
                dispatch('getHotelList')
                message.success('????????????')
            }else{
                message.error('????????????')
            }
        },
        addRoom: async({ state, dispatch, commit }) => {
            const res = await addRoomAPI(state.addRoomParams)
            if(res){
                commit('set_addRoomModalVisible', false)
                commit('set_addRoomParams', {
                    roomType: '',
                    hotelId: '',
                    price: '',
                    total: 0,
                    curNum: 0,
                })
                message.success('????????????')
            }else{
                message.error('????????????')
            }
        },
        getHotelCoupon: async({ state, commit }) => {
            const res = await hotelAllCouponsAPI(state.activeHotelId)
            if(res) {
                // ?????????????????????????????????????????????????????????????????????couponList???
                commit('set_couponList', res)
            }
        },
        addHotelCoupon: async({ commit, dispatch }, data) => {
            let res=null;
            if(Number(data.couponType)===1){
                res=await hotelTargetMoneyAPI(data)
            }else if(Number(data.couponType)===2){
                res=await hotelTimeAPI(data)
            }else if(Number(data.couponType)===4){
                res=await hotelMultiRoomAPI(data)
            }
            if(res){
                // ??????????????????????????????????????????modal???????????????????????????????????????????????????
                dispatch('getHotelCoupon')
                commit('set_addCouponVisible', false)
                commit('set_couponVisible', true)
                message.success('??????????????????')
            }else{
                // ????????????????????????
                message.error('????????????')
            }
        },//???????????????????????????????????????api????????????????????????????????????dispatch????????????????????????coupon?????????commit view?????????
    }
}
export default hotelManager
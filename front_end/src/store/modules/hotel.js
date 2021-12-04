import {message} from 'ant-design-vue'
import store from '@/store'
import {
  getHotelsAPI,
  getHotelByIdAPI,
  getHotelCommentsAPI,
} from '@/api/hotel'
import {
  reserveHotelAPI,
  getHotelOrders,
  executeOrderAPI,
  checkOutOrderAPI,
  checkOrdersAPI,
} from '@/api/order'
import {
  orderMatchCouponsAPI,
} from '@/api/coupon'


const hotel = {
  state: {
    hotelList: [],
    hotelListParams: {
      pageNo: 0,
      pageSize: 12
    },
    hotelListLoading: true,
    currentHotelId: '',
    currentHotelInfo: {},
    orderModalVisible: false,
    currentOrderRoom: {},
    orderMatchCouponList: [],
    commentList: []
  },
  mutations: {
    set_hotelList: function (state, data) {
      state.hotelList = data
    },
    set_hotelListParams: function (state, data) {
      state.hotelListParams = {
        ...state.hotelListParams,
        ...data,
      }
    },
    set_hotelListLoading: function (state, data) {
      state.hotelListLoading = data
    },
    set_currentHotelId: function (state, data) {
      state.currentHotelId = data
    },
    set_currentHotelInfo: function (state, data) {
      state.currentHotelInfo = {
        ...state.currentHotelInfo,
        ...data,
      }
    },
    set_orderModalVisible: function (state, data) {
      state.orderModalVisible = data
    },
    set_currentOrderRoom: function (state, data) {
      state.currentOrderRoom = {
        ...state.currentOrderRoom,
        ...data,
      }
    },
    set_orderMatchCouponList: function (state, data) {
      state.orderMatchCouponList = data
    },
    set_hotelComments: function (state, data) {   //新增
      state.commentList = data
    }
  },

  actions: {
    getHotelList: async ({commit, state}) => {
      const res = await getHotelsAPI();
      if (res) {
        commit('set_hotelList', res);
        commit('set_hotelListLoading', false)
      }
    },
    getHotelById: async ({commit, state}) => {
      const res = await getHotelByIdAPI({
        hotelId: state.currentHotelId
      });
      if (res) {
        commit('set_currentHotelInfo', res)
      }
    },
    addOrder: async ({state, commit}, data) => {
      const res = await reserveHotelAPI(data)
      console.log(res)
      if (res) {
        message.success('预定成功')
        commit('set_orderModalVisible', false)
      }
    },
    getOrderMatchCoupons: async ({state, commit}, data) => {
      const res = await orderMatchCouponsAPI(data)
      if (res) {
        commit('set_orderMatchCouponList', res)
      }
    },

    getHotelComments: async ({state, commit}) => {      //新增
      const res = await getHotelCommentsAPI({
        hotelId: state.currentHotelId
      })
      if (res) {
        commit('set_hotelComments', res)
      }
    },
    getHotelOrders: async ({state, commit}) => {
      const res = await getHotelOrders(Number(state.hotelList[0].id))
      if (res) {
        commit('set_orderList', res)
      }
    },
    executeOrder: async ({state, dispatch}, orderId) => {
      const res = await executeOrderAPI(orderId)
      if (res) {
        dispatch('getHotelOrders')
        message.success("入住成功")
      } else {
        message.error("入住失败")
      }
    },
    checkAllOrders: async ({state, commit, dispatch}) => {
      const res = checkOrdersAPI()
      if (res) {
        dispatch('getHotelOrders')
      }
    },
    checkoutOrder: async ({state, dispatch}, orderId) => {
      const res = await checkOutOrderAPI(orderId)
      if (res) {
        dispatch('getHotelOrders')
        message.success("退房成功")
      } else {
        message.error("退房失败")
      }
    },
  }
}

export default hotel

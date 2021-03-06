import {axios} from '@/utils/request'
import {message} from "ant-design-vue";

const api = {
  userPre: '/api/user'
};

export function loginAPI(data) {
  return axios({
    url: `${api.userPre}/login`,
    method: 'POST',
    data
  })
}

export function registerAPI(data) {
  return axios({
    url: `${api.userPre}/register`,
    method: 'POST',
    data
  })
}

export function sendMailAPI(data) {
  return axios({
    url: `${api.userPre}/sendMail`,
    method: 'POST',
    data
  })
}

export function checkoutVarifyCodeAPI(data) {
  return axios({
    url:`${api.userPre}/checkoutVarifyCode`,
    method: 'POST',
    data
  })
}

export function changePasswdAPI(data) {
  return axios({
    url:`${api.userPre}/changePasswd`,
    method: 'POST',
    data
  })
}

export function getUserInfoAPI(id) {
  return axios({
    url: `${api.userPre}/${id}/getUserInfo`,
    method: 'GET'
  })
}

export function updateUserInfoAPI(data) {
  return axios({
    url: `${api.userPre}/${data.id}/userInfo/update`,
    method: 'POST',
    data
  })
}

export function addCreditAPI(data) {
  return axios({
    url: `${api.userPre}/${data.userEmail}/${data.amount}/userInfo/addCredit`,
    method: 'POST',
    data
  })
}

export function getUserCreditRecordsAPI(data) {
  return axios({
    url: `${api.userPre}/${data.userId}/getUserCreditRecords`,
    method: 'GET',
  })
}


<template>
  <div class="main">
      <div class="top">
        <div class="header">
          <div>
            <!-- <img src="@/assets/logo.svg" class="logo" alt="logo"> -->
            <span class="title">NJU酒店管理系统</span>
          </div>
        </div>
        <div class="desc">
          
        </div>
      </div>
    <a-form
      id="formLogin"
      class="user-layout-login"
      ref="formLogin"
      :form="form"
    >
      <a-tabs
        :activeKey="customActiveKey"
        :tabBarStyle="{ textAlign: 'center', borderBottom: 'unset' }"
        @change="handleTabClick"
      >
        <a-tab-pane key="tab1" tab="登录">
          <a-form-item>
            <a-input
              size="large"
              type="text"
              placeholder="邮箱"

              v-decorator="[
                'username',
                {rules: [{ required: true, message: '请输入邮箱地址' }], validateTrigger: 'blur'}
              ]"
            >
              <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-item>

          <a-form-item>
            <a-input-password
              size="large"
              type="password"
              autocomplete="false"
              placeholder="密码"
              v-decorator="[
                'password',
                {rules: [{ required: true, message: '请输入密码' }], validateTrigger: 'blur'}
              ]"
            >
              <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input-password>
          </a-form-item>

          <a-form-item>
           <div class="form-group" style="display: flex;">
             <a-input size="large" type="text" id="code" v-model="code" class="code"  placeholder="请输入您的验证码" >
               <a-icon slot="prefix" type="book" :style="{ color: 'rgba(0,0,0,.25)' }"/>
             </a-input>


                             <div class="login-code" @click="refreshCode">
       <!--验证码组件-->
                       <s-identify :identifyCode="identifyCode"></s-identify>
                       </div>
                           </div>

          </a-form-item>

          <a-form-item style="margin-top:24px">
            <a-button
              size="large"
              type="primary"
              class="login-button"
              :loading="loginLoading"
              @click="handlelogin()"
            >确定</a-button>
          </a-form-item>
        </a-tab-pane>

        <a-tab-pane key="tab2" tab="注册">
          <a-form-item>
            <a-input
                size="large"
                placeholder="用户名"
                v-decorator="[
              'registerUsername',
              {rules: [{ required: true, message: '请输入用户名' }], validateTrigger: 'blur'}]">
              <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-item>

          <a-form-item>

            <a-input
              size="large"
              type="email"
              placeholder="邮箱"
              v-decorator="[
              'registerUserMail', 
              {rules: [{ required: true, type: 'email', message: '请输入正确的邮箱' }], validateTrigger: 'blur'}]">
              <a-icon slot="prefix" type="mail" :style="{ color: 'rgba(0,0,0,.25)' }"/>

            </a-input>


          </a-form-item>

          <a-form-item>
            <div class="form-group" style="display: flex;">
            <a-input

                size="large"
                type="code"
                placeholder="邮件验证码"

                v-decorator="[
              'registerCode',
              {rules: [{ required: true, type: 'book', message: '验证码错误或已过期' }], validateTrigger: 'blur'}]"><a-icon slot="prefix" type="book" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
              <div>
                <a-button size="large" @click="sendcode" v-if="issend">获取验证码</a-button>
                <a-button size="large" v-if="!issend" disabled type="primary">{{ count }}s后可再次发送</a-button>
              </div>
            </div>
          </a-form-item>


<!--新增性别-->
<!--          <a-form-item>-->
<!--            <a-select-->
<!--                    size="large"-->
<!--                    placeholder="性别"-->
<!--                    v-decorator="[-->
<!--              'registerSexType',-->
<!--              {rules: [{ required: true, message: '请选择您的性别' }], validateTrigger: 'blur'}]">-->
<!--              <a-select-option v-for="item in sex" :key="item" :value="item">{{item}}</a-select-option>-->
<!--            </a-select>-->
<!--          </a-form-item>-->

<!-- 身份证号码-->
<!--          <a-form-item>-->
<!--            <a-input-->
<!--                    size="large"-->
<!--                    placeholder="身份证号码"-->
<!--                    v-decorator="[-->
<!--              'registerIdNumber',-->
<!--              {rules: [{ required: true, message: '请输入您的身份证号码' },{ validator: this.handleidNumber }], validateTrigger:'blur'}]">-->
<!--              <a-icon slot="prefix" type="book" :style="{ color: 'rgba(0,0,0,.25)' }"/>-->
<!--            </a-input>-->
<!--          </a-form-item>-->
<!--新增出生日期-->
<!--          <a-form-item>-->
<!--            <a-date-picker-->
<!--                    style="width: 100%"-->
<!--                    size="large"-->
<!--                    placeholder="出生日期"-->
<!--                    v-decorator="[-->
<!--              'registerBirth_date']">-->
<!--              <a-icon slot="prefix" type="book" :style="{ color: 'rgba(0,0,0,.25)' }"/>-->
<!--            </a-date-picker>-->
<!--          </a-form-item>-->

<!--           <a-form-item>-->
<!--            <a-input-->
<!--              size="large"-->
<!--              placeholder="手机号"-->
<!--              maxlength=11-->
<!--              v-decorator="[-->
<!--              'registerPhoneNumber', -->
<!--              {rules: [{ required: true, message: '请输入手机号' }, { validator: this.handlePhoneNumber }], validateTrigger: 'blur'}]">-->
<!--              <a-icon slot="prefix" type="book" :style="{ color: 'rgba(0,0,0,.25)' }"/>-->
<!--            </a-input>-->
<!--          </a-form-item>-->
          <a-form-item>
            <a-input-password
              size="large"
              type="password"
              placeholder="密码"
              v-decorator="[
                'registerPassword', 
                {rules: [{ required: true, message: '请输入密码' }, { validator: this.handlePassword }]}]">
              <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input-password>
          </a-form-item>


            <div class='input_span'>
              <label >密码强度:</label>
              <span id="one"></span>
              <span id="two"></span>
              <span id="three"></span>
            </div>
            <div id="font">
              <span>弱</span>
              <span>中</span>
              <span>强</span>
            </div>


          <a-form-item>
            <a-input-password
              size="large"
              type="password"
              placeholder="确认密码"
              v-decorator="[
                'registerPasswordconfirm', 
                {rules: [{ required: true, message: '请输入密码' }, { validator: this.handlePasswordCheck }], validateTrigger: 'blur'}]">
              <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input-password>
          </a-form-item>
           <a-form-item style="margin-top:24px">
            <a-button
              size="large"
              type="primary"
              class="login-button"
              :loading="registerLoading"
              @click="handleRegister()"
            >确定</a-button>
          </a-form-item>
        </a-tab-pane>
      </a-tabs>
    </a-form>

  </div>
</template>

<script >
import { mapGetters, mapActions, mapMutations } from 'vuex'
const moment = require('moment')
import SIdentify from "./user/components/varifyCode";
import Vue from 'vue'

export default {

  name: 'login',
  components: {
    SIdentify
  },
  data () {
    return {
      customActiveKey: 'tab1',
      loginLoading: false,
      registerLoading: false,
      form: this.$form.createForm(this),
      userTypeValue: 0,
      sex: ['男', '女'],

      identifyCodes: "1234567890",
      identifyCode: "",
      code:"",//text框输入的验证码
      count: 60,
      issend: true
    }
  },
  computed: {
    ...mapGetters([
      'token',
      'isRegistered'
    ])
  },
  mounted() {
  this.refreshCode();
  this.checkAllOrders()
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    },

  },
  methods: {
    ...mapActions([
      'login',
      'sendMail',
      'register',
      'checkAllOrders'
    ]),

    mounted(){   //新增
      this.identifyCode = "";
      this.makeCode(this.identifyCodes, 4);
    },
    created(){     //新增
      this.refreshCode()
    },
    // changeUserType(event){
    //   if(event==1){this.userType="普通用户"}
    //   else if(event==2){this.userType="酒店工作人员"}
    //   else if(event==3){this.userType="网站管理人员"}
    //   else{this.userType="网站营销人员"}
    // },

    // handler

     //验证码
     randomNum(min, max) {
       return Math.floor(Math.random() * (max - min) + min);
   },

 refreshCode() {
       this.identifyCode = "";
      this.makeCode(this.identifyCodes, 4);
   },
 makeCode(o, l) {
       for (let i = 0; i < l; i++) {
             this.identifyCode += this.identifyCodes[
                       this.randomNum(0, this.identifyCodes.length)
             ];
         }
       console.log(this.identifyCode);
   },

    handleidNumber(rule, value, callback) {
      value="null"
      // if (value.length !== 18) {
      //   callback(new Error('身份证号码是18位'))
      // }
      callback()
    },

    checkStrong(sValue) {
      var modes = 0;
      //正则表达式验证符合要求的
      if (sValue.length < 1) return modes;
      if (/\d/.test(sValue)) modes++; //数字
      if (/[a-z]/.test(sValue)) modes++; //小写
      if (/[A-Z]/.test(sValue)) modes++; //大写
      if (/\W/.test(sValue)) modes++; //特殊字符

      //逻辑处理
      switch (modes) {
        case 1:
          return 1;
        case 2:
          return 2;
        case 3:
          return 3;
        case 4:
          return 4;
      }
      return modes;
    },

    handlePhoneNumber(rule, value, callback) {
      //为了值是空的时候和“请输入手机号”两个提示同时出现
      if(value.length==0){
        callback()
      }
      const regex=/^1[0-9]{10}$/
      if(regex.test(value)){
        if (value.length !== 11) {
          callback(new Error('请输入正确的手机号码'))
        }
        callback()
      }else {
        callback(new Error('请输入正确的手机号码'))
      }

      callback()
    },
    handlesexType(rule, value, callback) {
          value="null"
      // if (value !== '男'&&value !== '女') {
      //   callback(new Error('请输入男或女'))
      // }
    },

    handleUsernameOrEmail (rule, value, callback) {
      const { state } = this
      const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
      if (regex.test(value)) {
        callback()
      } else {
        callback(new Error('请输入有效用户名或邮箱'))
      }
      callback()
    },

    handleBirthDate(rule, value, callback){
      // if (value === ''){
      //   callback(new Error('请选择出生日期'))
      // }
      value="null"
      callback();
    },
    handlePassword(rule, value, callback) {
      if (value.length < 6 || value.length >18) {
        callback(new Error('密码长度应是6-18位'))
      }
      this.msgText = this.checkStrong(value);
      if (this.msgText > 1 || this.msgText == 1) {
        document.getElementById("one").style.background = "red";
      } else {
        document.getElementById("one").style.background = "#eee";
      }
      if (this.msgText > 2 || this.msgText == 2) {
        document.getElementById("two").style.background = "orange";
      } else {
        document.getElementById("two").style.background = "#eee";
      }
      if (this.msgText == 4) {
        document.getElementById("three").style.background = "#00D1B2";
      } else {
        document.getElementById("three").style.background = "#eee";
      }
      callback()
    },
    handlePasswordCheck (rule, value, callback) {
      const password = this.form.getFieldValue('registerPassword')
      console.log(password)
      if (value === undefined) {
        callback(new Error('请输入密码'))
      }
      if (value && password && value.trim() !== password.trim()) {
        callback(new Error('两次密码不一致'))
      }
      callback()
    },
    handleTabClick (key) {
      this.customActiveKey = key
    },
    handlelogin() {
      const validateFieldsKey = this.customActiveKey === 'tab1' ? ['username', 'password'] : ['registerUsername', 'registerUserMail','registerPassword','registerPasswordconfirm']
      this.form.validateFields(validateFieldsKey, { force: true }, async (err, values) => {
        if(!err){
          this.loginLoading = true
          if(this.code==""){
            alert("验证码不能为空！");
            this.loginLoading = false;
            return;
          }
          if(this.identifyCode!==this.code){
            this.code="";
            this.refreshCode();
            alert("请输入正确的验证码！")
            this.loginLoading = false;
            return;
          }

          const data = {
            email: this.form.getFieldValue("username"),
            password: this.$md5(this.form.getFieldValue("password")).toString().substring(0,10)
          }
          await this.login(data)
          this.loginLoading = false
        }
      })
    },

    handleRegister() {
      const { form: { validateFields } } = this
      const validateFieldsKey = this.customActiveKey === 'tab1' ? ['username', 'password'] : ['registerUsername','registerPhoneNumber','registerUserMail','registerPassword','registerPasswordconfirm']
      validateFields(validateFieldsKey, { force: true }, async (err, values) => {
        if (!err) {
          this.registerLoading = true
          const data = {
            email: this.form.getFieldValue('registerUserMail'),
            registerCode: this.form.getFieldValue('registerCode'),
            password: this.$md5(this.form.getFieldValue('registerPassword')).toString().substring(0,10),
            sexType: this.form.getFieldValue('registerSexType'),           //新增用户性别
            idNumber: this.form.getFieldValue('registerIdNumber'),
            birth_date: moment(this.form.getFieldValue('registerBirth_date')).format('YYYY-MM-DD'),        //新增用户出生日期
            phoneNumber: this.form.getFieldValue('registerPhoneNumber'),
            userName: this.form.getFieldValue('registerUsername'),
            vip_level:"普通会员",
            credit: 100,
            userType: this.userTypeValue
          }
          await this.register(data).then((result) => {
            this.customActiveKey = 'tab1'
            document.getElementById("one").style.background = "#eee";
            document.getElementById("two").style.background = "#eee";
            document.getElementById("three").style.background = "#eee";
            this.form.setFieldsValue({
              'registerUserMail': '',
              'registerCode': '',
              'registerPassword': '',
              'registerPasswordconfirm': '',
              'registerPhoneNumber': '',
              'registerUsername': ''
            })
          })

          this.registerLoading = false
        }
      });
    },
      sendcode() {
        if(this.form.getFieldValue('registerUserMail').length==0){
          this.issend = true;
        }
        const TIME_COUNT = 60;
        if (!this.timer) {
          this.count = TIME_COUNT;
          this.issend = false;
          const data = this.form.getFieldValue('registerUserMail')
          console.log(data)
          this.sendMail(data)
          //这里可以插入$axios调用后台接口
          this.timer = setInterval(() => {
            if (this.count > 0 && this.count <= TIME_COUNT) {
              this.count--;
            } else {
              this.issend = true;
              clearInterval(this.timer);
              this.timer = null;
            }
          }, 1000);
        }
      }
    }
}
</script>

<style lang="less" scoped>

.main{
    min-width: 260px;
    width: 368px;
    margin: 100px auto;
    .top {
        text-align: center;

        .header {
          height: 44px;
          line-height: 44px;

          .badge {
            position: absolute;
            display: inline-block;
            line-height: 1;
            vertical-align: middle;
            margin-left: -12px;
            margin-top: -10px;
            opacity: 0.8;
          }

          .logo {
            height: 44px;
            vertical-align: top;
            margin-right: 16px;
            border-style: none;
          }

          .title {
            font-size: 33px;
            color: rgba(0, 0, 0, .85);
            font-family: Avenir, 'Helvetica Neue', Arial, Helvetica, sans-serif;
            font-weight: 600;
            position: relative;
            top: 2px;
          }
        }
        .desc {
          font-size: 14px;
          color: rgba(0, 0, 0, 0.45);
          margin-top: 12px;
          margin-bottom: 40px;
        }
      }
}
.user-layout-login {
  label {
    font-size: 14px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  .forge-password {
    font-size: 14px;
  }

  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    height: 40px;
    width: 100%;
  }

  .user-login-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;

    .item-icon {
      font-size: 24px;
      color: rgba(0, 0, 0, 0.2);
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: #1890ff;
      }
    }

    .register {
      float: right;
    }
  }
}

.input_span span {
  display: inline-block;
  width: 85px;
  height: 10px;
  background: #eee;
  line-height: 20px;
}

#one {
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
  border-right: 0px solid;
  margin-left: 20px;
  margin-right: 3px;
}

#two {
  border-left: 0px solid;
  border-right: 0px solid;
  margin-left: -5px;
  margin-right: 3px;
}

#three {
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
  border-left: 0px solid;
  margin-left: -5px;
}
#font span:nth-child(1){
  color:red;
  margin-left: 120px;
}
#font span:nth-child(2){
  color:orange;
  margin: 0 67px;
}
#font span:nth-child(3){
  color:#00D1B2;
}
</style>

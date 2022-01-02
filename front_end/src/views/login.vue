<template>
  <div class="main">
    <div class="top">
      <div class="header">
        <div>
          <!-- <img src="@/assets/logo.svg" class="logo" alt="logo"> -->
          <span class="title">住哪儿</span>
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
              v-if="!isToRetrieve"
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
              <a-icon slot="prefix" type="mail" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-item>

          <a-form-item v-if="isCodeLogin">
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

          <a-form-item v-if="isCodeLogin">
            <div class="form-group" style="display: flex;">
              <a-input
                      :maxLength="4" size="large" type="text" id="code" v-model="code" class="code"
                      placeholder="请输入右侧验证码"
                      @change="this.handleDynamicCode"
              >
                >
                <a-icon slot="prefix" type="book" :style="{ color: 'rgba(0,0,0,.25)' }"/>
              </a-input>

              <div class="login-code" @click="refreshCode">
                <!--验证码组件-->
                <s-identify :identifyCode="identifyCode"></s-identify>
              </div>
            </div>
            <a-alert message="验证码不能为空" type="error" v-if="NoneVarifyCode "/>
            <a-alert message="验证码错误" type="error" v-if="isWrongDynamicCode "/>
          </a-form-item>
          <a-form-item v-if="!isCodeLogin">
            <div class="form-group" style="display: flex;">
              <a-input
                      :maxLength="6"
                      size="large"
                      type="code"
                      placeholder="邮件验证码"
                      v-decorator="[
              'loginVarifyCode',
              {rules: [{ required: true, type: 'book', message: '验证码错误或已过期' }], validateTrigger: 'blur'}]">
                <a-icon slot="prefix" type="book" :style="{ color: 'rgba(0,0,0,.25)' }"/>
              </a-input>
              <div>
                <a-button size="large" @click="sendLogincode" type="primary" v-if="issendLogin">获取验证码</a-button>
                <a-button size="large" v-if="!issendLogin" disabled type="primary">{{ countLogin }}s后重新获取</a-button>
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
            >确定
            </a-button>
          </a-form-item>

          <!--加验证码登录和忘记密码功能-->
          <a-button
                  type="link"
                  class="code-login-button"
                  style="float:left;"
                  :disabled="codeloginDisabled"
                  v-on:click="handleCodeLogin"
          ><span v-if="isCodeLogin">邮箱验证码登录</span>
            <span v-if="!isCodeLogin">密码登录</span>
          </a-button>

          <a-button
                  type="link"
                  class="forget-passwd-button"
                  style="float:right;"
                  v-on:click="handleForgetPassword"
          >忘记密码
          </a-button>

        </a-tab-pane>

        <a-tab-pane key="tab2" tab="注册">
          <a-form-item>
            <a-input
                    size="large"
                    placeholder="用户名"
                    v-decorator="['registerUsername',
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
                      :maxLength="6"
                      size="large"
                      type="code"
                      placeholder="邮件验证码"

                      v-decorator="[
              'registerCode',
              {rules: [{ required: true, type: 'book', message: '验证码错误或已过期' }], validateTrigger: 'blur'}]">
                <a-icon slot="prefix" type="book" :style="{ color: 'rgba(0,0,0,.25)' }"/>
              </a-input>
              <div>
                <a-button size="large" type="primary" @click="sendcode" v-if="issend">获取验证码</a-button>
                <a-button size="large" v-if="!issend" disabled type="primary">{{ count }}s后重新获取</a-button>
              </div>
            </div>
          </a-form-item>

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

          <div class='input_span' style="margin-left: 14px">
            <label>密码强度:</label>
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
            >确定
            </a-button>
          </a-form-item>
        </a-tab-pane>
      </a-tabs>

      <div v-else>
        <a-steps :current="retrieveStep" size="small">
          <a-step title="邮箱验证"/>
          <a-step title="设置新密码"/>
          <a-step title="完成"/>
        </a-steps>

        <!-- 找回密码第一步，邮箱验证 -->
        <div v-if="retrieveStep === 0">
          <a-form-item>
            <a-input size="large" type="email" placeholder="邮箱" style="margin-top: 55px"
                     v-decorator="[
              'retrieveEmail',
              {rules: [{ required: true, type: 'email', message: '请输入正确的邮箱' }], validateTrigger: 'blur'}]">
              <a-icon slot="prefix" type="mail" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-item>

          <a-form-item>
            <div class="form-group" style="display: flex">
              <a-input size="large" type="code" placeholder="邮件验证码" :maxLength="6"
                       v-decorator="[
              'retrieveCode',
              {rules: [{ required: true, type: 'book', message: '验证码错误或已过期' }], validateTrigger: 'blur'}]">
                <a-icon slot="prefix" type="book" :style="{ color: 'rgba(0,0,0,.25)' }"/>
              </a-input>
              <div>
                <a-button size="large" type="primary" @click="sendRetrieveCode" v-if="issendRetrieve">获取验证码</a-button>
                <a-button size="large" v-if="!issendRetrieve" disabled type="primary">{{ countRetrieve }}s后重新获取
                </a-button>
              </div>
            </div>
          </a-form-item>

          <a-form-item>
            <a-button style="width: 47%; margin-top: 30px" v-on:click="cancelRetrieve" type="danger">取消</a-button>
            <a-button style="width: 47%; margin-top: 30px; float: right" v-on:click="nextStep" type="primary">
              下一步
            </a-button>
          </a-form-item>
        </div>

        <!-- 找回密码第二步，设置新密码 -->
        <div v-else-if="retrieveStep === 1">
          <a-form-item>
            <a-input-password size="large" type="password" placeholder="新密码" style="margin-top: 55px"
                              v-decorator="[
                'retrievePassword',
                {rules: [{ required: true, message: '请输入新密码' }, { validator: this.handleRetrievePassword }]}]">
              <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input-password>
          </a-form-item>

          <div class='input_span' style="margin-left: 14px">
            <label>密码强度:</label>
            <span id="four"></span>
            <span id="five"></span>
            <span id="six"></span>
          </div>
          <div id="font2">
            <span>弱</span>
            <span>中</span>
            <span>强</span>
          </div>

          <a-form-item>
            <a-input-password size="large" type="password" placeholder="确认密码"
                              v-decorator="[
                'retrievePasswordconfirm',
                {rules: [{ required: true, message: '请输入密码' }, { validator: this.handleRetrievePasswordCheck }], validateTrigger: 'blur'}]">
              <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input-password>
          </a-form-item>

          <a-form-item>
            <a-button style="width: 47%; margin-top: 30px" v-on:click="cancelRetrieve" type="danger">取消</a-button>
            <a-button style="width: 47%; margin-top: 30px; float: right" v-on:click="nextStep" type="primary">
              下一步
            </a-button>
          </a-form-item>
        </div>

        <!-- 找回密码第三步，完成 -->
        <div v-else-if="retrieveStep === 2">
          <a-result status="success" sub-title="密码重置成功"></a-result>
          <a-button style="width: 47%; margin: auto; display: block" v-on:click="finishRetrieve" type="primary">完成
          </a-button>
        </div>
      </div>

    </a-form>

  </div>
</template>

<script>
  import {mapGetters, mapActions, mapMutations} from 'vuex'
  import SIdentify from "./user/components/varifyCode";
  import {changePasswdAPI, checkoutVarifyCodeAPI} from "../api/user";
  import {message} from 'ant-design-vue'

  const moment = require('moment');

  export default {

    name: 'login',
    components: {
      SIdentify,
    },
    data() {
      return {
        customActiveKey: 'tab1',
        loginLoading: false,
        registerLoading: false,
        form: this.$form.createForm(this),
        userTypeValue: 0,
        sex: ['男', '女'],
        codeloginDisabled: false,
        identifyCodes: "1234567890",
        identifyCode: "",
        code: "",//text框输入的验证码
        count: 60,            // 注册时 邮箱验证码倒计时
        countLogin: 60,       // 登录时 邮箱验证码倒计时
        countRetrieve: 60,    // 找密码时 邮箱验证码倒计时
        issend: true,
        isToRetrieve: false,
        retrieveStep: 0,
        issendLogin: true,
        issendRetrieve: true,
        isCodeLogin: true,
        textLoginWay: "邮箱验证码登录",
        NoneVarifyCode: false,
        isWrongDynamicCode: false,
        retrieveEmail: '',
        differntRetrievePasswd: true,
        pageTimers: {},//计时器数组
        resgisterTimer: 0,//注册的计时器
        loginWithCodeTimer: 1,//登录的计时器
        forgetPasswdTimer: 2,//找回密码的计时器
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
      this.checkAllOrders();
    },
    watch: {
      $route: {
        handler: function (route) {
          this.redirect = route.query && route.query.redirect;
        },
        immediate: true
      },
    },
    methods: {
      ...mapActions([
        'login',
        'sendMail',
        'register',
        'checkAllOrders',
        'retrievePassword'
      ]),

      mounted() {   //新增
        this.identifyCode = "";
        this.makeCode(this.identifyCodes, 4);
      },

      created() {     //新增
        this.refreshCode()
      },

      // changeUserType(event){
      //   if(event==1){this.userType="普通用户"}
      //   else if(event==2){this.userType="酒店工作人员"}
      //   else if(event==3){this.userType="网站管理人员"}
      //   else{this.userType="网站营销人员"}
      // },

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
        if (value.length === 0) {
          callback()
        }
        const regex = /^1[0-9]{10}$/;
        if (regex.test(value)) {
          if (value.length !== 11) {
            callback(new Error('请输入正确的手机号码'))
          }
          callback()
        } else {
          callback(new Error('请输入正确的手机号码'))
        }

        callback()
      },

      handlePassword(rule, value, callback) {
        if (value.length < 6 || value.length > 18) {
          callback(new Error('密码长度应是6-18位'))
        }
        this.msgText = this.checkStrong(value);
        if (this.msgText > 1 || this.msgText === 1) {
          document.getElementById("one").style.background = "red";
        } else {
          document.getElementById("one").style.background = "#eee";
        }
        if (this.msgText > 2 || this.msgText === 2) {
          document.getElementById("two").style.background = "orange";
        } else {
          document.getElementById("two").style.background = "#eee";
        }
        if (this.msgText === 4) {
          document.getElementById("three").style.background = "#00D1B2";
        } else {
          document.getElementById("three").style.background = "#eee";
        }
        callback()
      },

      handlePasswordCheck(rule, value, callback) {
        const password = this.form.getFieldValue('registerPassword');
        console.log(password);
        if (value === undefined) {
          callback(new Error('请输入密码'))
        }
        if (value && password && value.trim() !== password.trim()) {
          callback(new Error('两次密码不一致'))
        }
        callback()
      },

      handleRetrievePassword(rule, value, callback) {
        if (value.length < 6 || value.length > 18) {
          callback(new Error('密码长度应是6-18位'))
        }
        this.msgText = this.checkStrong(value);
        if (this.msgText > 1 || this.msgText === 1) {
          document.getElementById("four").style.background = "red";
        } else {
          document.getElementById("four").style.background = "#eee";
        }
        if (this.msgText > 2 || this.msgText === 2) {
          document.getElementById("five").style.background = "orange";
        } else {
          document.getElementById("five").style.background = "#eee";
        }
        if (this.msgText === 4) {
          document.getElementById("six").style.background = "#00D1B2";
        } else {
          document.getElementById("six").style.background = "#eee";
        }
        callback()
      },

      handleRetrievePasswordCheck(rule, value, callback) {
        const retrievePassword = this.form.getFieldValue('retrievePassword');
        if (value === undefined) {
          callback(new Error('请输入密码'))
        }
        if (value && retrievePassword && value.trim() !== retrievePassword.trim()) {
          this.differntRetrievePasswd = true;
          callback(new Error('两次密码不一致'))
        }
        if (value.length >= 6 && retrievePassword.length >= 6 && value.trim() == retrievePassword.trim()) {
          this.differntRetrievePasswd = false;
          callback()
        }
      },

      handleTabClick(key) {
        this.customActiveKey = key
      },

      handlelogin() {
        const validateFieldsKey = this.customActiveKey === 'tab1' ? ['username', 'password'] : ['registerUsername', 'registerUserMail', 'registerPassword', 'registerPasswordconfirm']
        this.form.validateFields(validateFieldsKey, {force: true}, async (err, values) => {
          if (!err) {
            this.loginLoading = true;

            if (this.code === "" && this.isCodeLogin) {
              this.loginLoading = false;
              this.NoneVarifyCode = true;
              return;
            }
            if (this.identifyCode !== this.code && this.isCodeLogin) {
              this.code = "";
              this.refreshCode();
              this.isWrongDynamicCode = true;
              this.loginLoading = false;
              return;
            }
            var passwd = this.form.getFieldValue("password");
            if (!this.isCodeLogin) {
              passwd = "000000"
            }
            const data = {
              email: this.form.getFieldValue("username"),
              password: this.$md5(passwd).toString().substring(0, 10),
              loginVarifyCode: this.form.getFieldValue('loginVarifyCode')
            };
            console.log(this.form.getFieldValue('loginVarifyCode'));
            await this.login(data);
            this.loginLoading = false
          }
        })
      },

      handleRegister() {
        const {form: {validateFields}} = this;
        const validateFieldsKey = this.customActiveKey === 'tab1' ? ['username', 'password'] : ['registerUsername', 'registerPhoneNumber', 'registerUserMail', 'registerPassword', 'registerPasswordconfirm']
        validateFields(validateFieldsKey, {force: true}, async (err, values) => {
          if (!err) {
            this.registerLoading = true;
            const data = {
              email: this.form.getFieldValue('registerUserMail'),
              registerCode: this.form.getFieldValue('registerCode'),
              password: this.$md5(this.form.getFieldValue('registerPassword')).toString().substring(0, 10),
              sexType: this.form.getFieldValue('registerSexType'),           //新增用户性别
              idNumber: this.form.getFieldValue('registerIdNumber'),
              birth_date: moment(this.form.getFieldValue('registerBirth_date')).format('YYYY-MM-DD'),        //新增用户出生日期
              phoneNumber: this.form.getFieldValue('registerPhoneNumber'),
              userName: this.form.getFieldValue('registerUsername'),
              vip_level: "普通会员",
              credit: 100,
              userType: this.userTypeValue
            };
            await this.register(data).then((result) => {
              this.customActiveKey = 'tab1';
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
              .catch((result) => {
                this.registerLoading = false;
              });

          }
        });
      },

      sendcode() {
        if (this.form.getFieldValue('registerUserMail').length === 0) {
          this.issend = true;
        }
        const TIME_COUNT = 60;
        if (!this.pageTimers[this.resgisterTimer]) {
          this.count = TIME_COUNT;
          this.issend = false;
          const data = this.form.getFieldValue('registerUserMail');
          console.log(data);
          this.sendMail(data);

          this.pageTimers[this.resgisterTimer] = setInterval(() => {
            if (this.count > 0 && this.count <= TIME_COUNT) {
              this.count--;
            } else {
              this.issend = true;
              clearInterval(this.pageTimers[this.resgisterTimer]);
              this.pageTimers[this.resgisterTimer] = null;
            }
          }, 1000);
        }
      },

      sendLogincode() {
        console.log(this.form.getFieldValue('username'));
        if (this.form.getFieldValue('username').length === 0) {
          this.issendLogin = true;
        }
        const TIME_COUNT = 60;
        if (!this.pageTimers[this.loginWithCodeTimer]) {
          this.countLogin = TIME_COUNT;
          this.issendLogin = false;
          const data = this.form.getFieldValue('username');
          console.log(data);
          this.sendMail(data);

          this.pageTimers[this.loginWithCodeTimer] = setInterval(() => {
            if (this.countLogin > 0 && this.countLogin <= TIME_COUNT) {
              this.countLogin--;
            } else {
              this.issendLogin = true;
              clearInterval(this.pageTimers[this.loginWithCodeTimer]);
              this.pageTimers[this.loginWithCodeTimer] = null;
            }
          }, 1000);
        }
      },

      sendRetrieveCode() {

        if (this.form.getFieldValue('retrieveEmail').length === 0) {
          this.issendRetrieve = true;
        }
        const TIME_COUNT = 60;
        if (!this.pageTimers[this.forgetPasswdTimer]) {
          this.countRetrieve = TIME_COUNT;
          this.issendRetrieve = false;
          const data = this.form.getFieldValue('retrieveEmail');
          console.log(data);
          this.sendMail(data);
          console.log("0000000");
          this.pageTimers[this.forgetPasswdTimer] = setInterval(() => {
            if (this.countRetrieve > 0 && this.countRetrieve <= TIME_COUNT) {
              this.countRetrieve--;
            } else {
              this.issendRetrieve = true;
              clearInterval(this.pageTimers[this.forgetPasswdTimer]);
              this.pageTimers[this.forgetPasswdTimer] = null;
            }
          }, 1000);
        }
      },

      handleCodeLogin() {
        this.isCodeLogin = !this.isCodeLogin;
      },
      handleForgetPassword() {
        this.isToRetrieve = true;
      },
      // 取消找回密码
      cancelRetrieve() {
        this.isToRetrieve = false;
        this.retrieveStep = 0;
      },
      // 找回密码下一步
      nextStep() {
        console.log('next step', this.retrieveStep);
        var _this = this
        if (_this.retrieveStep === 0) {
          this.retrieveEmail = this.form.getFieldValue('retrieveEmail');
          // 判断邮箱验证码是否正确
          const data = {
            email: this.retrieveEmail,
            loginVarifyCode: this.form.getFieldValue('retrieveCode')
          };
          console.log(data);
          checkoutVarifyCodeAPI(data).then(function (response) {
            console.log(response);
            if (response) {
              _this.retrieveStep = 1;
            }
          });
        } else if (_this.retrieveStep === 1) {
          // 判断密码是否合法
          console.log(this.differntRetrievePasswd)
          if (this.differntRetrievePasswd) {
            return;
          }
          const data = {
            email: this.retrieveEmail,
            password: this.$md5(this.form.getFieldValue('retrievePasswordconfirm')).toString().substring(0, 10)
          };
          console.log(data);
          changePasswdAPI(data).then(function (response) {
            if (response) {
              _this.retrieveStep = 2;
            } else {
              message.error('重置密码失败')
            }
          });

        }
      },
      // 找回密码完成
      finishRetrieve() {
        this.isToRetrieve = false;
        this.retrieveStep = 0;
      },
      handleDynamicCode() {
        if (this.code.length > 0) {
          this.NoneVarifyCode = false;
          this.isWrongDynamicCode = false;
        }
        // if(this.code.length>4){
        //   this.code=this.code.substr(0,4)
        // }
      },
    }
  }
</script>

<style lang="less" scoped>

  .main {
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

  #font span:nth-child(1) {
    color: red;
    margin-left: 120px;
  }

  #font span:nth-child(2) {
    color: orange;
    margin: 0 67px;
  }

  #font span:nth-child(3) {
    color: #00D1B2;
  }

  #four {
    border-top-left-radius: 5px;
    border-bottom-left-radius: 5px;
    border-right: 0px solid;
    margin-left: 20px;
    margin-right: 3px;
  }

  #five {
    border-left: 0px solid;
    border-right: 0px solid;
    margin-left: -5px;
    margin-right: 3px;
  }

  #six {
    border-top-right-radius: 5px;
    border-bottom-right-radius: 5px;
    border-left: 0px solid;
    margin-left: -5px;
  }

  #font2 span:nth-child(1) {
    color: red;
    margin-left: 120px;
  }

  #font2 span:nth-child(2) {
    color: orange;
    margin: 0 67px;
  }

  #font2 span:nth-child(3) {
    color: #00D1B2;
  }
</style>

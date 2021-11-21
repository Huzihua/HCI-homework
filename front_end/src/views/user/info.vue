<template>
    <div class="info-wrapper">
        <a-tabs>
            <a-tab-pane tab="我的信息" key="1">
                <a-form :form="form" style="margin-top: 30px">

                    <a-form-item label="用户名" :label-col="{ span: 3 }" :wrapper-col="{ span: 5, offset: 1  }">
                        <a-input
                                placeholder="请填写用户名"
                                v-decorator="['userName', { rules: [{ required: true, message: '请输入用户名' }] }]"
                                v-if="modify"
                        />
                        <span v-else>{{ userInfo.userName }}</span>
                    </a-form-item>
                    <a-form-item label="邮箱" :label-col="{ span: 3 }" :wrapper-col="{ span: 5, offset: 1 }">
                        <a-input
                                placeholder="请填写邮箱号"
                                v-decorator="['email', { rules: [{ required: true, message: '请输入邮箱号' },,{ validator: this.handleEmail }] }]"
                                v-if="modify"
                        />
                        <span v-else>{{ userInfo.email}}</span>

                    </a-form-item>

                    <!--身份证号码-->
<!--                    <a-form-item label="身份证号码" :label-col="{ span: 3 }" :wrapper-col="{ span: 5, offset: 1 }">-->
<!--                        <span>{{ userInfo.idNumber}}</span>-->
<!--                    </a-form-item>-->

<!--                    &lt;!&ndash;性别&ndash;&gt;-->
<!--                    <a-form-item label="性别" :label-col="{ span: 3 }" :wrapper-col="{ span: 5, offset: 1 }">-->
<!--                        <span>{{ userInfo.sexType}}</span>-->
<!--                    </a-form-item>-->
                    <!--出生日期-->
                    <a-form-item label="出生日期" :label-col="{ span: 3 }" :wrapper-col="{ span: 5, offset: 1 }">

                                  <a-date-picker
                                          style="width: 100%"
                                          size="middle"
                                          v-if="modify"
                                          v-decorator="[
                                    'birth_date']">
                                    <a-icon slot="prefix" type="book" :style="{ color: 'rgba(0,0,0,.25)' }"/>
                                  </a-date-picker>

                        <span v-else>
                          {{ userInfo.birth_date }}
                        </span>
                    </a-form-item>




                    <a-form-item label="手机号" :label-col="{ span: 3 }" :wrapper-col="{ span: 5, offset: 1 }">
                        <a-input
                                placeholder="请填写手机号"
                                v-decorator="['phoneNumber', { rules: [{ required: true, message: '请输入手机号' },{ validator: this.handlePhoneNumber }] }]"
                                v-if="modify"
                        />
                        <span v-else>{{ userInfo.phoneNumber}}</span>

                    </a-form-item>
                    <a-form-item label="会员等级" :label-col="{ span: 3 }" :wrapper-col="{ span: 5, offset: 1 }">
                        <span>{{ userInfo.vip_level }}</span>
                    </a-form-item>
                    <a-form-item label="信用值" :label-col="{ span: 3 }" :wrapper-col="{ span: 5, offset: 1 }">
                        <span>{{ userInfo.credit }}</span>
                    </a-form-item>

<!--                    <a-form-item label="密码" :label-col="{ span: 3 }" :wrapper-col="{ span: 5, offset: 1 }"-->
<!--                                 v-if="modify">-->

<!--                        <a-input-->
<!--                                placeholder="请输入新密码"-->
<!--                                v-decorator="['password', { rules: [{ required: false, message: '请输入新密码' }] }]"-->
<!--                        />-->
<!--                    </a-form-item>-->
                    <a-form-item :wrapper-col="{ span: 12, offset: 5 }" v-if="modify">
                        <a-button type="primary" @click="saveModify">
                            保存
                        </a-button>
                        <a-button type="default" style="margin-left: 30px" @click="cancelModify">
                            取消
                        </a-button>
                    </a-form-item>
                    <a-form-item :wrapper-col="{ span: 8, offset: 4 }" v-else>
                        <a-button type="primary" @click="modifyInfo">
                            编辑
                        </a-button>
                    </a-form-item>
                </a-form>
            </a-tab-pane>
            <a-tab-pane tab="我的订单" key="2">
                <a-table
                        :columns="columns"
                        :dataSource="userOrderList"
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
                    <a-tag slot="orderState" color="blue" slot-scope="text">
                        {{ text }}
                    </a-tag>
                    <span slot="action" slot-scope="record">

    <!--评论按钮-->      <a-button size="small"  :disabled="record.orderState!=='待点评'" @click="abletocomment(record.id)" type="primary" >评论</a-button>

                        <a-modal
                                       title="请写下您的评价"
                                       :visible="evaluateFormVisible&&commentIndex==record.id"
                                       @ok="confirmEvaluateOrder(record)"
                                       @cancel="cancelEvaluateOrder"
                                       okText="确定"
                                       cancelText="取消"
                               >
                              <div>
                                <a-rate v-model="orderRate" :tooltips="desc"/>
                                <span class="ant-rate-text">{{ desc[orderRate - 1] }}</span>
                                  <a-textarea style="margin-top: 20px"
                                              placeholder="请留下您友善的评论"
                                              :autoSize="{ minRows: 2, maxRows: 6 }"
                                              v-model="evaluateContent"
                                  />
                              </div>
                        </a-modal>



                        <a-divider type="vertical" ></a-divider>


                        <a-popconfirm
                                title="你确定撤销该笔订单吗？"
                                @confirm="confirmCancelOrder(record.id)"
                                @cancel="cancelCancelOrder"
                                okText="确定"
                                cancelText="取消"
                                :disabled="record.orderState !== '已预订'"
                        >
                          <a-button type="primary" :disabled="record.orderState !== '已预订'" size="small" >撤销</a-button>

                        </a-popconfirm>
<!--新增查看-->
                        <a-divider type="vertical" v-if="true"></a-divider>
              <a-button size="small"  @click="showOrderDetails(record.id)"  type="primary" >查看</a-button>

                        
                    </span>
                </a-table>
                <order-detail  v-if="showDetail" :back="setShowDetailFalse">
                </order-detail>
            </a-tab-pane>
                        <!--信用记录栏-->
            <a-tab-pane tab="我的信用记录" key="3">

                <a-table
                        :columns="credit"
                        :dataSource="userCreditRecordList"
                        bordered
                >
                </a-table>
            </a-tab-pane>
        </a-tabs>
    </div>
</template>
<script>

    import {mapGetters, mapMutations, mapActions} from 'vuex'

    import orderDetail from "../order/components/orderDetail"
    const columns = [
        {
            title: '订单号',
            dataIndex: 'id',
        },
        {
            title: '酒店名',
            dataIndex: 'hotelName',
        },
        {
            title: '房型',
            dataIndex: 'roomType',
            scopedSlots: {customRender: 'roomType'}
        },
        {
            title: '入住时间',
            dataIndex: 'checkInDate',
            scopedSlots: {customRender: 'checkInDate'}
        },
        {
            title: '离店时间',
            dataIndex: 'checkOutDate',
            scopedSlots: {customRender: 'checkOutDate'}
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
            filters: [{text: '已预订', value: '已预订'}, {text: '已撤销', value: '已撤销'}, {text: '已入住', value: '已入住'}
                ,{ text: '待点评', value: '待点评' },{ text: '异常', value: '异常' }, {text: '已评价', value: '已评价'}],
            onFilter: (value, record) => record.orderState.includes(value),
            dataIndex: 'orderState',
            scopedSlots: {customRender: 'orderState'}
        },
        {
            title: '操作',
            key: 'action',
            scopedSlots: {customRender: 'action'},
        },

    ];
    //信用记录表
    const credit = [
        {
            title: '订单号',
            dataIndex: 'orderId',
        },
        {
            title: '变更时间',
            dataIndex: 'date',
        },
        {
            title: '变更原因',
            filters: [{text: '订单正常执行', value: '订单正常执行'}, {text: '置为异常订单', value: '置为异常订单'}, {
                text: '撤销异常订单',value: '撤销异常订单'},{text:'充值信用',value:'充值信用'},{text:'超时撤销订单',value:'超时撤销订单'}],
            onFilter: (value, record) => record.type.includes(value),
            dataIndex: 'type',
            scopedSlots: {customRender: 'type'}
        },
        {
            title: '信用值变更',
            dataIndex: 'changeCredit',
            scopedSlots: {customRender: 'changeCredit'}
        },
        {
            title: '变更后信用值',
            dataIndex: 'finalCredit',
            scopedSlots: {customRender: 'finalCredit'}
        },
    ];
    export default {
        name: 'info',
        data() {
            return {
                modify: false,   //修改时状态为true
                formLayout: 'horizontal',
                pagination: {},
                columns,
                credit,
                data: [],
                form: this.$form.createForm(this, {name: 'coordinated'}),
                evaluateFormVisible: false,
                orderRate: 3,
                desc: ['terrible', 'bad', 'normal', 'good', 'wonderful'],
                evaluateContent: '',
                commentIndex:'',
                orderDetailFromVisible:false,
                showDetail:false
            }
        },
        components: {
            orderDetail
        },
        computed: {
            ...mapGetters([
                'userId',
                'userInfo',
                'userOrderList',
                'userCreditRecordList',
            ]),
            // hasCommented(){
            //     return this.record.orderState&&this.record.orderState==='已预订'
            // }
        },
        async mounted() {
            await this.getUserInfo()
            await this.getUserOrders()
            await this.getUserCreditRecords()

        },
        methods: {

            ...mapMutations([
                'set_currentOrderId',
            ]),
            ...mapActions([
                'getUserInfo',
                'getUserOrders',
                'updateUserInfo',
                'cancelOrder',
                'updateComment' ,
                'getUserCreditRecords',
                'updateComment',
                'getOrderDetails',
                'getOrderDetails',


            ]),
            saveModify() {
                this.form.validateFields((err, values) => {
                    if (!err) {
                        var birthDate=this.form.getFieldValue('birth_date');
                      if(birthDate==null){
                          birthDate=this.userInfo.birth_date;
                        }else {
                        birthDate=this.form.getFieldValue('birth_date').format('YYYY-MM-DD');
                      }
                      console.log(birthDate)
                        const data = {
                            userName: this.form.getFieldValue('userName'),
                            birth_date:birthDate,
                            phoneNumber: this.form.getFieldValue('phoneNumber'),
                            // password: this.$md5(this.form.getFieldValue('password')).toString().substring(0,10),
                            email: this.form.getFieldValue('email')

                        }
                        this.updateUserInfo(data).then(() => {
                            this.modify = false
                        })
                    }
                });
            },
            modifyInfo() {
                setTimeout(() => {
                    this.form.setFieldsValue({
                        'userName': this.userInfo.userName,
                        'phoneNumber': this.userInfo.phoneNumber,
                        'email': this.userInfo.email
                    })
                }, 0)
                this.modify = true
            },
            cancelModify() {
                this.modify = false
            },
            abletocomment(orderid){
                this.commentIndex=orderid
                this.evaluateFormVisible=true
            },
            confirmCancelOrder(orderId) {
                this.cancelOrder(orderId)
            },
            cancelCancelOrder() {

            },
            confirmEvaluateOrder(record) {       //评论完后点击确认执行该函数，新增
                console.log("record",record)
                this.commentIndex=record.id
                const data={
                    orderId:record.id,
                    comment:this.evaluateContent,
                    orderRate: this.orderRate,
                }
                 //console.log(data)
                this.updateComment(data)      //调用后端API更新评论内容到相应订单，新增
                this.evaluateFormVisible = false;
                // if (res){
                //     console.log("评价成功")
                // }
            },
            cancelEvaluateOrder() {       //评论完后点击取消啥都不做，新增
                this.evaluateFormVisible = false;
            },
            setShowDetailFalse(){
                this.showDetail=false;
            },
            setShowDetailTrue(){
                this.showDetail=true;
            },

            showOrderDetails(orderId){ //查看订单详细信息
                this.set_currentOrderId(orderId)
                this.getOrderDetails()
                this.setShowDetailTrue()
                console.log(this.orderDetailInfo)
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

          handleEmail (rule, value, callback) {
            const { state } = this
            const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
            if (regex.test(value)) {
              callback()
            } else {
              callback(new Error('请输入有效邮箱'))
            }
            callback()
          },

        }
    }
</script>
<style scoped lang="less">
    .info-wrapper {
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
    .info-wrapper {
        .ant-tabs-bar {
            padding-left: 30px
        }
    }
</style>
<style lang="less">

</style>
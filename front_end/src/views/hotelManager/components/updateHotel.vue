<template>
    <a-modal
            :visible="updateHotelModalVisible"
            title="修改酒店信息"
            ok-text="确认"
            cancel-text="取消"
            @ok="handleSubmit"
            @cancel="cancel"
    >
        <a-form :form="form" style="margin-top: 30px" >

            <a-form-item label="酒店名">
                <a-input
                        v-decorator="[
                                    'hotelName',
                                    {rules: [{ required: true, message: '请输入酒店名' }] },]"
                />
            </a-form-item>


            <a-form-item label="商圈">
                <a-input
                        v-decorator="[
                                    'bizRegion',
                                    {rules: [{ required: true, message: '请输入所属商圈' }] },]"
                />
            </a-form-item>

            <a-form-item label="地址">
                <a-input
                        v-decorator="[
                                    'address',
                                    {rules: [{ required: true,
                                    message:
                                    '请输入地址' }] },]"
                />
            </a-form-item>

            <a-form-item label="酒店星级">
                <a-select
                        v-decorator="[
                    'hotelStar',
                    { rules: [{ required: true, message: '请选择酒店星级' }] }]"
                        @change="changeStar"
                >
                    <a-select-option value="Three">三星级</a-select-option>
                    <a-select-option value="Four">四星级</a-select-option>
                    <a-select-option value="Five">五星级</a-select-option>
                </a-select>
            </a-form-item>

            <a-form-item label="简介">
                <a-input
                        v-decorator="[
                                    'description',
                                    {rules: [{ required: true, message: '请输入简介' }] },]"
                />
            </a-form-item>

            <a-form-item label="电话号码">
                <a-input
                        v-decorator="[
                                    'phoneNum',
                                    {rules: [{ required: true, message: '请输入电话号码' }] },]"
                />
            </a-form-item>
        </a-Form>
    </a-modal>
</template>

<script>
    import { mapGetters, mapMutations, mapActions } from 'vuex'
    export default {
        name: "updateHotel",
        data() {
            return{
                
            }
        },

        computed: {
            ...mapGetters([
                'updateHotelModalVisible',
                'userId',
                'addHotelParams',
                'currentHotelId'
            ]),
        },
        beforeCreate() {
            this.form = this.$form.createForm(this, { name: 'updateHotel' });
        },
        mounted() {
        },

        methods: {
            ...mapMutations([
                'set_updateHotelModalVisible',
                'set_addHotelParams',
            ]),
            ...mapActions([
                'addHotel',
                'updateHotel'
            ]),
            cancel() {
                this.set_updateHotelModalVisible(false)
            },
            changeStar(v){

            },
            handleSubmit(e) {
                console.log("good")
                this.form.validateFieldsAndScroll((err, values) => {
                    if (!err) {
                        const data = {
                            name: this.form.getFieldValue('hotelName'),
                            description: this.form.getFieldValue('description'),
                            address: this.form.getFieldValue('address'),
                            phoneNum: this.form.getFieldValue('phoneNumber'),
                            hotelStar: this.form.getFieldValue('hotelStar'),
                            bizRegion:  this.form.getFieldValue('bizRegion'),
                            rate:        this.form.getFieldValue('rate'),
                            managerId: Number(this.userId),
                            id: this.currentHotelId,
                        }
                        this.set_addHotelParams(data)
                        this.updateHotel()
                        this.set_updateHotelModalVisible(false)
                    }
                });
            },
        }
    }
</script>

<style scoped>

</style>

<template>
  <a-card hoverable class="hotelCard ant-col-xs-7 ant-col-lg-5 ant-col-xxl-3">
    <img alt="example" v-if="hotel.id===1" src="@/assets/cover1.jpg" referrerPolicy="no-referrer"/>
    <img alt="example" v-else-if="hotel.id===2" src="@/assets/cover2.jpg" referrerPolicy="no-referrer"/>
    <img alt="example" v-if="hotel.id===3" src="@/assets/cover3.jpg" referrerPolicy="no-referrer"/>
    <img alt="example" v-if="hotel.id===8" src="@/assets/cover4.jpg" referrerPolicy="no-referrer"/>
    <img alt="example" v-if="hotel.id===5" src="@/assets/cover5.jpg" referrerPolicy="no-referrer"/>
    <img alt="example" v-if="hotel.id===6" src="@/assets/cover6.jpg" referrerPolicy="no-referrer"/>
    <img alt="example" v-if="hotel.id===7" src="@/assets/cover7.jpg" referrerPolicy="no-referrer"/>

    <a-card-meta>
      <template slot="description">
        <!--酒店名-->
        <span class="hotel_content"
              style="float: left; font-weight: bold; font-size: large; margin-top: 5px; color: black">
          {{hotel.name}}
        </span>
        <br><br>
        <!--星级-->
        <span class="hotel_content" style="float: left; font-weight: bold">
        星级：<a-rate style="font-size: 15px; margin-left: 3px" :value="starDic.indexOf(hotel.hotelStar) + 1" disabled/>
        </span>
        <!--评分-->
        <span class="score hotel_content">{{hotel.rate.toFixed(1)}}</span>
        <br>
        <!--地址 & 商圈-->
        <span class="hotel_content" style="float: left; margin-top: 6px; margin-bottom: 10px">
          <span style="font-weight: bold">地址：</span>
          {{hotel.address}}
        </span>
        <!--水平分割线-->
        <a-divider style="margin-left: 10px"></a-divider>
        <!--房型-->
        <span class="hotel_content" style="float: left">
          <a-tag color="pink" v-for="type in this.roomTypes" :key="type.index">{{type}}</a-tag>
        </span>
        <!--优惠活动-->
        <!--起价-->
        <br>
        <span v-if="floorPrice !== 99999" class="hotel_content price">
          <span style="font-weight: bold; font-size: 25px; color: rgb(24, 144, 255)">
            ¥{{floorPrice}}
          </span> 起
        </span>
        <span v-else class="hotel_content price">暂无可订房型</span>
      </template>
    </a-card-meta>
  </a-card>
</template>
<script>
  import {mapGetters, mapActions, mapMutations} from 'vuex'
  import {getHotelByIdAPI} from "../../../api/hotel";

  export default {
    name: '',
    props: {
      hotel: {}
    },
    data() {
      return {
        starDic: ['One', 'Two', 'Three', 'Four', 'Five'],
        roomTypes: [],
        floorPrice: 99999,
        pic: '',
        allPics: {

        }
      }
    },
    computed: {
      ...mapGetters([
        'currentHotelInfo'
      ])
    },
    async mounted() {
      this.rooms = await this.getRoomsInfo();
      for (let i = 0; i < this.rooms.length; i++) {
        this.roomTypes.push(this.rooms[i].roomType);
        this.floorPrice = this.rooms[i].price < this.floorPrice ? this.rooms[i].price : this.floorPrice;
      }
    },
    methods: {
      ...mapMutations([
        'set_currentHotelId'
      ]),

      ...mapActions([
        'getHotelById'
      ]),

      async getRoomsInfo() {
        let curHotel = await getHotelByIdAPI({
          hotelId: this.hotel.id
        });
        return curHotel.rooms;
      }
    }
  }
</script>
<style scoped lang="less">
  .hotelCard {
    margin: 10px 22px 10px 22px;
    /*min-width: 45%;*/
    min-width: 600px;
    max-height: 250px;

    img {
      float: left;
      height: 200px;
      width: 200px;
    }
  }

  .hotel_content {
    margin: 0 0 0 30px;
  }

  /* 酒店评分 */
  .score {
    background-color: rgb(24, 144, 255);
    padding: 6px 10px;
    border-radius: 8px;
    color: white;
    font-weight: bold;
    font-size: large;
    margin: 0 0 0 90px;
  }

  /* 起步价 */
  .price {
    float: right;
    margin-right: 10px;
    margin-top: 10px;

  }
</style>

<style lang="less">
  .hotelCard {
    .ant-card-body {
      padding: 12px
    }
  }

  .ant-card-hoverable:hover {
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.4)
  }

</style>

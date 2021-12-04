<template>
  <a-card hoverable class="hotelCard ant-col-xs-7 ant-col-lg-5 ant-col-xxl-3">
    <img alt="example" src="@/assets/cover.jpeg" referrerPolicy="no-referrer"/>
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
        星级：<a-rate style="font-size: 15px" :value="starDic.indexOf(hotel.hotelStar) + 1"/>
        </span>
        <!--评分-->
        <span class="score hotel_content">{{hotel.rate.toFixed(1)}}</span>
        <br>
        <!--地址 & 商圈-->
        <span class="hotel_content" style="float: left; margin-top: 6px; margin-bottom: 10px">
          {{hotel.address}} / {{hotel.bizRegion}}
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
        <span class="hotel_content price"><span style="font-weight: bold; font-size: 25px; color: rgb(24, 144, 255)">¥{{floorPrice}}</span> 起</span>
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
    padding: 7px 10px;
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

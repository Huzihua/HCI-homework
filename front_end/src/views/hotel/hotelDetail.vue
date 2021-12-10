<template>
  <a-layout>
    <a-layout-content>
      <div class="hotelDetailCard">
        <!--        <h1>-->
        <!--          {{ currentHotelInfo.title }}-->
        <!--        </h1>-->
        <!--        <div class="hotel-info" style="display: inline-block">-->
        <!--          <a-card style="width: 240px">-->
        <!--          <img-->
        <!--                  alt="example"-->
        <!--                  src="@/assets/cover.jpeg"-->
        <!--                  slot="cover"-->
        <!--                  referrerPolicy="no-referrer"-->
        <!--          />-->
        <!--          </a-card>-->
        <!--          <div class="info">-->
        <!--            <div class="items" v-if="currentHotelInfo.name">-->
        <!--              <span class="label">酒店名称：</span>-->
        <!--              <span class="value">{{ currentHotelInfo.name }}</span>-->
        <!--            </div>-->
        <!--            <div class="items" v-if="currentHotelInfo.address">-->
        <!--              <span class="label">地址</span>-->
        <!--              <span class="value">{{ currentHotelInfo.address }}</span>-->
        <!--            </div>-->
        <!--            <div class="items" v-if="currentHotelInfo.rate">-->
        <!--              <span class="label">评分:</span>-->
        <!--              <span class="value">{{ currentHotelInfo.rate.toFixed(1) }}</span>-->
        <!--            </div>-->
        <!--            <div class="items" v-if="currentHotelInfo.hotelStar">-->
        <!--              <span class="label">星级:</span>-->
        <!--              <a-rate style="font-size: 15px" :value="starDic.indexOf(currentHotelInfo.hotelStar) + 1" disabled-->
        <!--                      allowHalf/>-->
        <!--            </div>-->
        <!--            <div class="items" v-if="currentHotelInfo.description">-->
        <!--              <span class="label">酒店简介:</span>-->
        <!--              <span class="value">{{ currentHotelInfo.description }}</span>-->
        <!--            </div>-->
        <!--          </div>-->
        <!--        </div>-->
        <div style="max-width: 570px; margin-bottom: 10px">
          <span style="font-weight: bold; font-size: 20px">{{ currentHotelInfo.name }}</span>
          <span class="hotelScore">{{ currentHotelInfo.rate.toFixed(1) }}</span>
          <br>
          <div class="hotelInfo">
            <a-icon type="star" theme="twoTone" style="margin-right: 15px; font-size: large"/>
            <a-rate style="font-size: 15px" :value="starDic.indexOf(currentHotelInfo.hotelStar) + 1" disabled
                    allowHalf/>
          </div>
          <div class="hotelInfo">
            <a-icon type="environment" theme="twoTone" style="margin-right: 15px; font-size: large"/>
            <span>{{ currentHotelInfo.address }}</span>
          </div>
          <div class="hotelInfo">
            <a-icon type="info-circle" theme="twoTone" style="margin-right: 15px; font-size: large"/>
            <span>{{ currentHotelInfo.description }}</span>
          </div>
        </div>

        <baidu-map id="map" :center="{lng: getHotelCoordinate()[0], lat: getHotelCoordinate()[1]}" :zoom="20"
                   style="height: 350px; width: 570px; display: inline-block; float: right; border: solid #9b9b9b 1px;
                          margin-bottom: 2px">
          <bm-marker :position="{lng: getHotelCoordinate()[0], lat: getHotelCoordinate()[1]}"
                     animation="BMAP_ANIMATION_BOUNCE"></bm-marker>
          <bm-navigation anchor="BMAP_ANCHOR_TOP_RIGHT"></bm-navigation>
        </baidu-map>
        <!--        <div id="map" style="height: 350px; width: 600px; display: inline-block; float: right"></div>-->

        <a-divider></a-divider>

        <a-tabs>
          <a-tab-pane tab="房间信息" key="1">
            <RoomList :rooms="currentHotelInfo.rooms"></RoomList>
          </a-tab-pane>
          <a-tab-pane tab="用户评价" key="2">
            <CommentList :comments="commentList">></CommentList>  <!--新增-->
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-layout-content>
  </a-layout>
</template>
<script>
  import {mapGetters, mapActions, mapMutations} from 'vuex'
  import RoomList from './components/roomList'
  import CommentList from './components/commentList'

  export default {
    name: 'hotelDetail',
    components: {
      RoomList,
      CommentList,//新增
    },
    data() {
      return {
        starDic: ['One', 'Two', 'Three', 'Four', 'Five'],
        // hotelLongitude: 118.97024,  // 酒店经度
        // hotelLatitude: 32.12009,    // 酒店纬度
      }
    },
    computed: {
      ...mapGetters([
        'currentHotelInfo',
        'commentList'//新增
      ])
    },
    mounted() {
      this.set_currentHotelId(Number(this.$route.params.hotelId));
      this.getHotelById();
      this.getHotelComments();//新增
    },
    beforeRouteUpdate(to, from, next) {
      this.set_currentHotelId(Number(to.params.hotelId));
      this.getHotelById();
      this.getHotelComments();//新增
      next()
    },
    methods: {
      ...mapMutations([
        'set_currentHotelId',
        'set_hotelComments',//新增
      ]),
      ...mapActions([
        'getHotelById',
        "getHotelComments",//新增
      ]),

      // 根据酒店地址获取经纬度
      getHotelCoordinate() {
        console.log(this.currentHotelInfo.id, this.currentHotelInfo.name);
        const hotelCoordinates = {
          5: [118.798432, 32.045335], // 大地君悦酒店
          6: [118.733997, 31.810431], // 山林悦民居
          7: [118.81311, 31.971188], // 南京樱悦美宿酒店(南京南站店)
          8: [118.809051, 31.971437], // 清沐铂金酒店
        };
        // console.log(hotelCoordinates[this.currentHotelInfo.id]);
        return hotelCoordinates[this.currentHotelInfo.id];
      }
    }
  }
</script>
<style scoped lang="less">
  .hotelDetailCard {
    padding: 50px 50px;
  }

  .hotelScore {
    background-color: rgb(24, 144, 255);
    padding: 6px 10px;
    border-radius: 8px;
    color: white;
    font-weight: bold;
    font-size: large;
    margin: 0 0 0 70px;
  }

  .hotelInfo {
    margin-top: 10px;
  }

  .hotel-info {
    display: flex;
    align-items: stretch;
    justify-content: flex-start;

    .info {
      padding: 10px 0;
      display: flex;
      flex-direction: column;
      margin-left: 20px;

      .items {
        display: flex;
        /*align-items: center;*/
        margin-bottom: 10px;

        .label {
          margin-right: 10px;
          font-size: 18px;
        }

        .value {
          margin-right: 15px
        }
      }
    }
  }
</style>

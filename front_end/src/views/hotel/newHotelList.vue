<template>
  <div>
    <div class="list-search-container">
      <div class="inner-wrap">
        <div class="search-container" id="search-con" style="height: 75px">
          <div class="search-box">
            <div class="search-box-header">目的地</div>
            <div class="search-box-content">
              <a-cascader placeholder="请选择" :options="Dests" v-model="dest" style="width: 100%"></a-cascader>
            </div>
          </div>
          <div class="search-box">
            <div class="search-box-header">入住时间</div>
            <div class="search-box-content">
              <a-range-picker></a-range-picker>
            </div>
          </div>
          <div class="search-box">
            <div class="search-box-header">星级</div>
            <div class="search-box-content">
              <a-cascader placeholder="请选择" v-model="hotelStar" :options="Stars" style="width: 100%"></a-cascader>
            </div>
          </div>
          <div class="search-box">
            <div class="search-box-header">关键词(选填)</div>
            <div class="search-box-content">
              <a-input v-model="importantKey" placeholder="酒店名称或地址"/>
            </div>
          </div>
          <a-form-item>
            <a-button @click="searchHotel" type="primary" style="height: 60px">
              <a-icon type="search" style="font-size: 25px"></a-icon>
            </a-button>
          </a-form-item>
        </div>
        <a-divider style="margin: 0 0 10px 0"></a-divider>
        <div class="search-box">
<!--          <div class="search-box-header">排序</div>-->
          <div class="search-box-content" style="display: inline-block">
            <a-radio-group v-on:change="sortHotel">
              <a-radio-button style="width: 120px; text-align: center" value="byRate">
                评分降序
              </a-radio-button>
              <a-radio-button style="width: 120px; text-align: center" value="byStar">
                星级降序
              </a-radio-button>
            </a-radio-group>
          </div>
        </div>
      </div>
    </div>
    <div class="hotelList">
      <a-layout>
<!--        <a-layout-header v-if="search" style="background-color: transparent; padding: 0">-->
<!--          <a-menu v-model="currentOrder" mode="horizontal" :default-selected-keys="['grade']"-->
<!--                  @click="sortOrderChange">-->
<!--            <template v-for="item in sortOrderList">-->
<!--              <a-menu-item v-if="!item.children" :key="item.key">-->
<!--                <a-icon type="mail"/>-->
<!--                <span>{{item.title}}</span>-->
<!--              </a-menu-item>-->
<!--              <a-sub-menu v-else :key="item.key">-->
<!--                <span slot="title"><a-icon type="mail"/><span>{{item.title}}</span></span>-->
<!--                <a-menu-item v-for="i in item.children" :key="i.key">-->
<!--                  {{i.title}}-->
<!--                </a-menu-item>-->
<!--              </a-sub-menu>-->
<!--            </template>-->
<!--          </a-menu>-->
<!--        </a-layout-header>-->
        <a-layout-content style="min-width: 800px">
          <a-spin :spinning="hotelListLoading">
            <div class="card-wrapper">

              <div v-if="search">
                <HotelCard :hotel="item" v-for="item in resultHotel" :key="item.index"
                           @click.native="jumpToDetails(item.id)"></HotelCard>
                <div v-for="item in emptyBox" :key="item.name"
                     class="emptyBox ant-col-xs-7 ant-col-lg-5 ant-col-xxl-3">
                </div>
              </div>
              <div v-else>
                <HotelCard :hotel="item" v-for="item in hotelList" :key="item.index"
                           @click.native="jumpToDetails(item.id)"></HotelCard>
                <div v-for="item in emptyBox" :key="item.name"
                     class="emptyBox ant-col-xs-7 ant-col-lg-5 ant-col-xxl-3">
                </div>
              </div>
            </div>
          </a-spin>
        </a-layout-content>
        <a-layout-footer>
          <a-divider style="color: rgb(140,140,140); font-weight: lighter">没有更多酒店了</a-divider>
          <!--            <a-pagination showQuickJumper :total="hotelList.totalElements" :defaultCurrent="1"-->
          <!--                          @change="pageChange"></a-pagination>-->
        </a-layout-footer>
      </a-layout>
    </div>
  </div>
</template>


<script>
  import HotelCard from './components/hotelCard'
  import {mapGetters, mapActions, mapMutations} from 'vuex'
  // import AddRoomModal from "../hotelManager/components/addRoomModal";

  const bizRegionList = [
    {
      name: '全部',
      key: 'All',
    },
    {
      name: '西单',
      key: 'XiDan',
    }];

  const starList = [
    {
      label: "五星级",
      value: "Five",
      num: 5,
    },
    {
      label: "四星级",
      value: "Four",
      num: 4,
    },
    {
      label: "三星级",
      value: "Three",
      num: 3,
    },
    {
      label: "二星级",
      value: "Two",
      num: 2,
    },
    {
      label: "一星级",
      value: "One",
      num: 1,
    },]

  const sortOrderList = [
    {
      key: 'grade',
      title: '按评分排序',
    },
    // {
    //     key: 'price',
    //     title: '按价格排序',
    //     children: [
    //         {
    //             key: 'priceAscending',
    //             title: '按星级低->高',
    //         },
    //         {
    //             key: 'priceDescending',
    //             title: '按星级高->低',
    //         }
    //     ]
    // },
    {
      key: 'hotelStar',
      title: '按星级排序',
      // showedTitle: '按星级排序',
      children: [
        {
          key: 'hotelStarAscending',
          title: '按星级低->高',
        },
        {
          key: 'hotelStarDescending',
          title: '按星级高->低',
        }
      ]
    }];

  const Dests = [
    {
      value: 'jiangsu',
      label: '江苏',
      children: [
        {
          value: 'nanjing',
          label: '南京',
        },
      ],
    },
    {
      value: 'zhejiang',
      label: '浙江',
      children: [
        {
          value: 'hangzhou',
          label: '杭州',
        },
      ],
    },
  ];

  const Stars = [
    {
      value: 'All',
      label: '全选',
    },
    {
      value: 'Five',
      label: '五星级',
    },
    {
      value: 'Four',
      label: '四星级',
    },
    {
      value: 'ThreeAndBelow',
      label: '三星及以下',
    },
  ];

  export default {
    name: 'home',
    components: {
      // AddRoomModal,
      HotelCard,
    },
    data() {
      return {
        emptyBox: [{name: 'box1'}, {name: 'box2'}, {name: 'box3'}],
        dest: [],
        bizRegion: "",
        importantKey: "",
        hotelStar: "",
        resultHotel: [],
        bizRegionList,
        starList,
        starFilters: ["Five", "Four", "Three", "Two", "One"],
        search: false,
        currentOrder: ["grade"],
        sortOrderList,
        Dests,
        Stars,
      }
    },
    async mounted() {
      await this.getHotelList();
      this.search = false;
    },
    computed: {
      ...mapGetters([
        'hotelList',
        'hotelListLoading'
      ]),
    },
    methods: {
      ...mapMutations([
        'set_hotelListParams',
        'set_hotelListLoading'
      ]),
      ...mapActions([
        'getHotelList'
      ]),
      pageChange(page, pageSize) {
        const data = {
          pageNo: page - 1
        };
        this.set_hotelListParams(data);
        this.set_hotelListLoading(true);
        this.getHotelList();
      },
      jumpToDetails(id) {
        this.$router.push({name: 'hotelDetail', params: {hotelId: id}});
      },

      searchHotel() {
        // console.log(this.hotelList);
        // console.log(this.bizRegion);
        console.log('dest', this.dest);
        this.search = true;
        let hotels = [];
        console.log('hotelStar', this.hotelStar);
        hotels = this.hotelList.filter((h) =>
          (this.hotelStar[0] === "All" || this.hotelStar[0] === h.hotelStar || (this.hotelStar[0] === "ThreeAndBelow" && h.hotelStar === "Three")) && (
          this.importantKey.length === 0 ||
          h.name.indexOf(this.importantKey) !== -1));
        console.log(hotels);
        this.resultHotel = hotels;
        // console.log(this.starFilters);
        return hotels;
      },

      bizRegionChange(value) {
        console.log(value);
        this.bizRegion = value;
      },

      sortOrderChange(e) {
        console.log(e);
        let order = e.key;
        this.resultHotel.sort((h1, h2) => {
          if (order === "grade") {
            console.log(h1.rate);
            return h2.rate - h1.rate;
          }
            // else if (order === ['price']){
            //     return h2.
          // }
          else if (order === ['hotelStarDescending']) {
            let s1 = this.starList.find((e) => e.value === h1.hotelStar);
            let s2 = this.starList.find((e) => e.value === h2.hotelStar);
            return s2.num - s1.num;
          } else if (order === ['hotelStarAscending']) {
            let s1 = this.starList.find((e) => e.value === h1.hotelStar);
            let s2 = this.starList.find((e) => e.value === h2.hotelStar);
            return s1.num - s2.num;
          }
        });
      },

      sortHotel(e) {
        console.log(e.target.value);
        let by = e.target.value;
        if (by === 'byRate') {
          let hotels = this.resultHotel.length > 0 ? this.resultHotel : this.hotelList;
          hotels.sort((h1, h2) => {
            return h2.rate - h1.rate;
          });
          this.resultHotel = hotels;
        } else if (by === 'byStar') {
          let hotels = this.resultHotel.length > 0 ? this.resultHotel : this.hotelList;
          hotels.sort((h1, h2) => {
            let s1 = this.starList.find((e) => e.value === h1.hotelStar);
            let s2 = this.starList.find((e) => e.value === h2.hotelStar);
            return s2.num - s1.num;
          });
          this.resultHotel = hotels;
        }
      }
    }
  }
</script>


<style scoped>

  .list-search-container {
    position: sticky;
    top: 0px;
    width: 98%;
    margin-top: 30px;
    background: #fff;
    box-shadow: 0 8px 20px 0 rgb(97 121 157 / 16%);
    box-sizing: border-box;
    padding: 24px;
    z-index: 100;
  }

  .inner-wrap {
    /*margin-top: 15px;*/
    margin-left: 4%;
  }

  .search-container {
    display: inline-flex;
  }

  .search-box {
    width: 100%;
    height: 100%;
    border: none;
    margin-right: 30px;
  }

  .search-box-header {
    margin-bottom: 5px;
  }

  .search-box-content {
    width: 240px;
  }

  /*.list-search-container .list-item {*/
  /*  display: inline-block;*/
  /*  height: 100%;*/
  /*  vertical-align: top;*/
  /*  box-sizing: border-box;*/
  /*  border: 1px solid #dadfe6;*/
  /*}*/
  /*.list-search-container .list-calendar {*/
  /*  width: 345px;*/
  /*}*/
  .star-rate-container {
    display: inline-flex;
    align-items: center;
  }

  .star-rate-container .rate {
    display: inline-flex;
    justify-content: center;
    align-items: center;
    background: rgb(240, 242, 245);
    border-radius: 1px;
    height: 22px;
    margin-right: 16px;
    padding: 4px 8px;
    font-size: 12px;
    color: rgb(15, 41, 77);
    cursor: pointer;
    border: 1px solid transparent;
  }


  .box-active {
    position: fixed;
    top: 0;
  }
</style>

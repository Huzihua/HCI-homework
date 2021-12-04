<template>
  <div>
  <div class="list-search-container">
    <div class="inner-wrap">

        <div class="search-container">

          <div class="search-box">
            <div class="search-box-header">目的地</div>
            <div class="search-box-content"><a-cascader :options="Dests"  v-model="dest" style="width: 100%" ></a-cascader></div>
          </div>
          <div class="search-box">
            <div class="search-box-header">入住时间</div>
            <div class="search-box-content"><a-range-picker ></a-range-picker></div>
          </div>
          <div class="search-box">
            <div class="search-box-header">星级</div>
            <div class="search-box-content"><a-cascader v-model="hotelStar" :options="Stars" style="width: 100%" ></a-cascader></div>
          </div>
          <div class="search-box">
            <div class="search-box-header">关键词(选填)</div>
            <div class="search-box-content"><a-input v-model="importantKey" placeholder="机场、火车站、酒店名称或区域" /></div>
          </div>
          <a-form-item >
          <a-button @click="searchHotel" type="primary" style="height: 60px"><a-icon type="search" style="font-size: 25px"></a-icon></a-button>
          </a-form-item>
        </div>
      <div>
      </div>
    </div>
  </div>
    <div><HotelCard></HotelCard></div>
  </div>
</template>


<script>
  import HotelCard from './components/hotelCard'
  import { mapGetters, mapActions, mapMutations } from 'vuex'
  const bizRegionList = [
  {
    name: '全部',
    key: 'All',
  },
  {
    name: '西单',
    key: 'XiDan',
  }]

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
  }]

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
  ]

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
  HotelCard,
},
  data(){
  return{
  emptyBox: [{ name: 'box1' }, { name: 'box2'}, {name: 'box3'}],
  dest:[],
  bizRegion: "",
  importantKey:"",
    hotelStar:"",
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
}
  this.set_hotelListParams(data)
  this.set_hotelListLoading(true)
  this.getHotelList()
},
  jumpToDetails(id){
  this.$router.push({ name: 'hotelDetail', params: { hotelId: id }})
},

  searchHotel(){
  // console.log(this.hotelList);
  // console.log(this.bizRegion);
  console.log(this.dest);
  this.search = true;
  let hotels = [];
    hotels = this.hotelList.filter((h) =>
        (h.bizRegion === this.bizRegion || this.bizRegion === 'All') &&
        this.starFilters.indexOf(h.hotelStar) !== -1);
  // console.log(hotels);
  this.resultHotel = hotels;
  // console.log(this.starFilters);
  return hotels;
},

  bizRegionChange(value){
  console.log(value);
  this.bizRegion = value;
},

  sortOrderChange(e){
  console.log(e);
  let order = e.key;
  this.resultHotel.sort((h1, h2) => {
  if (order === ["grade"]){
  return h2.rate - h1.rate;
}
  // else if (order === ['price']){
  //     return h2.
  // }
  else if (order === ['hotelStarDescending']){
  let s1 = this.starList.find((e) => e.value === h1.hotelStar);
  let s2 = this.starList.find((e) => e.value === h2.hotelStar);
  return s2.num - s1.num;
}
  else if (order === ['hotelStarAscending']){
  let s1 = this.starList.find((e) => e.value === h1.hotelStar);
  let s2 = this.starList.find((e) => e.value === h2.hotelStar);
  return s1.num - s2.num;
}
});

}
}
}
</script>

}


<style scoped>

.list-search-container {
  width: 100%;
  margin-top: 30px;
  background: #fff;
  box-shadow: 0 8px 20px 0 rgb(97 121 157 / 16%);
  box-sizing: border-box;
  padding: 24px;
}
.inner-wrap{
  margin-left: 5%;
}

.search-container{
  display: inline-flex;
}
.search-box {
  width: 100%;
  height: 100%;
  border: none;
  margin-right: 20px;
}
.search-box-header{
  margin-bottom: 5px;
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
/*.list-search-container .ul {*/
/*  max-width: 1160px;*/
/*}*/
/*.list-search-container .ul {*/
/*  margin: 0 auto;*/
/*  list-style: none;*/
/*  box-sizing: border-box;*/
/*}*/
/*.list-search-container .list-destination {*/
/*  width: 270px;*/
/*  border: none;*/
/*}*/
/*.list-search-container .list-item {*/
/*  height: 76px;*/
/*}*/
</style>
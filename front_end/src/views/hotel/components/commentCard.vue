<template>
  <div class="commentCard">
    <div style="border-bottom:1px solid #dddddd;"  v-for="(item,i) in comments" :key="i">
      <div class="userName">{{item.userName}}</div>
      <div class="context">{{item.context}}</div>
    </div>

  </div>
</template>

<script>
export default {
  name: "commentCard",
  props: {
    comments:{
      type: Array
    }
  },
  data() {
    return {
      pagination: {
        total: 0,
        pageSize: 8,
        showTotal: total => `共有${total}条评价`,
      },
      loading: false,
    };
  },
  mounted() {
    console.log(this.comments);
  },
}

</script>

<style scoped>
.userName{
  margin-top: 10px;
  vertical-align: top;
  padding-left: 30px;
  font-size: 17px;
  display: inline-block;
  width: 30%;
}
.context {
  display: inline-block;
  width:70%;
  margin: 10px 0 16px;
  font-size: 20px;
  color: #455873;
  letter-spacing: 0;
  line-height: 26px;
  word-break: break-word;
}
</style>
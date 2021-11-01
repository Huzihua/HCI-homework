<template>
    <div class="comment-list">
        <div class="list">
    <a-table
            :columns="columns"
            :data-source="comments"
            :pagination="pagination"
            :loading="loading"
            @change="pageChange"
            :scroll="{ y: 240 }"
    >
        <span slot="userName" slot-scope="text">
                    <span>{{ text }}</span>
                </span>
        <span slot="comment" slot-scope="text">
                    <span>{{ text }}</span>
                </span>
    </a-table>
        </div>
    </div>
</template>
<script>
    const columns = [
        {
            title: '用户名称',
            dataIndex: 'userName',
            key: 'userName',
            width: '20%',
            scopedSlots: { customRender: 'name' },
        },
        {
            title: '用户评价',
            dataIndex: 'context',
            key: 'comment',
            scopedSlots: { customRender: 'comment' },
        }

    ];

    export default {
        name: "commentList",
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
                columns: columns,
                // currentShow: this.comments.slice(0, this.pagination.pageSize),//当页显示数据
            };
        },
        mounted() {
           // this.fetch();
            console.log(this.comments);
            // this.updateShownData(0);
        },
        beforeRouteUpdate(to, from, next){
            next();
        },
        methods: {
            pageChange(pagination) {
                this.loading = true;
                console.log(pagination);
                this.pagination.current = pagination.current;
                this.pagination.pageSize = pagination.pageSize;
               // this.fetch();
               //  this.updateShownData(this.pagination.current);
                console.log(this.comments);
                this.loading = false;
            },
            updateShownData(currentPage){
                let size = this.pagination.pageSize;
                this.currentShow = this.comments.slice((currentPage - 1) * size, currentPage * size);
                console.log(this.currentShow);
            }
        }


    };
</script>


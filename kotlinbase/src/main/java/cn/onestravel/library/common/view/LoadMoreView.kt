package cn.onestravel.library.common.view

/**
 * @name LoadMoreView
 * @description 上拉加载的 footer view
 * @createTime 2018/11/29 17:52
 * @author onestravel
 * @version 1.0.0
 */
interface LoadMoreView {
    /**
     * 自定义适当的加载时机
     * @return true 自定义生效 false默认的加载时机
     */
    fun shouldLoadMore(recyclerView: LoadMoreRecyclerView):Boolean


    /**
     * 正在加载
     */
    fun onLoadMore(recyclerView: LoadMoreRecyclerView)

    /**
     * 加载完成
     * @param hasMore 是否还有更多数据
     */
    fun onComplete(recyclerView: LoadMoreRecyclerView, hasMore:Boolean)



    /**
     * 加载失败
     * @param errorCode 错误码，由用户定义
     */
    fun onError(recyclerView: LoadMoreRecyclerView, errorCode:Int)
}
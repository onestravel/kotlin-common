package cn.onestravel.library.kotlin.one.activity

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import cn.onestravel.library.kotlin.one.R
import cn.onestravel.library.kotlin.one.adapter.OneRecyclerAdapter
import cn.onestravel.library.kotlin.one.view.ClassicalFooter
import cn.onestravel.library.kotlin.one.view.LoadMoreRecyclerView
import kotlinx.android.synthetic.main.layout_base_list.*

/**
 * @name OneListActivity
 * @description 默认含有RecyclerView的可下拉刷新，上拉加载（可隐藏）Activity基类
 * @createTime 2018/11/28 18:02
 * @author onestravel
 * @version 1.0.0
 */
open class OneListActivity : OneActivity() {

    /**
     * 初始化布局ID
     */
    override fun getLayoutId(): Int {
        return R.layout.layout_base_list
    }

    /**
     * 初始化View 的相关操作
     */
    override fun initView() {
        super.initView()
        mRecyclerView.layoutManager = getLayoutManager()
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
        mRecyclerView.setLoadMoreView(ClassicalFooter(this))
    }

    /**
     * 初始化View的相关事件
     */
    override fun initListener() {
        super.initListener()
        mRefreshLayout.setOnRefreshListener {
            refreshDatas()
        }
        mRecyclerView.setLoadMoreListener {
            loadMoreDatas()
        }
    }


    protected fun getRecyclerView():LoadMoreRecyclerView{
        return mRecyclerView
    }

    /**
     * 获取recyclerView的布局管理器，子类可重写该方法，来更改布局管理器
     */
    protected open fun getLayoutManager(): androidx.recyclerview.widget.RecyclerView.LayoutManager {
        val layoutManager: androidx.recyclerview.widget.LinearLayoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this)
        //设置为垂直布局，这也是默认的
        layoutManager.orientation = androidx.recyclerview.widget.OrientationHelper.VERTICAL
        return layoutManager
    }


    /**
     * 设置RecyclerView 的Adapter适配器
     * @param adapter 设置的Adapter，必须是BaseRecyclerAdapter的子类
     */
    protected fun <T> setAdapter(adapter: OneRecyclerAdapter<T>) {
        mRecyclerView.adapter = adapter
    }

    /**
     * 设置刷新控件的颜色
     */
    protected fun setRefreshColorSchemeColors(@ColorInt vararg colors: Int) {
        mRefreshLayout.setColorSchemeColors(*colors)
    }
    /**
     * 设置刷新控件的颜色
     */
    protected fun setColorSchemeResources(@ColorRes vararg colorIds: Int) {
        mRefreshLayout.setColorSchemeResources(*colorIds)
    }

    /**
     * 设置刷新控件是否显示
     */
    protected fun isRefresh(isRefresh: Boolean) {
        mRefreshLayout.isRefreshing = false
    }

    /**
     * 刷新完成
     */
    protected fun refreshComplete() {
        mRefreshLayout.isRefreshing = false
    }

    /**
     * 刷新数据，子类实现该方法，进行数据请求
     */
    protected open fun refreshDatas() {

    }

    /**
     * 加载更多完成
     */
    protected fun loadMoreComplete() {
        mRecyclerView.loadMoreComplete(true)
    }

    /**
     * 设置是否可以加载更多
     */
    protected fun canLoadMore(canLoadMore: Boolean) {
        mRecyclerView.loadMoreEnable = canLoadMore
    }

    /**
     * 设置是否还有更多的数据
     */
    protected fun hasLoadMore(hasLoadMore: Boolean) {
        mRecyclerView.hasMore = hasLoadMore
    }

    /**
     * 加载新数据，子类实现该方法，进行数据请求
     */
    protected open fun loadMoreDatas() {

    }

}
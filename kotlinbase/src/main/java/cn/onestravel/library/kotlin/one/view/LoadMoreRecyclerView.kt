package cn.onestravel.library.kotlin.one.view

import android.content.Context
import androidx.collection.SparseArrayCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * @name LoadMoreRecyclerView
 * @description 可以实现上拉加载，下拉刷新的 RecyclerView
 * @createTime 2018/11/29 17:53
 * @author onestravel
 * @version 1.0.0
 */
class LoadMoreRecyclerView(context: Context, attrs: AttributeSet?, defStyle: Int) : androidx.recyclerview.widget.RecyclerView(context, attrs, defStyle) {
    private val ITEM_TYPE_HEADER_INIT = 100000
    private val ITEM_TYPE_FOOTER_INIT = 200000
    private val ITEM_TYPE_LOADMORE = 300000
    var hasMore: Boolean = false
        set(value) {
            field = value
            if (value) showMore = true
        }
    var showMore:Boolean = false
    private var mLoading: Boolean = false
    var loadMoreEnable: Boolean = true
    private var mWrapAdapter: WrapAdapter = WrapAdapter()
    private var mInnerAdapter: Adapter<ViewHolder>? = null
    private var mLoadMoreView: LoadMoreView? = null
    private val mHeaderViews = SparseArrayCompat<View>()
    private val mFooterViews = SparseArrayCompat<View>()
    private var mLoadMoreListener: ((androidx.recyclerview.widget.RecyclerView) -> Unit)? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        if (mInnerAdapter!=null){
            mInnerAdapter?.unregisterAdapterDataObserver(mDataObserver)
        }
        mInnerAdapter = adapter
        mInnerAdapter?.registerAdapterDataObserver(mDataObserver)
        super.setAdapter(mWrapAdapter)
    }

    /**
     * 添加HeaderView
     */
    fun addHeaderView(view: View) {
        mHeaderViews.put(mHeaderViews.size() + ITEM_TYPE_HEADER_INIT, view)
        mWrapAdapter.notifyDataSetChanged()
    }

    /**
     * 添加FooterView
     */
    fun addFooterView(view: View) {
        mFooterViews.put(mFooterViews.size() + ITEM_TYPE_FOOTER_INIT, view)
        mWrapAdapter.notifyDataSetChanged()
    }


    /**
     * 设置LoadMoreView
     * 必须是一个视图View
     */
    fun setLoadMoreView(loadMoreView: LoadMoreView) {
        if (loadMoreView !is View) {
            throw IllegalStateException("LoadMoreView must is a View?")
        }
        this.mLoadMoreView = loadMoreView
        mWrapAdapter.notifyDataSetChanged()
        removeOnScrollListener(defaultScrollListener)
        if (!(mLoadMoreView?.shouldLoadMore(this) ?: true)) {
            addOnScrollListener(defaultScrollListener)
        }
    }



    /**
     * 开始加载更多
     */
    fun startLoadMore() {
        if (!mLoading && loadMoreEnable && hasMore) {
            mLoading = true
            mLoadMoreView?.onLoadMore(this)
            mLoadMoreListener?.invoke(this)
        }
    }



    /**
     * 加载完成
     */
    fun loadMoreComplete(hasMore: Boolean) {
        mLoading = false
        mLoadMoreView?.onComplete(this, hasMore)
        this.hasMore = hasMore
    }



    fun loadMoreError(errorCode: Int) {
        mLoading = false
        mLoadMoreView?.onError(this, errorCode)
    }



    /**
     * 设置加载更多监听
     */

    fun setLoadMoreListener(loadMoreListener: (recyclerView: androidx.recyclerview.widget.RecyclerView) -> Unit): Unit {
        mLoadMoreListener = loadMoreListener
    }



    /**
     * 默认的加载触发时机
     */
    private val defaultScrollListener = object : OnScrollListener() {
        override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
            if (!recyclerView.canScrollVertically(1)) {
                startLoadMore()
            }
        }
    }


    private inner class WrapAdapter : Adapter<ViewHolder>() {
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (!isContent(position)) {
                return
            }
            mInnerAdapter?.onBindViewHolder(holder, position - mHeaderViews.size())
        }



        override fun getItemCount(): Int {
            val adapterCount = mInnerAdapter?.itemCount ?: 0
            if (adapterCount > 0) {
                return adapterCount + mHeaderViews.size() + mFooterViews.size() + if (mLoadMoreView == null||!showMore) 0 else 1
            } else {//防止没有内容的时候加载更多显示出来
                return mHeaderViews.size() + mFooterViews.size()
            }
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            if (mHeaderViews[viewType] != null) {
                return object : ViewHolder(mHeaderViews[viewType]!!) {
                    init {
                        itemView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    }
                }
            }
            if (viewType == ITEM_TYPE_LOADMORE)
                return object : ViewHolder(mLoadMoreView as View) {
                    init {
                        itemView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    }
                }
            if (mFooterViews[viewType] != null) {
                return object : ViewHolder(mFooterViews[viewType]!!) {
                    init {
                        itemView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    }
                }
            }
            return mInnerAdapter?.onCreateViewHolder(parent, viewType)!!
        }


        override fun getItemViewType(position: Int): Int {
            if (position < mHeaderViews.size()) {
                return mHeaderViews.keyAt(position)
            }
            if (mLoadMoreView != null && position == itemCount - 1&&showMore) {
                return ITEM_TYPE_LOADMORE
            }
            if (position >= mHeaderViews.size() + (mInnerAdapter?.itemCount ?: 0)) {
                return mFooterViews.keyAt(position - mHeaderViews.size() - (mInnerAdapter?.itemCount ?: 0))
            }
            return mInnerAdapter?.getItemViewType(position - mHeaderViews.size()) ?: -1
        }

        override fun onAttachedToRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {
            mInnerAdapter?.onAttachedToRecyclerView(recyclerView)
            val layoutManager = layoutManager
            if (layoutManager is androidx.recyclerview.widget.GridLayoutManager) {
                layoutManager.spanSizeLookup = object : androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        if (!isContent(position)) {
                            return layoutManager.spanCount
                        }
                        return 1
                    }
                }
            }
        }

        override fun onViewAttachedToWindow(holder: ViewHolder) {
            mInnerAdapter?.onViewAttachedToWindow(holder)
            val position = holder.layoutPosition
            val layoutParams = holder.itemView.layoutParams
            if (!isContent(position) && layoutParams != null && layoutParams is androidx.recyclerview.widget.StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        }

        fun isContent(position: Int): Boolean {
            if (position < mHeaderViews.size())
                return false
            if (mLoadMoreView != null && position == itemCount - 1)
                return false
            if (position >= mHeaderViews.size() + (mInnerAdapter?.itemCount ?: 0))
                return false
            return true
        }
    }

    private val mDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            mWrapAdapter.notifyDataSetChanged()
        }
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount)
        }
    }
}
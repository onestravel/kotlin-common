# 基于Kotlin语言封装依赖库

## 前言

该Library是基于Kotlin语言封装的一个库，主要是用来创建一个kotlin项目时，作为项目的简单封装，使用该Library作为依赖引入，即可进行项目开发，省去一些前期准备工作。

该Library主要进行了一下封装：

1. 采用普通MVC架构的方式，对Activity，Fragment,Adapter和一些工具类的封装以及自定义View的实现；
2. 采用Retrofit+RxJava 对http请求进行相应的封装，可设置公共参数，并对RX生命周期进行处理；
3. 采用MVP架构，对上面封装的Activity，Fragment 进行二次封装；
4. 该项目使用了org.jetbrains.anko ，可实现对布局文件的自动依赖注入，kotlin文件中的View名称就是对应布局文件中View的 id;

## 一、采用普通方式封装的基础文件介绍

### 1、BaseActivity

package:cn.onestravel.library.kotlin.base.activity

所有Activity的基类，定义一些子类必须实现的和可实现的方法：

```kotlin
 /**
     * 获取布局ID,子类必须实现
     */
    protected abstract fun getLayoutId(): Int


    /**
     * 初始化 View 的相关操作，若有需要可在子类实现
     */
    protected open fun initView() {}

    /**
     * 初始化 Listener 事件的相关操作，若有需要可在子类实现
     */
    protected open fun initListener() {}


    /**
     * 初始化 Data 数据的相关操作，若有需要可在子类实现
     */
    protected open fun initData() {}


    /**
     * 在主线程弹出Toast 提示
     * @param msg 需要弹出的提示信息
     */
    protected open fun showToast(msg:String){
        runOnUiThread {
            toast(msg)
        }
    }

    /**
     * 在主线程弹出Toast 提示
     * @param stringRes 需要弹出的提示信息的string资源ID
     */
    protected open fun showToast(stringRes:Int){
        runOnUiThread {
            toast(getString(stringRes))
        }
    }

    /**
     * 跳转到另一个Activity，并且finish 掉当前Activity
     * 需要跳转的Activity必须继承于BaseActivity 或者
     * @param params 可变参数，需要通过intent传递的参数 eg:"key" to "value"
     */
    inline fun <reified T: BaseActivity> startActivityAndFinish(vararg params: Pair<String, Any?>) {
        startActivity<T>(*params)
        finish()
    }
```

### 2、BaseListActivity

package:cn.onestravel.library.kotlin.base.activity

继承自BaseActivity，实现针对RecyclerView实现列表数据的一些配置，简化实现只有一个列表布局的Activity，并且实现上拉加载，下拉刷新操作。

主要方法如下：

```kotlin
 protected fun getRecyclerView():LoadMoreRecyclerView{
        return mRecyclerView
    }

    /**
     * 获取recyclerView的布局管理器，子类可重写该方法，来更改布局管理器
     */
    protected open fun getLayoutManager(): RecyclerView.LayoutManager {
        val layoutManager: LinearLayoutManager = LinearLayoutManager(this)
        //设置为垂直布局，这也是默认的
        layoutManager.orientation = OrientationHelper.VERTICAL
        return layoutManager
    }


    /**
     * 设置RecyclerView 的Adapter适配器
     * @param adapter 设置的Adapter，必须是BaseRecyclerAdapter的子类
     */
    protected fun <T> setAdapter(adapter: BaseRecyclerAdapter<T>) {
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
```



### 3、BaseRecyclerAdapter

package:cn.onestravel.library.kotlin.base.adapter

针对RecyclerView使用的RecyclerView.Adapter进行封装，实现一些基础的布局（不常变化的）设置的方法，定义一些方法，简化子类的实现，更加有利于缩短开发时间；实现adapter中提示Toast的方法，以及添加、设置数据，或者基础View的设置相关内容的方法；对Item点击和长按的监听事件。

主要方法如下：

```kotlin

abstract class BaseRecyclerAdapter<T> : RecyclerView.Adapter<VH>(), View.OnClickListener, View.OnLongClickListener {
    private val mDatas: MutableList<T> = ArrayList<T>()
    private lateinit var context: Context
    private var clickListener: OnItemClickListener<T>? = null
    private var longClickListener: OnItemLongClickListener<T>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        context = parent.context
        val convertView = LayoutInflater.from(parent.context).inflate(getLayoutId(viewType), parent, false)
        convertView.setOnClickListener(this)
        convertView.setOnLongClickListener(this)
        return VH(convertView)
    }

    /**
     * 设置Item 的点击事件
     */
    fun setOnItemClick(listener: OnItemClickListener<T>) {
        clickListener = listener
    }

    /**
     * 设置Item 的点击事件
     */
    fun setOnItemClick(onClick: (view:View,position:Int,data:T) -> Unit) {
        clickListener = object :  OnItemClickListener<T> {
            override fun onItemClick(view: View, position: Int, data: T) {
                onClick(view,position,data)
            }
        }
    }

    /**
     * 设置Item 的长按事件
     */
    fun setOnItemLongClick(listener:OnItemLongClickListener<T>) {
        longClickListener = listener
    }

    /**
     * 设置Item 的长按事件
     */
    fun setOnItemLongClick(onLongClick: (view:View,position:Int,data:T) -> Boolean) {
        longClickListener = object : OnItemLongClickListener<T> {
            override fun onItemLongClick(view: View, position: Int, data: T):Boolean {
                return onLongClick(view,position,data)
            }
        }
    }

    override fun onClick(p0: View?) {
        val position:Int = p0?.getTag(R.id.itemViewPosition) as Int
        val data:T = mDatas.get(position)
        clickListener?.onItemClick(p0,position,data)
    }

    override fun onLongClick(p0: View?): Boolean {
        val position:Int = p0?.getTag(R.id.itemViewPosition) as Int
        val data:T = mDatas.get(position)
        return longClickListener?.onItemLongClick(p0,position,data) ?: true
    }

    /**
     * 设置数据,并且会清空原数据列表
     * @param datas 新的数据列表
     */
    open fun setDatas(datas: List<T>) {
        mDatas.clear()
        mDatas.addAll(datas)
        notifyDataSetChanged()
    }

    /**
     * 添加新的数据列表到原数据列表末尾
     * @param datas 新的数据列表
     */
    open fun addDatas(datas: List<T>) {
        mDatas.addAll(datas)
        notifyDataSetChanged()
    }

    /**
     * 添加新的数据列表到原数据列表的指定位置
     * @param position 需要添加的指定位置
     * @param datas 新的数据列表
     */
    open fun addDatas(position: Int, datas: List<T>) {
        val pos = if (position > mDatas.size) {
            mDatas.size
        } else position
        mDatas.addAll(pos, datas)
        notifyDataSetChanged()
    }

    /**
     * 更新数据列表指定位置上的数据
     * @param position 需要更新数据的位置
     * @param data 更新后的数据
     */
    open fun updateData(position: Int, data: T) {
        if (position < mDatas.size) {
            mDatas.removeAt(position)
            mDatas.add(position, data)
            notifyItemChanged(position)
        }
    }

    /**
     * 添加新的数据到原数据列表末尾
     * @param datas 新的数据
     */
    open fun addData(data: T) {
        mDatas.add(data)
        notifyItemInserted(mDatas.size - 1)
    }

    /**
     * 添加新的数据到原数据列表的指定位置
     * @param position 需要添加的指定位置
     * @param data 新的数据
     */
    open fun addData(position: Int, data: T) {
        val pos = if (position > mDatas.size) {
            mDatas.size
        } else position
        mDatas.add(pos, data)
        notifyItemInserted(pos)
    }

    /**
     * 移除指定位置上的数据
     * @param position 需要添加的指定位置
     */
    open fun removeDataAt(position: Int) {
        if (position < mDatas.size) {
            mDatas.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    protected open fun showToast(msg: String) {
        context.runOnUiThread {
            context.toast(msg)
        }
    }

    /**
     * 移除指定的数据
     * @param data 需要移除的数据实体
     */
    open fun removeData(data: T) {
        if (mDatas.contains(data)) {
            val position = mDatas.indexOf(data)
            mDatas.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * 根据View的类型获取对应的Item布局的ID
     */
    @LayoutRes
    abstract fun getLayoutId(viewType: Int): Int


    /**
     * 绑定ViewHolder 时执行的方法，在此方法里处理对Item的view的操作
     */
    abstract fun onBindVH(holder: VH, position: Int, datas: T)

    /**
     * 返回数据的数量
     */
    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setTag(R.id.itemViewPosition, position)
        onBindVH(holder, position, mDatas.get(position))
    }


}

/**
 *Item 的点击事件
 */
interface OnItemClickListener<T> {
    fun onItemClick(view: View, position: Int, data: T)
}

/**
 *Item 的长按事件
 */
interface OnItemLongClickListener<T> {
    fun onItemLongClick(view: View, position: Int, data: T):Boolean
}

/**
 * 所有的Adapter 使用的ViewHolder
 */
class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {


    /**
     * 根据View的id获取对应的View
     */
    inline fun <reified E : View> getView(@IdRes viewId: Int): E {
        return itemView.find<E>(viewId)
    }

    /**
     * 对TextView及其子类设置文本内容
     * @param viewId 对应的View 的id
     * @param value 需要设置的文本内容
     */
    inline fun setText(@IdRes viewId: Int, value: String) {
        val view: View = this.getView(viewId)
        if (view is TextView) {
            val tv: TextView = view
            tv.text = value
        }
    }

    /**
     * 对TextView及其子类设置文本内容
     * @param viewId 对应的View 的id
     * @param value 需要设置的文本内容
     */
    inline fun setText(@IdRes viewId: Int, value: Spannable) {
        val view: View = this.getView(viewId)
        if (view is TextView) {
            val tv: TextView = view
            tv.text = value
        }
    }

    /**
     * 对TextView及其子类设置文本内容
     * @param viewId 对应的View 的id
     * @param stringRes 需要设置的文本资源的id
     */
    inline fun setText(@IdRes viewId: Int, @StringRes stringRes: Int) {
        val view: View = this.getView(viewId)
        if (view is TextView) {
            val tv: TextView = view
            tv.setText(stringRes)
        }
    }

    /**
     * 对ImageView及其子类设置图片
     * @param viewId 对应的View 的id
     * @param resId 需要设置的图片资源的id
     */
    inline fun setImageResource(@IdRes viewId: Int, @DrawableRes resId: Int) {
        val view: View = getView(viewId)
        if (view is ImageView) {
            val iv: ImageView = view
            iv.imageResource = resId
        }
    }

    /**
     * 对ImageView及其子类设置图片
     * @param viewId 对应的View 的id
     * @param imgUrl 需要设置的网络图片的地址
     */
    inline fun setImageUrl(@IdRes viewId: Int, imgUrl: String) {
        val view: View = getView(viewId)
        if (view is ImageView) {
            val iv: ImageView = view
            iv.loadImage(imgUrl)
        }
    }

    /**
     * 对View及其子类设置背景图片
     * @param viewId 对应的View 的id
     * @param resId 需要设置的背景图片的资源地址
     */
    inline fun setBackgroundResource(@IdRes viewId: Int, @DrawableRes resId: Int) {
        val view: View = getView(viewId)
        view.setBackgroundResource(resId)
    }

    /**
     * 对View及其子类设置背景颜色
     * @param viewId 对应的View 的id
     * @param color 需要设置的背景颜色
     */
    inline fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int) {
        val view: View = getView(viewId)
        view.setBackgroundColor(color)
    }


    /**
     * 对View及其子类设置点击事件
     * @param viewId 对应的View 的id
     * @param color 需要设置的背景颜色
     */
    inline fun setOnClick(@IdRes viewId: Int, crossinline onClick: (View) -> Unit) {
        val view: View = getView(viewId)
        view.setOnClickListener {
            onClick.invoke(it)
        }
    }

}
```


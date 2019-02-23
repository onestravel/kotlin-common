# Kotlin语言基于Mvp+Retrofit+RxJava封装的Android项目的基础依赖库

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

### 4、ImageViewExtend

package: cn.onestravel.library.kotlin.base.extend

改文件是ImageView的扩展方法的文件，主要针对ImageView加载网络图片而新增的一些扩展方法

具体方法如下：

```kotlin
inline fun ImageView.loadImage(imgUrl: String): ImageView {
    ImageUtils.loadImageUrl(this.context, imgUrl, this)
    return this
}

inline fun ImageView.loadBitmap(imgUrl: String): ImageView {
    ImageUtils.loadImageBitmap(this.context, imgUrl, this)
    return this
}

inline fun ImageView.loadGif(imgUrl: String): ImageView {
    ImageUtils.loadImageGif(this.context, imgUrl, this)
    return this
}

inline fun ImageView.loadImgCallBack(imgUrl: String, requestListener: RequestListener<Bitmap>): ImageView {
    ImageUtils.loadImageCallBack(this.context, imgUrl, this, requestListener)
    return this
}

```

### 5、BaseFragment

package: cn.onestravel.library.kotlin.base.fragment

所有Fragment的基类，定义一些子类必须实现的和可实现的方法，具体方法与BaseActivity方法类似。



### 6、BaseListFragment

package: cn.onestravel.library.kotlin.base.fragment

继承自BaseFragment，实现针对RecyclerView实现列表数据的一些配置，简化实现只有一个列表布局的Fragment，并且实现上拉加载，下拉刷新操作；具体方法与BaseListActivity相同。



### 7、DensityUtil

package: cn.onestravel.library.kotlin.base.utils

尺寸转换工具类，可以将dip转为px，将px转为dip；获取屏幕宽度和高度。

```kotlin
	/**
     * dip转换为px
     */
    fun dip2px(dp: Int): Int {
        return (dp * density).toInt()
    }


    /**
     * px转换为dip
     */
    fun px2dip(px:  Float): Int {
        return (px / density).toInt()
    }

    /**
     * 获取屏幕宽度
     */
    fun appWidth(): Int {
        return Resources.getSystem().getDisplayMetrics().widthPixels
    }

    /**
     * 获取屏幕高度
     */
    fun appHeight(): Int {
        return Resources.getSystem().getDisplayMetrics().heightPixels
    }
```

### 8、CircleTextView

package：cn.onestravel.library.kotlin.base.view

自定义圆形TextView，一满足不同需求

### 9、EmptyView

package：cn.onestravel.library.kotlin.base.view

自定义 数据为空的页面视图，主要有三个功能类型，分别为：数据为空显示页面（TYPE_EMPTY）；加载进度提示页面（TYPE_LOADING）；加载失败可点击重试页面（TYPE_RELOAD）

重新定义的一下方法如下：

```kotlin
/**
     * 设置页面显示类型
     *
     * @param type
     */
    fun setEmptyType(type: Int) {
        _setEmptyType(type, true)
    }

    /**
     * 设置页面显示类型
     *
     * @param type
     */
    private fun _setEmptyType(type: Int, isShow: Boolean) {
        this.emptyType = type
        when (type) {
            TYPE_EMPTY -> {
                layoutLoading!!.visibility = View.GONE
                layoutReload!!.visibility = View.GONE
                layoutEmpty!!.visibility = View.VISIBLE
                btnReload!!.visibility = View.GONE
                ivEmptyImg!!.visibility = View.VISIBLE
            }
            TYPE_LOADING -> {
                layoutLoading!!.visibility = View.VISIBLE
                layoutEmpty!!.visibility = View.GONE
                layoutReload!!.visibility = View.GONE
                if (tvLoadingMsg != null) {
                    tvLoadingMsg!!.text = "正在加载..."
                }
            }
            TYPE_RELOAD -> {
                layoutLoading!!.visibility = View.GONE
                layoutEmpty!!.visibility = View.GONE
                layoutReload!!.visibility = View.VISIBLE
                btnReload!!.visibility = View.VISIBLE
                ivEmptyImg!!.visibility = View.VISIBLE
            }
        }
        if (isShow) {
            show()
        }
    }

    /**
     * 设置空数据界面的背景色
     *
     * @param color 背景色
     */
    fun setEmptyBackgroundColor(color: Int) {
        layoutEmpty!!.setBackgroundColor(color)
    }

    /**
     * 设置错误描述
     *
     * @param resId
     */
    fun setEmptyText(@StringRes resId: Int) {
        if (tvEmptyMsg != null) {
            tvEmptyMsg!!.text = mContext!!.getString(resId)
        }
    }

    /**
     * 设置错误描述
     *
     * @param text
     */
    fun setEmptyText(text: CharSequence?) {
        var text = text
        if (text == null) {
            text = ""
        }
        if (tvEmptyMsg != null) {
            tvEmptyMsg!!.text = text
        }
    }


    /**
     * 设置错误描述字体大小
     *
     * @param size
     */
    fun setEmptyTextSize(size: Float) {
        if (tvEmptyMsg != null) {
            tvEmptyMsg!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        }
    }

    /**
     * 设置错误描述颜色
     *
     * @param color
     */
    fun setEmptyTextColor(color: Int) {
        if (tvEmptyMsg != null) {
            tvEmptyMsg!!.setTextColor(color)
        }
    }

    /**
     * 设置错误描述颜色
     *
     * @param colors
     */
    fun setEmptyTextColor(colors: ColorStateList) {
        if (tvEmptyMsg != null) {
            tvEmptyMsg!!.setTextColor(colors)
        }
    }

    /**
     * 设置页面图片
     *
     * @param resId
     */
    fun setEmptyImgResource(@DrawableRes resId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.noDataImg = mContext!!.getDrawable(resId)
        } else {
            this.noDataImg = mContext!!.resources.getDrawable(resId)
        }
        if (ivEmptyImg != null) {
            ivEmptyImg!!.setImageResource(resId)
        }
    }


    /**
     * 设置页面图片
     *
     * @param noDataImg
     */
    fun setEmptyImg(noDataImg: Drawable) {
        this.noDataImg = noDataImg
        if (ivEmptyImg != null) {
            ivEmptyImg!!.setImageDrawable(noDataImg)
        }
    }

    /**
     * 获取加载按钮文字
     */
    fun getReloadBtnText(): String? {
        return reloadBtnText
    }

    /**
     * 设置重新加载按钮文字
     *
     * @param resId
     */
    fun setReloadBtnText(resId: Int) {
        this.reloadBtnText = mContext!!.getString(resId)
        if (btnReload != null) {
            btnReload!!.text = this.reloadBtnText
        }
    }

    /**
     * 设置重新加载按钮文字
     *
     * @param reloadBtnText
     */
    fun setReloadBtnText(reloadBtnText: String) {
        this.reloadBtnText = reloadBtnText
        if (btnReload != null) {
            btnReload!!.text = this.reloadBtnText
        }
    }


    /**
     * 设置重新加载按钮文字大小
     *
     * @param reloadBtnTextSize
     */
    fun setReloadBtnTextSize(reloadBtnTextSize: Float) {
        this.reloadBtnTextSize = reloadBtnTextSize
        if (btnReload != null) {
            btnReload!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.reloadBtnTextSize)
        }
    }


    /**
     * 设置重新加载按钮文字颜色
     *
     * @param color
     */
    fun setReloadBtnTextColor(color: Int) {
        this.reloadBtnTextColor = color
        if (btnReload != null) {
            btnReload!!.setTextColor(this.reloadBtnTextColor)
        }
    }

    /**
     * 设置重新加载按钮文字颜色
     *
     * @param colors
     */
    fun setReloadBtnTextColor(colors: ColorStateList) {
        if (btnReload != null) {
            btnReload!!.setTextColor(colors)
        }
    }


    /**
     * 设置加载按钮背景
     *
     * @param reloadBackground
     */
    fun setReloadBtnBackground(reloadBackground: Drawable) {
        this.reloadBtnBackground = reloadBackground
        if (btnReload != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btnReload!!.background = this.reloadBtnBackground
            } else {
                btnReload!!.setBackgroundDrawable(this.reloadBtnBackground)
            }
        }
    }

    /**
     * 设置加载按钮背景
     *
     * @param color
     */
    fun setReloadBtnBackgroundColor(color: Int) {
        this.reloadBtnBackground = ColorDrawable(color)
        if (btnReload != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btnReload!!.background = this.reloadBtnBackground
            } else {
                btnReload!!.setBackgroundDrawable(this.reloadBtnBackground)
            }
        }
    }

    /**
     * 设置加载按钮背景
     *
     * @param resId
     */
    fun setReloadBtnBackgroundResource(resId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.reloadBtnBackground = mContext!!.getDrawable(resId)
        } else {
            this.reloadBtnBackground = mContext!!.resources.getDrawable(resId)
        }
        if (btnReload != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btnReload!!.background = this.reloadBtnBackground
            } else {
                btnReload!!.setBackgroundDrawable(this.reloadBtnBackground)
            }
        }
    }

    /**
     * 获取加载提示文字
     */
    fun getReloadText(): String? {
        return reloadText
    }

    /**
     * 设置重新加载提示文字
     *
     * @param resId
     */
    fun setReloadText(resId: Int) {
        this.reloadText = mContext!!.getString(resId)
        if (tvReloadMsg != null) {
            tvReloadMsg!!.text = this.reloadText
        }
    }

    /**
     * 设置重新加载提示文字
     *
     * @param reloadText
     */
    fun setReloadText(reloadText: String) {
        this.reloadText = reloadText
        if (tvReloadMsg != null) {
            tvReloadMsg!!.text = this.reloadText
        }
    }


    /**
     * 设置重新加载提示文字大小
     *
     * @param reloadTextSize
     */
    fun setReloadTextSize(reloadTextSize: Float) {
        this.reloadTextSize = reloadTextSize
        if (tvReloadMsg != null) {
            tvReloadMsg!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.reloadTextSize)
        }
    }


    /**
     * 设置重新加载提示文字颜色
     *
     * @param color
     */
    fun setReloadTextColor(color: Int) {
        this.reloadTextColor = color
        if (tvReloadMsg != null) {
            tvReloadMsg!!.setTextColor(this.reloadTextColor)
        }
    }

    /**
     * 设置重新加载提示文字颜色
     *
     * @param colors
     */
    fun setReloadTextColor(colors: ColorStateList) {
        if (tvReloadMsg != null) {
            tvReloadMsg!!.setTextColor(colors)
        }
    }

    /**
     * 设置页面图片
     *
     * @param resId
     */
    fun setReloadImgResource(@DrawableRes resId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.reloadImage = mContext!!.getDrawable(resId)
        } else {
            this.reloadImage = mContext!!.resources.getDrawable(resId)
        }
        if (ivReloadImg != null) {
            ivReloadImg!!.setImageResource(resId)
        }
    }


    /**
     * 设置页面图片
     *
     * @param reloadImage
     */
    fun setReloadImg(reloadImage: Drawable) {
        this.reloadImage = reloadImage
        if (ivReloadImg != null) {
            ivReloadImg!!.setImageDrawable(reloadImage)
        }
    }


    /**
     * 设置加载提示文字
     *
     * @param resId
     */
    fun setLoadingText(resId: Int) {
        this.loadingText = mContext!!.getString(resId)
        if (tvLoadingMsg != null) {
            tvLoadingMsg!!.text = this.loadingText
        }
    }

    /**
     * 设置加载提示文字
     *
     * @param loadingText
     */
    fun setLoadingText(loadingText: String) {
        this.loadingText = loadingText
        if (tvLoadingMsg != null) {
            tvLoadingMsg!!.text = this.loadingText
        }
    }

    /**
     * 显示此视图
     */
    fun show() {
        this.visibility = View.VISIBLE
    }

    /**
     * 隐藏此视图
     */
    fun hide() {
        this.visibility = View.GONE
    }

    /**
     * 设置重新加载监听事件
     *
     * @param listener
     */
    fun setOnReloadListener(listener: OnReloadListener) {
        this.onReloadListener = listener
    }


    interface OnReloadListener {
        fun onReload()
    }

    companion object {
        const val TYPE_EMPTY = 0 // 页面为空，没有数据时展示
        const val TYPE_LOADING = 1 // 正在加载视图
        const val TYPE_RELOAD = 2 // 加载失败，显示重新加载视图
    }
```



### 10、ShapeImageView

package：cn.onestravel.library.kotlin.base.view

自定义矩形和圆形ImageView，实现带边框的圆角矩形ImageView或圆形ImageVIew

viewShape的值为：

| 值           | 说明          |
| ------------ | ------------- |
| SHAPE_REC    | 矩形ImageView |
| SHAPE_CIRCLE | 圆形ImageView |
| SHAPE_OVAL   | 椭圆ImageView |



```kotlin
/**
 * 设置ImageView的形式，圆角矩形，圆形，或椭圆
 **/
var shape = SHAPE_REC: Int
        get() = shape
        set(shape) {
            this.shape = shape
            invalidate()
        }
/**
 * 设置ImageView边框颜色
 **/
 var borderColor: Int
        get() = mBorderColor
        set(mBorderColor) {
            this.mBorderColor = mBorderColor
            mBorderPaint.color = mBorderColor
            invalidate()
        }
	/**
 	 * 设置ImageView的圆角
 	 **/
    var roundRadius: Float
        get() = mRoundRadius
        set(mRoundRadius) {
            this.mRoundRadius = mRoundRadius
            invalidate()
        }
	/**
 	 * 设置ImageView的边框宽度
 	 **/
    fun setBorderSize(mBorderSize: Int) {
        this.borderSize = mBorderSize.toFloat()
        mBorderPaint.strokeWidth = mBorderSize.toFloat()
        initRect()
        invalidate()
    }
	/**
 	 * 设置ImageView的图片资源
 	 **/
override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        mBitmap = ImageUtils.getBitmapFromDrawable(drawable)
        setupBitmapShader()
    }
	/**
 	 * 设置ImageView的Drawable
 	 **/		
    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        mBitmap = ImageUtils.getBitmapFromDrawable(drawable)
        setupBitmapShader()
    }

    
```



## 二、采用RxJava+Retrofit架构封装的关于Http请求以及生命周期处理的文件介绍

### 1、BaseRxActivity

package：cn.onestravel.library.kotlin.rxrequest.activity

继承自BaseActivity ，针对RxJava处理了相对应的生命周期，同时拥有BaseActivity的所有特性。

生命周期处理：

```kotlin
abstract class BaseRxActivity : BaseActivity(), LifecycleProvider<ActivityEvent> {
    private val lifecycleSubject:BehaviorSubject<ActivityEvent> = BehaviorSubject.create()

    @CheckResult
    override fun lifecycle(): Observable<ActivityEvent> {
        return lifecycleSubject.hide()
    }

    @CheckResult
    override fun <T> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
    }

    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(ActivityEvent.CREATE)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(ActivityEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(ActivityEvent.RESUME)
    }

    @CallSuper
    override fun onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP)
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY)
        super.onDestroy()
    }
}
```

### 2、BaseRxListActivity

package：cn.onestravel.library.kotlin.rxrequest.activity

继承自BaseListActivity ，针对RxJava处理了相对应的生命周期，同时拥有BaseListActivity下拉刷新，上拉加载的所有特性。

### 3、BaseRxFragment

package：cn.onestravel.library.kotlin.rxrequest.fragment

继承自BaseFragment ，针对RxJava处理了相对应的生命周期，同时拥有BaseFragment的所有特性。

生命周期处理：

```kotlin
abstract class BaseRxFragment : BaseFragment(), LifecycleProvider<FragmentEvent> {
    private val lifecycleSubject:BehaviorSubject<FragmentEvent> = BehaviorSubject.create()

    override fun lifecycle(): Observable<FragmentEvent> {
        return lifecycleSubject.hide()
    }

    @CheckResult
    override fun <T> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
    }

    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(FragmentEvent.CREATE)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(FragmentEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(FragmentEvent.RESUME)
    }

    @CallSuper
    override fun onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE)
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP)
        super.onStop()
    }

    override fun onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW)
        super.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY)
        super.onDestroy()
    }
}

```

### 4、BaseRxListFragment

package：cn.onestravel.library.kotlin.rxrequest.fragment

继承自BaseListFragment ，针对RxJava处理了相对应的生命周期，同时拥有BaseListFragment下拉刷新，上拉加载的所有特性。

### 5、BaseObserver<T: ResponseResult>

package：cn.onestravel.library.kotlin.rxrequest.common

RxJava+Retrofit 请求的观察者，返回特定数据结构的数据，并进行相应的处理。

具体实现如下：

```kotlin
/**
 * @name cn.onestravel.library.kotlin.rxrequest.common.BaseObserver
 * @description 请求返回结果，返回json数据必须符合 {"code":"0000","msg":""}
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
abstract class BaseObserver<T : ResponseResult> : Observer<T> {

    /**
     * 请求开始 处理基本的loading框的显示等
     *
     * @param d
     */
    fun onStart(d: Disposable) {
        Log.e(
            TAG,
            "===========单个接口请求开始========================================="
        )
    }

    /**
     * 此方法必须实现
     *
     * @param result 请求成功的结果
     */
    abstract fun onSuccess(result: T)

    /**
     * 请求失败
     *
     * @param code 错误码
     * @param msg  错误提示语
     */
    fun onFailure(code: String, msg: String?) {
        Log.e(
            TAG,
            "接口请求失败===code = " + code + "errorMsg =" + msg 
        )
    }

    /**
     * 请求都完成时之行此方法
     */
    fun onFinish() {

    }

    override fun onSubscribe(d: Disposable) {
        onStart(d)
    }

    override fun onNext(result: T) {
        if (BaseResponse.REQUEST_OK == result.code) {
            onSuccess(result)
            Log.i(TAG, "请求成功responseBody====" + JSON.toJSONString(result))
        } else {
            onFailure(result.code, result.msg)
        }

    }

    override fun onError(e: Throwable) {
        var errorMsg = ""
        var errorCode = 0
        if (e is HttpException) {
            val httpException = e as HttpException
            errorCode = httpException.response().code()
            if (404 == errorCode || 500 == errorCode) {
                errorMsg = "请求异常，请稍候重试！"
            } else {

            }

        } else {
            //todo 设置固定的错误码及错误提示
        }
        onFailure(errorCode.toString(), errorMsg)
    }

    override fun onComplete() {
        onFinish()
    }

    companion object {
        private val TAG = "request"
    }
}

```

BaseResponse:

```kotlin
/**
 * @name cn.onestravel.library.kotlin.rxrequest.common.BaseResponse
 * @description 请求返回结果数据基类
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
open class BaseResponse(val code: String = "0000", val msg: String = ""): Serializable {
    companion object {
        val REQUEST_OK = "0000"  //请求成功的code码
        val REQUEST_ERROR = "-1" //请求失败的code码
    }
}
```

ResponseResult:

```kotlin
/**
 * @name cn.onestravel.library.kotlin.rxrequest.common.ResponseResult
 * @description 请求返回结果数据 符合 {"code":"0000","msg":"","data":{},"datas":[]} 的基类
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
class ResponseResult :BaseResponse(), Serializable
```





### 6、BaseObserver1<DATA: Serializable>

package：cn.onestravel.library.kotlin.rxrequest.common

RxJava+Retrofit 请求的观察者，返回特定数据结构的数据，并进行相应的处理。

具体实现如下：

```kotlin
/**
 * @name cn.onestravel.library.kotlin.rxrequest.common.BaseObserver
 * @description 请求返回结果，返回json数据必须符合  {"code":"0000","msg":"","data":{},"datas":[]},data 和 datas 的数据 bean 相同
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
abstract class BaseObserver1<DATA : Serializable> : Observer<ResponseResult1<DATA>> {

    /**
     * 请求开始 处理基本的loading框的显示等
     *
     * @param d
     */
    fun onStart(d: Disposable) {
        Log.e(
            TAG,
            "===========单个接口请求开始  =========="
        )
    }

    /**
     * 此方法必须实现
     *
     * @param result 请求成功的结果
     */
    abstract fun onSuccess(result: ResponseResult1<DATA>)

    /**
     * 请求失败
     *
     * @param code 错误码
     * @param msg  错误提示语
     */
    fun onFailure(code: String, msg: String?) {
        Log.e(
            TAG,
            "接口请求失败============code = " + code + "errorMsg =" + msg    )
    }

    /**
     * 请求都完成时之行此方法
     */
    open fun onFinish() {

    }

    override fun onSubscribe(d: Disposable) {
        onStart(d)
    }

    override fun onNext(result: ResponseResult1<DATA>) {
        if (BaseResponse.REQUEST_OK == result.code) {
            onSuccess(result)
            Log.i(TAG, "请求成功responseBody====" + JSON.toJSONString(result))
        } else {
            onFailure(result.code, result.msg)
        }

    }

    override fun onError(e: Throwable) {
        var errorMsg = ""
        var errorCode = 0
        if (e is HttpException) {
            val httpException = e as HttpException
            errorCode = httpException.response().code()
            if (404 == errorCode || 500 == errorCode) {
                errorMsg = "请求异常，请稍候重试！"
            } else {

            }

        } else {
            //todo 设置固定的错误码及错误提示
        }
        onFailure(errorCode.toString(), errorMsg)
    }

    override fun onComplete() {
        onFinish()
    }

    companion object {
        private val TAG = "request"
    }
}

```

ResponseResult1:

```kotlin
/**
 * @name cn.onestravel.library.kotlin.rxrequest.common.ResponseResult
 * @description 请求返回结果数据 符合 {"code":"0000","msg":"","data":{},"datas":[]} 的基类
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
class ResponseResult1<DATA:Serializable>(
    val data: DATA?,
    val datas: MutableList<DATA>? = ArrayList()
) :BaseResponse(), Serializable


```



### 7、BaseObserver2<DATA : Serializable,ITEM:Serializable>

package：cn.onestravel.library.kotlin.rxrequest.common

RxJava+Retrofit 请求的观察者，返回特定数据结构的数据，并进行相应的处理。

具体实现如下：

```kotlin
package cn.onestravel.library.kotlin.rxrequest.common

import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.io.Serializable

/**
 * @name cn.onestravel.library.kotlin.rxrequest.common.BaseObserver2
 * @description 请求返回结果，返回json数据必须符合 {"code":"0000","msg":"","data":{},"datas":[]},data 和 datas 的数据 bean 可以不相同
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
abstract class BaseObserver2<DATA : Serializable,ITEM:Serializable> : Observer<ResponseResult2<DATA,ITEM>>, ObserverResult<ResponseResult2<DATA,ITEM>> {
    /**
     * 请求开始 处理基本的loading框的显示等
     *
     * @param d
     */
    override fun onStart(d: Disposable) {
        Log.e(
             BaseObserver2.TAG,
            "===========单个接口请求开始  =========="
        )
    }

    /**
     * 此方法必须实现
     *
     * @param result 请求成功的结果
     */
    abstract override fun onSuccess(result: ResponseResult2<DATA, ITEM>)

    /**
     * 请求失败
     *
     * @param code 错误码
     * @param msg  错误提示语
     */
    override fun onFailure(code: String, msg: String?) {
        Log.e(
             BaseObserver2.TAG,
            "接口请求失败============code = " + code + "errorMsg =" + msg    )
    }

    /**
     * 请求都完成时之行此方法
     */
    override fun onFinish() {

    }

    override fun onSubscribe(d: Disposable) {
        onStart(d)
    }
    override fun onNext(baseObject: ResponseResult2<DATA,ITEM>) {
        //        Log.i("responseBody====",)
        if (BaseResponse.REQUEST_OK == baseObject.code) {
            onSuccess(baseObject)
        } else {
            onFailure(baseObject.code, baseObject.msg)
        }
    }

    /**
     *
     */
    override fun onError(e: Throwable) {
        var errorMsg = ""
        var errorCode = 0
        if (e is HttpException) {
            val httpException = e as HttpException
            errorCode = httpException.response().code()
            if (404 == errorCode || 500 == errorCode) {
                errorMsg = "请求异常，请稍候重试！"
            } else {

            }

        } else {
            //todo 设置固定的错误码及错误提示
        }
        onFailure(errorCode.toString(), errorMsg)
    }

    override fun onComplete() {
        onFinish()
    }

    companion object {
        private val TAG = "RequestBaseObserver2"
    }
}

```

ResponseResult2:

```kotlin
/**
 * @name cn.onestravel.library.kotlin.rxrequest.common.ResponseResult
 * @description 请求返回结果数据 符合 {"code":"0000","msg":"","data":{},"datas":[]} 的基类
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
class ResponseResult2<DATA:Serializable,ITEM:Serializable>(
    val data: DATA?,
    val datas: MutableList<ITEM>? = ArrayList()
) :BaseResponse(), Serializable


```

### 8、BaseLoader

package：cn.onestravel.library.kotlin.rxrequest.loader

公共的Loader，处理observe针对请求进行线程切换

```kotlin
abstract class BaseLoader<S : BaseService> {
    protected val mServiceManager: RetrofitServiceManager by lazy { RetrofitServiceManager.INSTANCE }
    protected val mService: S by lazy { createService() }

    /**
     * 创建 Service 实例
     */
    abstract fun createService(): S

    /**
     * 设置Observable的工作线程
     * @param observable
     * @param <T>
     * @return
    </T> */
    fun <T> observe(observable: Observable<T>): Observable<T> {
        return observable.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


}

```

### 9、BaseService

package：cn.onestravel.library.kotlin.rxrequest.service

所有 Retrofit 请求的 Service 的接口基类，目前只用于规范子类，可声明对应公用方法



### 10、RetrofitServiceManager

package：cn.onestravel.library.kotlin.rxrequest.service

生成接口service实例的管理类，初始化Retrofit 实例，设置请求相关的一些拦截器、超时时间等配置，定义Http请求的公共URL

```kotlin
/**
 * @name cn.onestravel.library.kotlin.rxrequest.service.RetrofitServiceManager
 * @description 生成接口实例的管理类
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
class RetrofitServiceManager private constructor() {
    private val mRetrofit: Retrofit
    init {
        val interceptorBuild = HttpCommonInterceptor.Builder()
     
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClientBuild = OkHttpClient.Builder()
        okHttpClientBuild.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(interceptorBuild.build())//拦截器添加公共请求参数
            .addInterceptor(RetryInterceptor(2))//重试三次的拦截
                        .addInterceptor(logging)//请求日志打印

        //初始化Retrofit
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClientBuild.build())
            .build()

    }



    /**
     * 生成对应接口的实例
     *
     * @param service
     * @param <T>
     * @return
    </T> */
    fun <T:BaseService> create(service: Class<T>): T {
        return mRetrofit.create(service)

    }

    companion object {
        val INSTANCE by lazy { RetrofitServiceManager() }
        private const val CONNECTION_TIMEOUT = 5
        private const val READ_TIMEOUT = 20
        private const val WRITE_TIMEOUT = 10
        private const val BASE_URL = "http://192.168.1.12:9090/"
    }
}

```

## 三、采用Mvp架构封装的文件介绍

### 1、BaseMvpActivity<V : BaseMvpView, P : BaseMvpPresenter<V>>

package：cn.onestravel.library.kotlin.mvp.activity

所有Mvp架构的 Activity 的基类,自BaseActivity ，拥有BaseActivity的所有特性,处理了P层的相关生命周期。

```kotlin
/**
 * @name  BaseMvpActivity
 * @description 所有Mvp架构的 Activity 的基类
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
abstract class BaseMvpActivity<V : BaseMvpView, P : BaseMvpPresenter<V>> : BaseActivity(), BaseMvpView {
    private val presenter by lazy { createPresenter() }
    private var mLoadingDialog: LoadingDialog? = null
    protected abstract fun createPresenter(): P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (presenter == null) {
            throw  NullPointerException("Presenter is null! Do you return null in createPresenter()?")
        }
        presenter.onMvpAttachView(this as V, savedInstanceState)
    }


    override fun onStart() {
        super.onStart()
        presenter.let {
            it.onMvpStart()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.let {
            it.onMvpResume()
        }
    }

    override fun onPause() {
        presenter.let {
            it.onMvpPause()
        }
        super.onPause()
    }

    override fun onStop() {
        presenter.let {
            it.onMvpStop()
        }
        super.onStop()
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter?.let {
            it.onMvpSaveInstanceState(outState)
        }
    }

    override fun onDestroy() {
        presenter?.let {
            it.onMvpDetachView(false)
            it.onMvpDestroy()
        }
        mLoadingDialog?.let {
            it.destroy()
            mLoadingDialog = null
        }
        super.onDestroy()
    }

    override fun showLoading(content: String?) {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.getInstance(this)
            mLoadingDialog!!.title = content?:"正在加载..."
            if (!mLoadingDialog!!.isShowing) {
                mLoadingDialog!!.show()
            }
        }
    }

    override fun hideLoading() {
        mLoadingDialog?.let {
            mLoadingDialog!!.dismiss()
        }
    }

    override fun onResponseError(msg: String?) {
        msg?.let {
            showToast(msg)
        }
    }

}
```

### 2、BaseMvpListActivity<V : BaseMvpView, P : BaseMvpPresenter<V>>

package：cn.onestravel.library.kotlin.mvp.activity

所有Mvp架构的 ListActivity 的基类,自BaseListActivity ，拥有BaseListActivity的所有特性，并且处理了P层的相关生命周期。

### 3、BaseRxMvpActivity<V : BaseMvpView, P : BaseMvpPresenter<V>>

package：cn.onestravel.library.kotlin.mvp.activity

所有Mvp架构的 采用RxJava+Retrofit 请求的Activity 的基类，继承自BaseRxActivity ，拥有BaseRxActivity的所有特性，并且处理了P层的相关生命周期。



### 4、BaseRxMvpListActivity<V : BaseMvpView, P : BaseMvpPresenter<V>>

package：cn.onestravel.library.kotlin.mvp.activity

所有Mvp架构的 采用RxJava+Retrofit 请求的带有列表的Activity，继承自BaseRxListActivity ，拥有BaseRxListActivity的所有特性，并且处理了P层的相关生命周期。

### 5、Fragment 有与前4个Activity功能相同Fragment，就不多做介绍了。

package：cn.onestravel.library.kotlin.mvp.fragment

### 6、BaseMvpModel

package：cn.onestravel.library.kotlin.mvp.model

Mvp模式中所有Model层的基类，是一个接口，可以声明共同的方法，交由子类去实现。

### 7、BaseMvpPresenter

package：cn.onestravel.library.kotlin.mvp.presenter

Mvp模式中所有presenter层的基类，是一个接口，声明了presenter层相关的生命周期方法，由子类去实现。

```kotlin
/**
 * @name  BaseMvpPresenterImpl
 * @description 所有界面presenter的接口类
 * @createTime 2018/12/12 16:27
 * @author onestravel
 * @version 1.0.0
 */

interface BaseMvpPresenter<in V : BaseMvpView> {
    fun onMvpAttachView(view: V, savedInstanceState: Bundle?)

    fun onMvpStart()

    fun onMvpResume()

    fun onMvpPause()

    fun onMvpStop()

    fun onMvpSaveInstanceState(savedInstanceState: Bundle?)

    fun onMvpDetachView(retainInstance: Boolean)

    fun onMvpDestroy()

}
```

### 8、BaseMvpPresenterImpl<V: BaseMvpView>

package：cn.onestravel.library.kotlin.mvp.presenter.impl

Mvp模式中所有presenter层的接口的实现类，对presenter层相关的生命周期方法进行实现；并且实现与View层的绑定，持有View层的引用。

```kotlin
/**
 * @name  BaseMvpPresenterImpl
 * @description 所有界面presenter的接口类
 * @createTime 2018/12/12 16:27
 * @author onestravel
 * @version 1.0.0
 */

interface BaseMvpPresenter<in V : BaseMvpView> {
    fun onMvpAttachView(view: V, savedInstanceState: Bundle?)

    fun onMvpStart()

    fun onMvpResume()

    fun onMvpPause()

    fun onMvpStop()

    fun onMvpSaveInstanceState(savedInstanceState: Bundle?)

    fun onMvpDetachView(retainInstance: Boolean)

    fun onMvpDestroy()

}
```

### 9、BaseMvpView

package：cn.onestravel.library.kotlin.mvp.view

Mvp模式中所有View层的接口类，声明了一些View层常用的加载进度显示与隐藏，请求失败提示等公用的方法。

```kotlin
/**
 * @name  BaseMvpPresenterImpl
 * @description 所有界面presenter的接口类
 * @createTime 2018/12/12 16:27
 * @author onestravel
 * @version 1.0.0
 */

interface BaseMvpPresenter<in V : BaseMvpView> {
    fun onMvpAttachView(view: V, savedInstanceState: Bundle?)

    fun onMvpStart()

    fun onMvpResume()

    fun onMvpPause()

    fun onMvpStop()

    fun onMvpSaveInstanceState(savedInstanceState: Bundle?)

    fun onMvpDetachView(retainInstance: Boolean)

    fun onMvpDestroy()

}
```



## 声明：

针对Kotlin项目的一些常用封装，到这里就介绍完了，后面还会增加MVVM模式的一些封装，敬请期待！！

在封装或者介绍过程中，如有什么不正确或者不明白的地方，期待与您进行交流，共同提高！

联系邮件：server@onestravel.cn

鸣谢！！！

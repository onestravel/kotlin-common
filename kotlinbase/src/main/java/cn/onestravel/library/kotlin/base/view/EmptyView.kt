package cn.onestravel.library.kotlin.base.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.*
import cn.onestravel.library.kotlin.base.R
import cn.onestravel.library.kotlin.base.utils.DensityUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_empty.view.*


/**
 * @author wanghu
 * @version 1.0.0
 * @project BrZhongYiAndroid
 * @description 自定义空视图的显示，包含空页面，正在加载的页面，重新加载的页面
 * @createTime 2017/8/25
 */

class EmptyView : RelativeLayout {
    private var mContext: Context? = null
    private var text: String? = ""
    private var textSize = 15f
    private var textColor = Color.parseColor("#b1b1b1")
    private var loadingBackground = Color.parseColor("#00FFFFFF")
    private var emptyType = TYPE_EMPTY
    private var reloadBtnText: String? = "重新加载"
    private var reloadBtnTextSize = 15f
    private var reloadBtnTextColor = Color.parseColor("#999999")
    private var reloadText: String? = ""
    private var reloadTextSize = 15f
    private var reloadTextColor = Color.parseColor("#999999")
    private var noDataImg: Drawable? = null
    private var reloadImage: Drawable? = resources.getDrawable(R.drawable.load_error)
    private var reloadBtnBackground: Drawable? = null
    private var onReloadListener: OnReloadListener? = null
    private var loadingText = "正在加载..."

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    /**
     * 初始化布局
     *
     * @param context
     */
    private fun initView(context: Context, attrs: AttributeSet?) {
        this.mContext = context
        View.inflate(context, R.layout.view_empty, this)
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.mEmptyView)
            text = ta.getString(R.styleable.mEmptyView_emptyText)
            textSize = ta.getDimension(R.styleable.mEmptyView_emptyTextSize, DensityUtil.dip2px( 14).toFloat())
            textColor = ta.getColor(R.styleable.mEmptyView_emptyTextColor, Color.parseColor("#b1b1b1"))

            loadingBackground = ta.getColor(R.styleable.mEmptyView_loadingBackground, Color.parseColor("#00FFFFFF"))

            noDataImg = ta.getDrawable(R.styleable.mEmptyView_emptyImage)
            reloadImage = ta.getDrawable(R.styleable.mEmptyView_reloadImage)
            emptyType = ta.getInt(R.styleable.mEmptyView_emptyType, emptyType)
            reloadBtnText = ta.getString(R.styleable.mEmptyView_reloadBtnText)
            reloadBtnTextSize =
                    ta.getDimension(R.styleable.mEmptyView_reloadBtnTextSize, DensityUtil.dip2px( 15).toFloat())
            reloadBtnTextColor = ta.getColor(R.styleable.mEmptyView_reloadBtnTextColor, Color.parseColor("#bdbdbd"))
            reloadBtnBackground = ta.getDrawable(R.styleable.mEmptyView_reloadBtnBackground)

            reloadText = ta.getString(R.styleable.mEmptyView_reloadText)
            reloadTextSize = ta.getDimension(R.styleable.mEmptyView_reloadTextSize, DensityUtil.dip2px( 15).toFloat())
            reloadTextColor = ta.getColor(R.styleable.mEmptyView_reloadBtnTextColor, Color.parseColor("#bdbdbd"))

            ta.recycle()
            if (!TextUtils.isEmpty(text)) {
                tvEmptyMsg!!.text = text
            }
            if (textSize != -1f) {
                tvEmptyMsg!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            }
            tvEmptyMsg!!.setTextColor(textColor)

            if (noDataImg != null) {
                ivEmptyImg!!.setImageDrawable(noDataImg)
            }

            if (!TextUtils.isEmpty(reloadBtnText)) {
                btnReload!!.text = reloadBtnText
            }
            if (reloadBtnTextSize != -1f) {
                btnReload!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, reloadBtnTextSize)
            }
            btnReload!!.setTextColor(reloadBtnTextColor)
            if (reloadBtnBackground != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btnReload!!.background = reloadBtnBackground
                } else {
                    btnReload!!.setBackgroundDrawable(reloadBtnBackground)
                }
            }
            _setEmptyType(emptyType, visibility == View.VISIBLE)
        }

        btnReload!!.setOnClickListener {
            if (onReloadListener != null) {
                onReloadListener!!.onReload()
            }
        }

        if (reloadImage != null) {
            ivReloadImg!!.setImageDrawable(reloadImage)
        }

        if (!TextUtils.isEmpty(reloadText)) {
            tvReloadMsg!!.text = reloadText
        }
        if (reloadTextSize != -1f) {
            tvReloadMsg!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, reloadTextSize)
        }
        tvReloadMsg!!.setTextColor(reloadTextColor)
        this.setOnTouchListener { v, event -> true }
        layoutLoading!!.setBackgroundColor(loadingBackground)
        try {
            Glide.with(mContext!!)
                .load(R.drawable.loading_gif)
                .into(ivLoading!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

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
}

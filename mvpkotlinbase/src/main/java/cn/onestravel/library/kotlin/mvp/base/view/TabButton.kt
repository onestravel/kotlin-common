package cn.onestravel.library.kotlin.mvp.base.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import cn.onestravel.library.kotlin.mvp.base.utils.DensityUtil
import org.jetbrains.anko.below
import org.jetbrains.anko.padding

/**
 * @author onestravel
 * @createTime 2018/12/2 下午5:12
 * @description TODO
 */
class TabButton constructor(context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    private val root by lazy { RelativeLayout(context) }
    private val rootPadding = 5
    private val rootLayoutParams by lazy {
        LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT)
    }
    private val tabImageView by lazy {
        ImageView(context)
    }

    private val tabTextView by lazy {
        TextView(context)
    }

    private var mHeight = height
    private val textSize = 15f
    private var tabText = ""
    private lateinit var imageDrawable: Drawable
    private var tabMargin = 5

    init {
        val rootHeight = height - rootPadding * 2
        rootLayoutParams.gravity = Gravity.CENTER
        root.layoutParams = rootLayoutParams
        root.padding = 5
        addView(root)
        initView(root)
    }

    private fun initView(root: RelativeLayout) {
        tabTextView.text = tabText
        tabTextView.textSize = textSize
        val tabTextViewHeight = tabTextView.height
        val height = mHeight - rootPadding * 2 - tabTextViewHeight - tabMargin
        val tabImageViewLayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,height)
        tabImageViewLayoutParams.below(tabImageView)
        tabImageViewLayoutParams.topMargin = tabMargin
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }
}
package cn.onestravel.library.kotlin.base.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import cn.onestravel.library.kotlin.base.R
import cn.onestravel.library.kotlin.base.utils.DensityUtil
import org.jetbrains.anko.above
import org.jetbrains.anko.below
import org.jetbrains.anko.padding

/**
 * @author onestravel
 * @createTime 2018/12/2 下午5:12
 * @description TODO
 */
class TabButton :
    FrameLayout {


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

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//        //init(context)要在retrieveAttributes(attrs)前调用
//        //因为属性赋值，会直接赋值到控件上去。如:
//        //调用label = ""时，相当于调用了label的set方法。
//        init(context)
//        //retrieveAttributes(attrs: AttributeSet)方法只接受非空参数
//        attrs?.let { retrieveAttributes(attrs) }
    }

    init {
        val rootHeight = height - rootPadding * 2
        rootLayoutParams.gravity = Gravity.CENTER
        root.layoutParams = rootLayoutParams
        root.padding = 5
        addView(root)
        initView(root)
    }

//    override fun retrieveAttributes(attrs: AttributeSet) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
    private fun initView(root: RelativeLayout) {
        tabTextView.text = tabText
        tabTextView.textSize = textSize
        val tabTextViewHeight = tabTextView.height
        val height = mHeight - rootPadding * 2 - tabTextViewHeight - tabMargin
        val tabImageViewLayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, height)
        tabImageViewLayoutParams.above(tabTextView)
        tabImageViewLayoutParams.bottomMargin = tabMargin
        root.addView(tabTextView)
        root.addView(tabImageView)

        tabTextView.text = "哈哈哈"
        tabImageView.setImageResource(R.drawable.ic_loading)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }
}
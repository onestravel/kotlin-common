package cn.onestravel.library.kotlin.one.view

import android.content.Context
import android.graphics.Color
import androidx.annotation.AttrRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import cn.onestravel.library.kotlin.one.utils.DensityUtil

/**
 * @author onestravel
 * @version 1.0.0
 * @name ClassicalFooter
 * @description 经典上拉加载
 * @createTime 2018/11/30 15:23
 */
class ClassicalFooter @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), LoadMoreView {
    private var recyclerView: LoadMoreRecyclerView? = null
    private val arrawImg: ImageView
    private val textTitle: TextView
    private val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

    init {
        val root = LinearLayout(context)
        root.orientation = LinearLayout.HORIZONTAL
        root.gravity = Gravity.CENTER
        addView(root, FrameLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(50))
        (root.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.CENTER
        arrawImg = ImageView(context)
//        arrawImg.layoutParams = LayoutParams(50,50)
//        arrawImg.padding = 5
//        arrawImg.setImageResource(R.drawable.ic_loading)
//        arrawImg.scaleType = ImageView.ScaleType.CENTER
//        root.addView(arrawImg)
        textTitle = TextView(context)
        textTitle.textSize =16f
        textTitle.text = "上拉或点击加载更多..."
        textTitle.setTextColor(Color.parseColor("#999999"))
        val params = LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.leftMargin = 20
        root.addView(textTitle, params)
        rotateAnimation.duration = 800
        rotateAnimation.interpolator = LinearInterpolator()
        rotateAnimation.repeatCount = Animation.INFINITE
        rotateAnimation.repeatMode = Animation.RESTART
        setPadding(0, 1, 0, 1)
        setOnClickListener { recyclerView!!.startLoadMore() }
    }


    override fun shouldLoadMore(recyclerView: LoadMoreRecyclerView): Boolean {
        this.recyclerView = recyclerView
        return false
    }


    override fun onLoadMore(recyclerView: LoadMoreRecyclerView) {
        arrawImg.visibility = View.VISIBLE
        arrawImg.startAnimation(rotateAnimation)
        textTitle.text = "正在加载..."
    }


    override fun onComplete(recyclerView: LoadMoreRecyclerView, hasMore: Boolean) {
        arrawImg.clearAnimation()
        textTitle.text = if (hasMore) "上拉或点击加载更多..." else "没有更多数据"
        arrawImg.visibility = View.GONE
    }


    override fun onError(recyclerView: LoadMoreRecyclerView, errorCode: Int) {
        arrawImg.clearAnimation()
        textTitle.text = "加载失败,点击重新加载"
        arrawImg.visibility = View.GONE
    }
}
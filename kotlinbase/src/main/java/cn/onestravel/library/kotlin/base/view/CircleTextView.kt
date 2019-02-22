package cn.onestravel.library.kotlin.base.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.widget.TextView

/**
 * @author  onestravel
 * @version 1.0.0
 * @projectName BrZhongYiAndroid
 * @desctrion 自定义圆形的TextView，以适应不同的显示需求
 * @createTime 2017/7/7.˙
 */
class CircleTextView : AppCompatTextView {

    private val mBgPaint = Paint()

    private var pfd = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        // TODO Auto-generated constructor stub
        mBgPaint.color = Color.WHITE
        mBgPaint.isAntiAlias = true
    }

    constructor(context: Context) : super(context) {
        // TODO Auto-generated constructor stub
        mBgPaint.color = Color.WHITE
        mBgPaint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredWidth = measuredWidth
        val measuredHeight = measuredHeight
        val max = Math.max(measuredWidth, measuredHeight)
        setMeasuredDimension(max, max)
    }

    override fun setBackgroundColor(color: Int) {
        // TODO Auto-generated method stub
        mBgPaint.color = color
    }

    /**
     * 设置通知个数显示
     * @param text
     */
    fun setNotifiText(text: Int) {
        setText(text.toString() + "")
    }

    override fun draw(canvas: Canvas) {
        // TODO Auto-generated method stub
        canvas.drawFilter = pfd
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (Math.max(width, height) / 2).toFloat(),
            mBgPaint
        )
        super.draw(canvas)
    }
}
package cn.onestravel.library.kotlin.base.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.widget.ImageView
import cn.onestravel.library.kotlin.base.R
import cn.onestravel.library.kotlin.base.utils.ImageUtils


/**
 * @author  onestravel
 * @version 1.0.0
 * @projectName EasouAndroid-zy
 * @desctrion 自定义形状的ImageView ,REC 表示矩形的ImageView，CIRCLE 表示圆形的ImageView，OVAL 表示椭圆的ImageView
 * @createTime 2017/7/7.˙
 */

class ShapeImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    AppCompatImageView(context, attrs, defStyle) {

    var borderSize = 0f
        private set // 边框大小,默认为０，即无边框
    private var mBorderColor = Color.WHITE // 边框颜色，默认为白色
    var shape = SHAPE_REC // 形状，默认为直接矩形
    private var mRoundRadius = 0f // 矩形的圆角半径,默认为０，即直角矩形
    private val mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mViewRect = RectF() // imageview的矩形区域
    private val mBorderRect = RectF() // 边框的矩形区域

    private val mShaderMatrix = Matrix()
    private val mBitmapPaint = Paint()
    private var mBitmapShader: BitmapShader? = null
    private var mBitmap: Bitmap? = null

    var borderColor: Int
        get() = mBorderColor
        set(mBorderColor) {
            this.mBorderColor = mBorderColor
            mBorderPaint.color = mBorderColor
            invalidate()
        }

    var roundRadius: Float
        get() = mRoundRadius
        set(mRoundRadius) {
            this.mRoundRadius = mRoundRadius
            invalidate()
        }

    init {
        init(attrs)
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = borderSize
        mBorderPaint.color = mBorderColor
        mBorderPaint.isAntiAlias = true
        mBitmapPaint.isAntiAlias = true
        //        super.setScaleType(ScaleType.CENTER_CROP); // 固定为CENTER_CROP，其他不生效
    }// 虽然此处会调用setImageDrawable，但此时成员变量还未被正确初始化


    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        mBitmap = ImageUtils.getBitmapFromDrawable(drawable)
        setupBitmapShader()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        mBitmap = ImageUtils.getBitmapFromDrawable(drawable)
        setupBitmapShader()
    }

    override fun setScaleType(scaleType: ImageView.ScaleType) {
        if (scaleType != ImageView.ScaleType.CENTER_CROP) {
            //            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    private fun init(attrs: AttributeSet?) {

        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.ShapeImageView
        )
        shape = a.getInt(R.styleable.ShapeImageView_viewShape, shape)
        mRoundRadius = a.getDimension(R.styleable.ShapeImageView_round_radius, mRoundRadius)
        borderSize = a.getDimension(R.styleable.ShapeImageView_border_size, borderSize)
        mBorderColor = a.getColor(R.styleable.ShapeImageView_border_color, mBorderColor)
        a.recycle()
    }

    /**
     * 对于普通的view,在执行到onDraw()时，背景图已绘制完成
     *
     *
     * 对于ViewGroup,当它没有背景时直接调用的是dispatchDraw()方法, 而绕过了draw()方法，
     * 当它有背景的时候就调用draw()方法，而draw()方法里包含了dispatchDraw()方法的调用，
     */
    public override fun onDraw(canvas: Canvas) {

        if (drawable != null) {
            if (shape == SHAPE_CIRCLE) {
                canvas.drawCircle(
                    (width / 2).toFloat(), (height / 2).toFloat(),
                    (Math.min(width, height) / 2).toFloat(), mBitmapPaint
                )
            } else if (shape == SHAPE_OVAL) {
                canvas.drawOval(mViewRect, mBitmapPaint)
            } else {
                canvas.drawRoundRect(mViewRect, mRoundRadius, mRoundRadius, mBitmapPaint)
            }
        }


        if (borderSize > 0) { // 绘制边框
            if (shape == SHAPE_CIRCLE) {
                canvas.drawCircle(
                    mViewRect.right / 2, mViewRect.bottom / 2,
                    Math.min(mViewRect.right, mViewRect.bottom) / 2 - borderSize / 2, mBorderPaint
                )
            } else if (shape == SHAPE_OVAL) {
                canvas.drawOval(mBorderRect, mBorderPaint)
            } else {
                canvas.drawRoundRect(mBorderRect, mRoundRadius, mRoundRadius, mBorderPaint)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initRect()
        setupBitmapShader()
    }

    // 不能在onLayout()调用invalidate()，否则导致绘制异常。（setupBitmapShader()中调用了invalidate()）
    override fun onLayout(
        changed: Boolean, left: Int, top: Int, right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        //        initRect();
        //        setupBitmapShader();
    }

    private fun setupBitmapShader() {
        // super(context, attrs, defStyle)调用setImageDrawable时,成员变量还未被正确初始化
        if (mBitmapPaint == null) {
            return
        }
        if (mBitmap == null) {
            invalidate()
            return
        }
        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mBitmapPaint.shader = mBitmapShader

        // 固定为CENTER_CROP,使图片在ｖｉｅｗ中居中并裁剪
        mShaderMatrix.set(null)
        // 缩放到高或宽　与view的高或宽　匹配
        val scale = Math.max(width * 1f / mBitmap!!.width, height * 1f / mBitmap!!.height)
        // 由于BitmapShader默认是从画布的左上角开始绘制，所以把其平移到画布中间，即居中
        val dx = (width - mBitmap!!.width * scale) / 2
        val dy = (height - mBitmap!!.height * scale) / 2
        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(dx, dy)
        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
        invalidate()
    }

    //　设置图片的绘制区域
    private fun initRect() {

        mViewRect.top = 0f
        mViewRect.left = 0f
        mViewRect.right = width.toFloat() // 宽度
        mViewRect.bottom = height.toFloat() // 高度

        // 边框的矩形区域不能等于ImageView的矩形区域，否则边框的宽度只显示了一半
        mBorderRect.top = borderSize / 2
        mBorderRect.left = borderSize / 2
        mBorderRect.right = width - borderSize / 2
        mBorderRect.bottom = height - borderSize / 2
    }

    fun setBorderSize(mBorderSize: Int) {
        this.borderSize = mBorderSize.toFloat()
        mBorderPaint.strokeWidth = mBorderSize.toFloat()
        initRect()
        invalidate()
    }

    companion object {

        var SHAPE_REC = 1 // 矩形
        var SHAPE_CIRCLE = 2 // 圆形
        var SHAPE_OVAL = 3 // 椭圆
    }
}


package cn.onestravel.library.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener

/**
 * @name ImageUtils1
 * @description 实现使用图片加载框架加载网络图片
 * @createTime 2018/11/29 15:01
 * @author onestravel
 * @version 1.0.0
 */
object  ImageUtils{
    private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
    private const val COLORDRAWABLE_DIMENSION = 2
    fun loadImageUrl(context:Context,imgUrl:String,imageView: ImageView){
        Glide.with(context).load(imgUrl).into(imageView)
    }

    fun loadImageUrl(context:Context,imgUrl:String,imageView: ImageView, width: Int, height: Int){
        Glide.with(context).load(imgUrl).into(imageView)
    }

    fun loadImageBitmap(context:Context,imgUrl:String,imageView: ImageView){
        Glide.with(context).asBitmap().load(imgUrl).into(imageView)
    }

    fun loadImageGif(context:Context,imgUrl:String,imageView: ImageView){
        Glide.with(context).asGif().load(imgUrl).into(imageView)
    }

    fun loadImageCallBack(context:Context,imgUrl:String,imageView: ImageView,requestListener: RequestListener<Bitmap>){
        Glide.with(context).asBitmap().load(imgUrl).addListener(requestListener).into(imageView)
    }


    fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        try {
            val bitmap: Bitmap

            if (drawable is ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG)
            } else {
                bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, BITMAP_CONFIG)
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }
}

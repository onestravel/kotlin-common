package cn.onestravel.library.kotlin.mvp.base.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestListener

/**
 * @name cn.onestravel.library.kotlin.mvp.base.utils.ImageUtils
 * @description 实现使用图片加载框架加载网络图片
 * @createTime 2018/11/29 15:01
 * @author onestravel
 * @version 1.0.0
 */
object  ImageUtils{
    fun loadImageUrl(context:Context,imgUrl:String,imageView: ImageView){
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

}

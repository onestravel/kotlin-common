package cn.onestravel.library.common.extend

import android.graphics.Bitmap
import android.widget.ImageView
import cn.onestravel.library.common.utils.ImageUtils
import com.bumptech.glide.request.RequestListener

/**
 * @name ImageViewExtend
 * @description ImageView 加载网络图片的扩展方法
 * @createTime 2018/11/29 15:32
 * @author onestravel
 * @version 1.0.0
 */


/**
 *
 */
inline fun ImageView.loadImage(imgUrl: String): ImageView {
    ImageUtils.loadImageUrl(this.context, imgUrl, this)
    return this
}

inline fun ImageView.loadImage(imgUrl: String, width: Int, height: Int): ImageView {
    ImageUtils.loadImageUrl(this.context, imgUrl, this,width, height)
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

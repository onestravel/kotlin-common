package cn.onestravel.library.kotlin.mvp.base.utils

import android.content.res.Resources
import android.widget.ImageView
import cn.onestravel.library.kotlin.mvp.base.extend.loadImage

/**
 * @name cn.onestravel.library.kotlin.mvp.base.utils.DensityUtil
 * @description 尺寸转换工具类
 * @createTime 2018/11/30 15:27
 * @author onestravel
 * @version 1.0.0
 */
object DensityUtil {
    val density = Resources.getSystem().getDisplayMetrics().density
     fun dip2px(dp: Int): Int {
        return (dp * density).toInt()
    }

    fun dip2px(dp: Float): Int {
        return (dp * density).toInt()
    }

    fun appWidth(): Int {
        return Resources.getSystem().getDisplayMetrics().widthPixels
    }

    fun appHeight(): Int {
        return Resources.getSystem().getDisplayMetrics().heightPixels
    }


}
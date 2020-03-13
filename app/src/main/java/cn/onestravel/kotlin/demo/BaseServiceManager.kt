package cn.onestravel.kotlin.demo

import cn.onestravel.library.rxrequest.service.RetrofitServiceManager

/**
 * @author onestravel
 * @createTime 2019-08-03 10:49
 * @description TODO
 */
object BaseServiceManager : RetrofitServiceManager() {
    override fun getBaseUrl(): String {
        return "https://onestravel.cn/"
    }
}
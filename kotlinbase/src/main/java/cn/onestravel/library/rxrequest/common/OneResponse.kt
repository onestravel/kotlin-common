package cn.onestravel.library.rxrequest.common

import java.io.Serializable

/**
 * @name cn.onestravel.library.kotlin.rxrequest.common.OneResponse
 * @description 请求返回结果数据基类
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
open class OneResponse(val code: String = "0000", val msg: String = ""): Serializable {
    companion object {
        val REQUEST_OK = "0000"  //请求成功的code码
        val REQUEST_ERROR = "-1" //请求失败的code码
    }
}

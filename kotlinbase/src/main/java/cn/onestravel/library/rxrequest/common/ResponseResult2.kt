package cn.onestravel.library.rxrequest.common

import java.io.Serializable

/**
 * @name cn.onestravel.library.kotlin.rxrequest.common.ResponseResult
 * @description 请求返回结果数据 符合 {"code":"0000","msg":"","data":{},"datas":[]} 的基类
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
class ResponseResult2<DATA:Serializable,ITEM:Serializable>(
    val data: DATA?,
    val datas: MutableList<ITEM>? = ArrayList()
) :OneResponse(), Serializable


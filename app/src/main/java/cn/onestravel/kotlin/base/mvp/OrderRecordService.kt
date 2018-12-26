package cn.onestravel.kotlin.base.mvp

import cn.onestravel.kotlin.base.mvp.model.User
import cn.onestravel.library.kotlin.rxrequest.common.ResponseResult1
import cn.onestravel.library.kotlin.rxrequest.service.BaseService
import io.reactivex.Observable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 类描述：养生建议 订单记录相关的请求service
 * 创建人：YangYajun
 * 创建时间：2018/8/23
 */

interface OrderRecordService : BaseService {
    //获取订单记录列表
    @FormUrlEncoded
    @POST("apiController")
    fun getOrderRecordList(@FieldMap map: Map<String, String>): Observable<User>

    //获取订单详情
    @FormUrlEncoded
    @POST("apiController")
    fun getOrderDetailInfo(@FieldMap map: Map<String, String>): Observable<ResponseResult1<User>>
}

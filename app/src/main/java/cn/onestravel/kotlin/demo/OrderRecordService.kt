package cn.onestravel.kotlin.demo

import cn.onestravel.kotlin.demo.model.User
import cn.onestravel.library.rxrequest.common.ResponseResult1
import cn.onestravel.library.rxrequest.service.OneService
import io.reactivex.Observable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 类描述：订单记录相关的请求service
 * 创建人：
 * 创建时间：2018/8/23
 */

interface OrderRecordService : OneService {
    //获取订单记录列表
    @FormUrlEncoded
    @POST("orderList")
    fun getOrderRecordList(@FieldMap map: Map<String, String>): Observable<User>

    //获取订单详情
    @FormUrlEncoded
    @POST("orderDetail")
    fun getOrderDetailInfo(@FieldMap map: Map<String, String>): Observable<ResponseResult1<User>>
}

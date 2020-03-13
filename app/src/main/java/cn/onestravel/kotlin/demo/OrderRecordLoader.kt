package cn.onestravel.kotlin.demo


import cn.onestravel.kotlin.demo.model.User
import cn.onestravel.library.rxrequest.common.ResponseResult1
import io.reactivex.Observable

/**
 * 类描述：订单记录请求的相关loader
 * 创建人：
 * 创建时间：2018/8/23
 */

class OrderRecordLoader : BaseLoader<OrderRecordService>() {


    override fun createService(): OrderRecordService {
        return mServiceManager.create(OrderRecordService::class.java)
    }

    /**
     * 请求订单记录列表
     *
     * @param map
     * @return  订单列表的Bean
     */
    fun getOrderRecordList(map: Map<String, String>): Observable<User> {
        return observe(mService.getOrderRecordList(map))
    }

    /**
     * 请求订单列表的详情
     *
     * @param map
     * @return 详情的Bean
     */
    fun getOrderDetailInfo(map: Map<String, String>): Observable<ResponseResult1<User>> {
        return observe(mService.getOrderDetailInfo(map))
    }


}

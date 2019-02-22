package cn.onestravel.library.kotlin.rxrequest.common

import android.text.TextUtils
import okhttp3.*

import java.io.IOException
import java.util.HashMap

/**
 * @name cn.onestravel.library.kotlin.rxrequest.common.HttpCommonInterceptor
 * @description 添加公共参数请求拦截器
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
class HttpCommonInterceptor private constructor(private val mHeaderParamsMap: Map<String, String>?) : Interceptor {
    private val TAG = "request"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = FormBody.Builder()
        val oldRequest = chain.request()
        val mBuilder = oldRequest.url().newBuilder()
        //添加公共参数
        if (mHeaderParamsMap != null && mHeaderParamsMap!!.isNotEmpty()) {
            for (entry in mHeaderParamsMap!!.entries) {
                if (TextUtils.isEmpty(entry.value)) {
                    builder.add(entry.key, "")
                } else {
                    builder.add(entry.key, entry.value)
                }
            }
        }
        //将本次请求的参数添加进去
        if (oldRequest.body() is FormBody) {
            val body = oldRequest.body() as FormBody?
            for (i in 0 until body!!.size()) {
                if (TextUtils.isEmpty(body.encodedValue(i))) {
                    builder.add(body.encodedName(i), "")
                } else {
                    builder.add(body.encodedName(i), body.encodedValue(i))
                }
            }
        }
        //生成新的请求
        val request = oldRequest.newBuilder().url(mBuilder.build()).post(builder.build()).build()
        //打印请求参数相关代码开始
        val sb = StringBuilder()
        val body = request.body() as FormBody?
        var method_code = ""
        for (i in 0 until body!!.size()) {
            if ("method_code" == body.encodedName(i)) {
                method_code = body.encodedValue(i)
            }
            sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + "&")
        }
        sb.delete(sb.length - 1, sb.length)
        println("接口【 " + method_code + "】RequestParams====     " + request.url() + "?" + sb.toString())
        //打印请求参数相关代码结束
        return chain.proceed(request)
    }


    class Builder {
        private val mHeaderParamsMap: MutableMap<String, String>
        init {
            mHeaderParamsMap = HashMap()
        }

        fun addParams(key: String, value: String): Builder {
            mHeaderParamsMap[key] = value
            return this
        }

        fun build(): HttpCommonInterceptor {
            return HttpCommonInterceptor(mHeaderParamsMap)
        }
    }


}

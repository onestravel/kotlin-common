package cn.onestravel.library.kotlin.rxrequest.service

import cn.onestravel.library.kotlin.rxrequest.common.HttpCommonInterceptor
import cn.onestravel.library.kotlin.rxrequest.common.RetryInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit


/**
 * @name cn.onestravel.library.kotlin.rxrequest.service.RetrofitServiceManager
 * @description 生成接口实例的管理类
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
class RetrofitServiceManager private constructor() {
    private val mRetrofit: Retrofit
    init {
        val interceptorBuild = HttpCommonInterceptor.Builder()
        //添加公共请求参数,不可变的参数
//        interceptorBuild.addParams("userType", "1")
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClientBuild = OkHttpClient.Builder()
        okHttpClientBuild.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(interceptorBuild.build())//拦截器添加公共请求参数
            .addInterceptor(RetryInterceptor(2))//重试三次的拦截
                        .addInterceptor(logging)//请求日志打印

        //初始化Retrofit
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClientBuild.build())
            .build()

    }



    /**
     * 生成对应接口的实例
     *
     * @param service
     * @param <T>
     * @return
    </T> */
    fun <T:BaseService> create(service: Class<T>): T {
        return mRetrofit.create(service)

    }

    companion object {
        val INSTANCE by lazy { RetrofitServiceManager() }
        private const val CONNECTION_TIMEOUT = 5
        private const val READ_TIMEOUT = 20
        private const val WRITE_TIMEOUT = 10
        private const val BASE_URL = "https://testapi.brzhongyi.cn:9090/easydoctorv2-ws/"
    }
}

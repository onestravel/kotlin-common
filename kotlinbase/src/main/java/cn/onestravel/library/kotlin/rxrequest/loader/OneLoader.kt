package cn.onestravel.library.kotlin.rxrequest.loader


import cn.onestravel.library.kotlin.rxrequest.service.OneService
import cn.onestravel.library.kotlin.rxrequest.service.RetrofitServiceManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @name OneLoader
 * @description 封装公共loader进行线程切换
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
abstract class OneLoader<S : OneService> {
    protected val mServiceManager: RetrofitServiceManager by lazy { createServiceManager() }
    protected val mService: S by lazy { createService() }

    /**
     * 创建 ServiceManager 实例
     */
    abstract fun createServiceManager(): RetrofitServiceManager

    /**
     * 创建 Service 实例
     */
    abstract fun createService(): S

    /**
     * 设置Observable的工作线程
     * @param observable
     * @param <T>
     * @return
    </T> */
    fun <T> observe(observable: Observable<T>): Observable<T> {
        return observable.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


}

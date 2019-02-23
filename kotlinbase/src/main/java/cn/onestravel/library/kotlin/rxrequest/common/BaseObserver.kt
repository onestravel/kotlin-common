package cn.onestravel.library.kotlin.rxrequest.common

import android.util.Log
import com.alibaba.fastjson.JSON
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

/**
 * @name cn.onestravel.library.kotlin.rxrequest.common.BaseObserver
 * @description 请求返回结果，返回json数据必须符合 {"code":"0000","msg":""}
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
abstract class BaseObserver<T : ResponseResult> : Observer<T>,ObserverResult<T> {

    /**
     * 请求开始 处理基本的loading框的显示等
     *
     * @param d
     */
    override fun onStart(d: Disposable) {
        Log.e(
            TAG,
            "===========单个接口请求开始  =========="
        )
    }

    /**
     * 此方法必须实现
     *
     * @param result 请求成功的结果
     */
    abstract override fun onSuccess(result: T)

    /**
     * 请求失败
     *
     * @param code 错误码
     * @param msg  错误提示语
     */
    override fun onFailure(code: String, msg: String?) {
        Log.e(
            TAG,
            "接口请求失败============code = " + code + "errorMsg =" + msg 
        )
    }

    /**
     * 请求都完成时之行此方法
     */
    override fun onFinish() {

    }

    override fun onSubscribe(d: Disposable) {
        onStart(d)
    }

    override fun onNext(result: T) {
        if (BaseResponse.REQUEST_OK == result.code) {
            onSuccess(result)
            Log.i(TAG, "请求成功responseBody====" + JSON.toJSONString(result))
        } else {
            onFailure(result.code, result.msg)
        }

    }

    override fun onError(e: Throwable) {
        var errorMsg = ""
        var errorCode = 0
        if (e is HttpException) {
            val httpException = e as HttpException
            errorCode = httpException.response().code()
            if (404 == errorCode || 500 == errorCode) {
                errorMsg = "请求异常，请稍候重试！"
            } else {

            }

        } else {
            //todo 设置固定的错误码及错误提示
        }
        onFailure(errorCode.toString(), errorMsg)
    }

    override fun onComplete() {
        onFinish()
    }

    companion object {
        private val TAG = "RequestBaseObserver"
    }
}

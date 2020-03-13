package cn.onestravel.library.rxrequest.common

import io.reactivex.disposables.Disposable

/**
 * @name cn.onestravel.library.kotlin.rxrequest.common.ObserverResult
 * @description //TODO
 * @createTime 2018/12/13 12:11
 * @author onestravel
 * @version 1.0.0
 */
interface ObserverResult<R> {

    /**
     * 请求开始 处理基本的loading框的显示等
     * @param d
     */
    fun onStart(d: Disposable)

    /**
     * 此方法必须实现
     * @param result 请求成功的结果
     */
    fun onSuccess(result: R)

    /**
     * 请求失败
     * @param code 错误码
     * @param msg 错误提示语
     */
    fun onFailure(code: String, msg: String?)

    /**
     * 请求都完成时之行此方法
     */
    fun onFinish()

    companion object {
        private val TAG = "request"
    }
}
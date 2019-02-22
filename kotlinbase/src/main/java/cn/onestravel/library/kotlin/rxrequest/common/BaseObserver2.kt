package cn.onestravel.library.kotlin.rxrequest.common

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.io.Serializable

/**
 * @name cn.onestravel.library.kotlin.rxrequest.common.BaseObserver2
 * @description 请求返回结果，返回json数据必须符合 {"code":"0000","msg":"","data":{},"datas":[]},data 和 datas 的数据 bean 可以不相同
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
abstract class BaseObserver2<DATA : Serializable,ITEM:Serializable> : Observer<ResponseResult2<DATA,ITEM>>, ObserverResult<ResponseResult2<DATA,ITEM>> {
    override fun onFailure(code: String?, msg: String?) {
    }

    override fun onFinish() {
    }

    override fun onStart(d: Disposable) {

    }

    abstract override fun onSuccess(result: ResponseResult2<DATA,ITEM>)


    override fun onSubscribe(d: Disposable) {
        onStart(d)
    }
    override fun onNext(baseObject: ResponseResult2<DATA,ITEM>) {
        //        Log.i("responseBody====",)
        if (BaseResponse.REQUEST_OK == baseObject.code) {
            onSuccess(baseObject)
        } else {
            onFailure(baseObject.code, baseObject.msg)
        }
    }

    /**
     *
     */
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
}

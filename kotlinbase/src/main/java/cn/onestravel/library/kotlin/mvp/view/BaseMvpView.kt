package cn.onestravel.library.kotlin.mvp.view

/**
 * @name BaseMvpView
 * @description //TODO
 * @createTime 2018/12/12 16:28
 * @author onestravel
 * @version 1.0.0
 */
interface BaseMvpView {
    fun showLoading(content:String?="正在加载...")
    fun hideLoading()
    fun onResponseError(msg: String?)
}
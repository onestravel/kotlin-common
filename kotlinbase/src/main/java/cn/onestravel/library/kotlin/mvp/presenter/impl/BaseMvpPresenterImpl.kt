package cn.onestravel.library.kotlin.mvp.presenter.impl

import android.os.Bundle
import cn.onestravel.library.kotlin.mvp.presenter.BaseMvpPresenter
import cn.onestravel.library.kotlin.mvp.view.BaseMvpView
import java.lang.ref.WeakReference

/**
 * @name cn.onestravel.library.kotlin.mvp.presenter.impl.BaseMvpPresenterImpl
 * @description 所有Mvp 的Presenter 的基类，对生命周期进行处理
 * @createTime 2018/12/12 16:52
 * @author onestravel
 * @version 1.0.0
 */
open class BaseMvpPresenterImpl<V : BaseMvpView> : BaseMvpPresenter<V> {
    private var viewRef: WeakReference<V>? = null

    protected fun getView(): V? {
        return viewRef?.get()
    }

    protected fun isViewAttached(): Boolean {
        return viewRef?.get() != null
    }

    private fun attach(view: V, savedInstanceState: Bundle?) {
        viewRef = WeakReference(view)
    }


    override fun onMvpAttachView(view: V, savedInstanceState: Bundle?) {
        attach(view, savedInstanceState)
    }

    override fun onMvpStart() {
    }

    override fun onMvpResume() {
    }

    override fun onMvpPause() {
    }

    override fun onMvpStop() {
    }

    override fun onMvpSaveInstanceState(savedInstanceState: Bundle?) {
    }

    override fun onMvpDetachView(retainInstance: Boolean) {
        detach(retainInstance)
    }

    override fun onMvpDestroy() {
    }

    private fun detach( retainInstance: Boolean) {
        viewRef.let {
            viewRef?.clear()
            viewRef = null
        }
    }

}
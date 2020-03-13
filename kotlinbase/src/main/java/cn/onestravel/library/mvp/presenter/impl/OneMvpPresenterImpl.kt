package  cn.onestravel.library.mvp.presenter.impl

import android.os.Bundle
import cn.onestravel.library.mvp.model.OneMvpModel
import cn.onestravel.library.mvp.presenter.OneMvpPresenter
import cn.onestravel.library.mvp.view.OneMvpView

/**
 * @name  OneMvpPresenterImpl
 * @description 所有Mvp 的Presenter 的基类，对生命周期进行处理
 * @createTime 2018/12/12 16:52
 * @author onestravel
 * @version 1.0.0
 */
open abstract class OneMvpPresenterImpl<M : OneMvpModel, V : OneMvpView> : OneMvpPresenter<V> {

    protected val mModel: M? by lazy { createModel() }
    protected var mView: V? = null

    abstract fun createModel(): M?

    protected fun getView(): V? {
        return mView
    }

    protected fun isViewAttached(): Boolean {
        return mView != null
    }


    private fun attach(view: V, savedInstanceState: Bundle?) {
        mView = view
    }

    override fun onMvpAttachView(view: V, savedInstanceState: Bundle?) {
        attach(view, savedInstanceState)
        init()
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
        detach(true)
    }

    private fun detach(retainInstance: Boolean) {
        mView?.let {
            mView = null
        }
    }

}



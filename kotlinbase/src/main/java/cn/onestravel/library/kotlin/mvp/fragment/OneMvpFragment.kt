package cn.onestravel.library.kotlin.mvp.fragment

import android.os.Bundle
import cn.onestravel.library.kotlin.one.fragment.OneFragment
import cn.onestravel.library.kotlin.mvp.presenter.OneMvpPresenter
import cn.onestravel.library.kotlin.mvp.view.OneMvpView
import cn.onestravel.library.kotlin.one.view.LoadingDialog

/**
 * @name  OneMvpActivity
 * @description 所有Mvp架构的Fragment的基类
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
abstract class OneMvpFragment<V : OneMvpView, P : OneMvpPresenter<V>> : OneFragment(), OneMvpView {
    protected val mPresenter by lazy { createPresenter() }
    protected var mLoadingDialog: LoadingDialog? = null
    protected abstract fun createPresenter(): P

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mPresenter == null) {
            throw  NullPointerException("Presenter is null! Do you return null in createPresenter()?")
        }
        mPresenter.onMvpAttachView(this as V, savedInstanceState)
    }




    override fun onStart() {
        super.onStart()
        mPresenter.let {
            it.onMvpStart()
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.let {
            it.onMvpResume()
        }
    }

    override fun onPause() {
        super.onPause()
        mPresenter.let {
            it.onMvpPause()
        }
    }

    override fun onStop() {
        super.onStop()
        mPresenter.let {
            it.onMvpStop()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mPresenter.let {
            it.onMvpSaveInstanceState(outState)
        }
    }

    override fun onDestroy() {

        mLoadingDialog?.let {
            it.destroy()
            mLoadingDialog = null
        }
        mPresenter.let {
            it.onMvpDetachView(false)
            it.onMvpDestroy()
        }
        super.onDestroy()

    }

    override fun showLoading(content: String?) {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.getInstance(context!!)
            mLoadingDialog!!.title = content ?: "正在加载..."
            if (!mLoadingDialog!!.isShowing) {
                mLoadingDialog!!.show()
            }
        }

    }

    override fun hideLoading() {
        mLoadingDialog?.let {
            mLoadingDialog!!.dismiss()
        }
    }

    override fun onResponseError(msg: String?) {
        msg?.let {
            showToast(msg)
        }
    }


}
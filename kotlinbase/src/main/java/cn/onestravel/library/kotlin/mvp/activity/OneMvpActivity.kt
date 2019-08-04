package cn.onestravel.library.kotlin.mvp.activity

import android.os.Bundle
import cn.onestravel.library.kotlin.mvp.model.OneMvpModel
import cn.onestravel.library.kotlin.one.activity.OneActivity
import cn.onestravel.library.kotlin.one.view.LoadingDialog
import cn.onestravel.library.kotlin.mvp.presenter.OneMvpPresenter
import cn.onestravel.library.kotlin.mvp.view.OneMvpView

/**
 * @name  OneMvpActivity
 * @description 所有Mvp架构的 Activity 的基类
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
abstract class OneMvpActivity<V : OneMvpView, P : OneMvpPresenter<V>> : OneActivity(), OneMvpView {
    protected val mPresenter: P by lazy { createPresenter() }
    protected var mLoadingDialog: LoadingDialog? = null
    protected abstract fun createPresenter(): P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mPresenter == null) {
            throw  NullPointerException("Presenter is null! Do you return null in createPresenter()?")
        }
        mPresenter.onMvpAttachView(this as V, savedInstanceState)
    }


    override fun onStart() {
        super.onStart()
        mPresenter?.let {
            it.onMvpStart()
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.let {
            it.onMvpResume()
        }
    }

    override fun onPause() {
        mPresenter?.let {
            it.onMvpPause()
        }
        super.onPause()
    }

    override fun onStop() {
        mPresenter?.let {
            it.onMvpStop()
        }
        super.onStop()
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mPresenter?.let {
            it.onMvpSaveInstanceState(outState)
        }
    }

    override fun onDestroy() {
        mPresenter?.let {
            it.onMvpDetachView(false)
            it.onMvpDestroy()
        }
        mLoadingDialog?.let {
            it.destroy()
            mLoadingDialog = null
        }
        super.onDestroy()
    }

    override fun showLoading(content: String?) {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.getInstance(this)
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
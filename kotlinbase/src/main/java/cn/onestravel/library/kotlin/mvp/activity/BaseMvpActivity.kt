package cn.onestravel.library.kotlin.mvp.activity

import android.os.Bundle
import cn.onestravel.library.kotlin.base.activity.BaseActivity
import cn.onestravel.library.kotlin.base.view.LoadingDialog
import cn.onestravel.library.kotlin.mvp.presenter.BaseMvpPresenter
import cn.onestravel.library.kotlin.mvp.view.BaseMvpView

/**
 * @name cn.onestravel.library.kotlin.mvp.activity.BaseMvpActivity
 * @description 所有Mvp架构的 Activity 的基类
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
abstract class BaseMvpActivity<V : BaseMvpView, P : BaseMvpPresenter<V>> : BaseActivity(), BaseMvpView {
    private val presenter by lazy { createPresenter() }
    private var mLoadingDialog: LoadingDialog? = null
    protected abstract fun createPresenter(): P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (presenter == null) {
            throw  NullPointerException("Presenter is null! Do you return null in createPresenter()?")
        }
        presenter.onMvpAttachView(this as V, savedInstanceState)
    }


    override fun onStart() {
        super.onStart()
        presenter.let {
            it.onMvpStart()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.let {
            it.onMvpResume()
        }
    }

    override fun onPause() {
        presenter.let {
            it.onMvpPause()
        }
        super.onPause()
    }

    override fun onStop() {
        presenter.let {
            it.onMvpStop()
        }
        super.onStop()
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter?.let {
            it.onMvpSaveInstanceState(outState)
        }
    }

    override fun onDestroy() {
        presenter?.let {
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
            mLoadingDialog!!.title = content?:"正在加载..."
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
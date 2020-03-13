package cn.onestravel.library.mvp.activity

import android.os.Bundle
import cn.onestravel.library.common.activity.OneListActivity
import cn.onestravel.library.common.view.LoadingDialog
import cn.onestravel.library.mvp.presenter.OneMvpPresenter
import cn.onestravel.library.mvp.view.OneMvpView

/**
 * @name  OneMvpListActivity
 * @description 所有Mvp架构的 默认含有RecyclerView的可下拉刷新，上拉加载（可隐藏）Activity基类
 * @createTime 2018/12/12 17:00
 * @author onestravel
 * @version 1.0.0
 */
abstract class OneMvpListActivity<V : OneMvpView, P : OneMvpPresenter<V>> : OneListActivity(), OneMvpView {
    protected val mPresenter by lazy { createPresenter() }
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
        mPresenter.let {
            it.onMvpPause()
        }
        super.onPause()
    }

    override fun onStop() {
        mPresenter.let {
            it.onMvpStop()
        }
        super.onStop()
    }


    override fun onSaveInstanceState(outState: Bundle) {
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
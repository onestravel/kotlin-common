package cn.onestravel.library.mvvm.activity

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.onestravel.library.common.activity.OneActivity
import cn.onestravel.library.mvvm.viewmodel.OneMvvmViewModel
import java.lang.reflect.ParameterizedType

/**
 * @name  OneMvvmActivity
 * @description //TODO
 * @createTime 2018/12/26 17:13
 * @author onestravel
 * @version 1.0.0
 */
abstract class OneMvvmActivity<T : OneMvvmViewModel> : OneActivity() {
    private val lastClickTime: Long = 0
    protected var mViewModel: T? = null
    protected val TAG: String = this.javaClass.simpleName


    override fun initView() {
        try {
            mViewModel = getTClass()?.let { ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(it) }
            mViewModel!!.getToastResId()!!.observe(this, ToastResObserver())
            mViewModel!!.getToast()!!.observe(this, ToastObserver())
        } catch (e: java.lang.Exception) {
            Log.e(
                this.javaClass.name,
                "Activity must specify the type of the BaseViewModel subclass",
                e
            )
        }
        super.initView()
    }


    protected open fun setToolBarTitle(title: String?) {
        try { //            ((TextView) findViewById(R.id.toolbar_title)).setText(title);
        } catch (e: Exception) {
        }
    }


    protected open fun onBack() {
        finish()
    }


    private fun getTClass(): Class<T>? {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
    }


    protected open fun clickablePostDelay(view: View) {
        view.isClickable = false
        view.postDelayed({ view.isClickable = true }, 1000)
    }

    override fun onDestroy() {
        try {
            if (mViewModel != null) {
                mViewModel = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDestroy()
    }

    inner class ToastResObserver : Observer<Int> {
        override fun onChanged(resId: Int) {
            showToast(resId)
        }


    }

    inner class ToastObserver : Observer<String?> {
        override fun onChanged(msg: String?) {
            msg?.let {
                showToast(msg)
            }

        }
    }
}
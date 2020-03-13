package cn.onestravel.library.mvvm.fragment

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.onestravel.library.common.fragment.OneFragment
import cn.onestravel.library.mvvm.viewmodel.OneMvvmViewModel
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.android.FragmentEvent

import java.lang.reflect.ParameterizedType

/**
 * @name  .OneMvvmActivity
 * @description //TODO
 * @createTime 2018/12/26 17:13
 * @author onestravel
 * @version 1.0.0
 */
abstract class OneMvvmFragment<T : OneMvvmViewModel> : OneFragment(),
    LifecycleProvider<FragmentEvent> {
    protected val TAG: String = this.javaClass.simpleName
    protected var viewModel:T? = null

    override fun initView(contentView: View) {
        try {
            viewModel = getTClass()?.let { ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)).get(it) };
            viewModel!!.getToastResId().observe(this, ToastResObserver());
            viewModel!!.getToast().observe(this, ToastObserver());
        } catch ( e:Exception) {
            Log.e(this.javaClass.name, "Fragment must specify the type of the BaseViewModel subclass", e);
        }
        super.initView(contentView)
    }

    open fun getTClass(): Class<T>? {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
    }




    protected open fun clickablePostDelay(view: View) {
        view.isClickable = false
        view.postDelayed({ view.isClickable = true }, 1000)
    }

    inner class ToastResObserver : Observer<Int> {
        override fun onChanged(resId: Int) {
            showToast(resId)
        }

    }

    inner class ToastObserver : Observer<String> {
        override fun onChanged(msg: String) {
            showToast(msg)
        }
    }
}

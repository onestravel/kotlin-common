package cn.onestravel.library.mvvm.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @name OneMvvmViewModel
 * @description //TODO
 * @createTime 2018/12/26 17:16
 * @author onestravel
 * @version 1.0.0
 */
class OneMvvmViewModel: ViewModel() {
        val TAG = this.javaClass.simpleName
        private val toastResId = MutableLiveData<Int>()
        private val toast = MutableLiveData<String>()

        fun showToast(@StringRes resId: Int) {
            toastResId.postValue(resId)
        }

        fun getToastResId(): MutableLiveData<Int> {
            return toastResId
        }


        fun showToast(msg: String) {
            toast.postValue(msg)
        }

        fun getToast(): MutableLiveData<String> {
            return toast
        }

        override fun onCleared() {
            super.onCleared()
        }



}
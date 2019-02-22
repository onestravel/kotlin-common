package cn.onestravel.library.kotlin.mvp.presenter

import android.os.Bundle
import cn.onestravel.library.kotlin.mvp.view.BaseMvpView

/**
 * @name  BaseMvpPresenterImpl
 * @description 所有界面presenter的基类
 * @createTime 2018/12/12 16:27
 * @author onestravel
 * @version 1.0.0
 */

interface BaseMvpPresenter<in V : BaseMvpView> {
    fun onMvpAttachView(view: V, savedInstanceState: Bundle?)

    fun onMvpStart()

    fun onMvpResume()

    fun onMvpPause()

    fun onMvpStop()

    fun onMvpSaveInstanceState(savedInstanceState: Bundle?)

    fun onMvpDetachView(retainInstance: Boolean)

    fun onMvpDestroy()

}
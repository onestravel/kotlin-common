package cn.test

import android.os.Bundle
import cn.onestravel.library.kotlin.mvp.presenter.impl.BaseMvpPresenterImpl

/**
 * @name cn.test.MainPresenterImpl
 * @description //TODO
 * @createTime 2018/12/12 17:27
 * @author onestravel
 * @version 1.0.0
 */
class MainPresenterImpl : BaseMvpPresenterImpl<IMainView>(), IMainPresenter {
    override fun requestTestContent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMvpAttachView(view: IMainView, savedInstanceState: Bundle?) {
        super.onMvpAttachView(view, savedInstanceState)
    }

    override fun onMvpStart() {
        super.onMvpStart()
    }
}
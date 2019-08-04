package cn.test

import android.os.Bundle
import cn.onestravel.kotlin.demo.model.User
import cn.onestravel.kotlin.demo.model.UserModel
import cn.onestravel.library.kotlin.mvp.presenter.impl.OneMvpPresenterImpl

/**
 * @name cn.test.MainPresenterImpl
 * @description //TODO
 * @createTime 2018/12/12 17:27
 * @author onestravel
 * @version 1.0.0
 */
class MainPresenterImpl : OneMvpPresenterImpl<UserModel,IMainView>(), IMainPresenter {
    override fun createModel(): UserModel? {
        return null
    }


    override fun init() {
    }



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
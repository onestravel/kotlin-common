package cn.onestravel.kotlin.demo.presenter.impl

import cn.onestravel.kotlin.demo.model.UserModel
import cn.onestravel.kotlin.demo.presenter.interf.UserPresenter
import cn.onestravel.kotlin.demo.view.UserView
import cn.onestravel.library.mvp.presenter.impl.OneMvpPresenterImpl

/**
 * @name cn.onestravel.kotlin.base.mvp.mPresenter.impl.UserPresenterImpl
 * @description //TODO
 * @createTime 2018/12/26 14:48
 * @author onestravel
 * @version 1.0.0
 */
class UserPresenterImpl: OneMvpPresenterImpl<UserModel,UserView>(),
    UserPresenter {
    override fun init() {
    }

    override fun createModel(): UserModel {
        return UserModel()
    }

    override fun requestUserInfo() {

    }

    override fun requestUserList(page: Int) {

    }
}
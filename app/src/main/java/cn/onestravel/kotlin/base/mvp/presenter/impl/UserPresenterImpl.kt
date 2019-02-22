package cn.onestravel.kotlin.base.mvp.presenter.impl

import cn.onestravel.kotlin.base.mvp.presenter.interf.UserPresenter
import cn.onestravel.kotlin.base.mvp.view.UserView
import cn.onestravel.library.kotlin.mvp.presenter.BaseMvpPresenter
import impl.BaseMvpPresenterImpl

/**
 * @name cn.onestravel.kotlin.base.mvp.presenter.impl.UserPresenterImpl
 * @description //TODO
 * @createTime 2018/12/26 14:48
 * @author onestravel
 * @version 1.0.0
 */
class UserPresenterImpl: BaseMvpPresenterImpl<UserView>(),UserPresenter {
    override fun requestUserInfo() {

    }

    override fun requestUserList(page: Int) {

    }
}
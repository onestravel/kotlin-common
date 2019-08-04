package cn.onestravel.kotlin.demo.presenter.interf

import cn.onestravel.kotlin.demo.model.UserModel
import cn.onestravel.kotlin.demo.view.UserView
import cn.onestravel.library.kotlin.mvp.presenter.OneMvpPresenter

/**
 * @name cn.onestravel.kotlin.base.mvp.mPresenter.interf.UserPresenter
 * @description //TODO
 * @createTime 2018/12/26 14:36
 * @author onestravel
 * @version 1.0.0
 */
interface UserPresenter: OneMvpPresenter<UserView> {
    fun requestUserInfo()
    fun requestUserList(page:Int)
}
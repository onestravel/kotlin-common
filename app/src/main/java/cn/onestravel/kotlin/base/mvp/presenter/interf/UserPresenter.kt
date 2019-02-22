package cn.onestravel.kotlin.base.mvp.presenter.interf

import cn.onestravel.kotlin.base.mvp.view.UserView
import  BaseMvpPresenter

/**
 * @name cn.onestravel.kotlin.base.mvp.presenter.interf.UserPresenter
 * @description //TODO
 * @createTime 2018/12/26 14:36
 * @author onestravel
 * @version 1.0.0
 */
interface UserPresenter: BaseMvpPresenter<UserView> {
    fun requestUserInfo()
    fun requestUserList(page:Int)
}
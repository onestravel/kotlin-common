package cn.onestravel.kotlin.demo.view

import cn.onestravel.kotlin.demo.model.User
import cn.onestravel.library.kotlin.mvp.view.OneMvpView

/**
 * @name cn.onestravel.kotlin.base.mvp.view.UserView
 * @description //TODO
 * @createTime 2018/12/26 14:40
 * @author onestravel
 * @version 1.0.0
 */
interface UserView:OneMvpView {
    fun setUserInfo(user: User)

    fun setUserList(list:MutableList<User>)
}
package cn.onestravel.kotlin.base.mvp.ui.activity

import cn.onestravel.kotlin.base.mvp.R
import cn.onestravel.kotlin.base.mvp.presenter.impl.UserPresenterImpl
import cn.onestravel.kotlin.base.mvp.presenter.interf.UserPresenter
import cn.onestravel.kotlin.base.mvp.view.UserView
import cn.onestravel.library.kotlin.mvp.activity.BaseMvpActivity

/**
 * @name cn.onestravel.kotlin.base.mvp.ui.activity.UserActivity
 * @description //TODO
 * @createTime 2018/12/26 14:52
 * @author onestravel
 * @version 1.0.0
 */
class UserActivity: BaseMvpActivity<UserView, UserPresenter>() {
    override fun createPresenter(): UserPresenter {
        return UserPresenterImpl()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }



}
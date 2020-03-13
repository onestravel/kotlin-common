package cn.onestravel.kotlin.demo.ui.activity

import cn.onestravel.kotlin.demo.mvp.R
import cn.onestravel.kotlin.demo.presenter.impl.UserPresenterImpl
import cn.onestravel.kotlin.demo.presenter.interf.UserPresenter
import cn.onestravel.kotlin.demo.view.UserView
import cn.onestravel.library.mvp.activity.OneMvpActivity

/**
 * @name cn.onestravel.kotlin.base.mvp.ui.activity.UserActivity
 * @description //TODO
 * @createTime 2018/12/26 14:52
 * @author onestravel
 * @version 1.0.0
 */
class UserActivity: OneMvpActivity<UserView, UserPresenter>() {
    override fun createPresenter(): UserPresenter {
        return UserPresenterImpl()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


}
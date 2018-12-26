package cn.test

import cn.onestravel.kotlin.base.mvp.R
import cn.onestravel.library.kotlin.mvp.activity.BaseMvpActivity

/**
 * @name cn.test.TestMainActivity
 * @description //TODO
 * @createTime 2018/12/12 17:15
 * @author onestravel
 * @version 1.0.0
 */
class TestMainActivity: BaseMvpActivity<IMainView, IMainPresenter>() {
    override fun createPresenter(): IMainPresenter {
        return MainPresenterImpl()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        super.initData()

    }

}
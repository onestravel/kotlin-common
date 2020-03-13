package cn.test

import cn.onestravel.kotlin.demo.mvp.R
import cn.onestravel.library.mvp.activity.OneMvpActivity

/**
 * @name cn.test.TestMainActivity
 * @description //TODO
 * @createTime 2018/12/12 17:15
 * @author onestravel
 * @version 1.0.0
 */
class TestMainActivity: OneMvpActivity<IMainView, IMainPresenter>() {
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
package cn.test

import cn.onestravel.library.mvp.presenter.OneMvpPresenter
import cn.onestravel.library.mvp.view.OneMvpView

/**
 * @name cn.onestravel.library.kotlin.mvp.MainContract
 * @description //TODO
 * @createTime 2018/12/12 17:14
 * @author onestravel
 * @version 1.0.0
 */

public interface IMainView : OneMvpView {

    /**
     * 测试
     */
    fun setTestContent()
}

public interface IMainPresenter : OneMvpPresenter<IMainView> {

    /**
     * 测试
     */
    fun requestTestContent()
}

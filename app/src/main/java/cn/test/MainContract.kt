package cn.test

import cn.onestravel.library.kotlin.mvp.presenter.BaseMvpPresenter
import cn.onestravel.library.kotlin.mvp.view.BaseMvpView

/**
 * @name cn.onestravel.library.kotlin.mvp.MainContract
 * @description //TODO
 * @createTime 2018/12/12 17:14
 * @author onestravel
 * @version 1.0.0
 */

public interface IMainView : BaseMvpView {

    /**
     * 测试
     */
    fun setTestContent()
}

public interface IMainPresenter : BaseMvpPresenter<IMainView> {

    /**
     * 测试
     */
    fun requestTestContent()
}

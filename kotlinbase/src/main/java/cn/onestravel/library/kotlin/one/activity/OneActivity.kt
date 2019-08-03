package cn.onestravel.library.kotlin.one.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * @name OneActivity
 * @description 所有Activity的基类
 * @createTime 2018/11/28 17:52
 * @author onestravel
 * @version 1.0.0
 */
abstract class OneActivity : AppCompatActivity(),AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initListener()
        initData()
    }


    /**
     * 获取布局ID,子类必须实现
     */
    protected abstract fun getLayoutId(): Int


    /**
     * 初始化 View 的相关操作，若有需要可在子类实现
     */
    protected open fun initView() {}

    /**
     * 初始化 Listener 事件的相关操作，若有需要可在子类实现
     */
    protected open fun initListener() {}


    /**
     * 初始化 Data 数据的相关操作，若有需要可在子类实现
     */
    protected open fun initData() {}


    /**
     * 在主线程弹出Toast 提示
     * @param msg 需要弹出的提示信息
     */
    protected open fun showToast(msg:String){
        runOnUiThread {
            toast(msg)
        }
    }

    /**
     * 在主线程弹出Toast 提示
     * @param stringRes 需要弹出的提示信息的string资源ID
     */
    protected open fun showToast(stringRes:Int){
        runOnUiThread {
            toast(getString(stringRes))
        }
    }

    /**
     * 跳转到另一个Activity，并且finish 掉当前Activity
     * 需要跳转的Activity必须继承于BaseActivity 或者
     * @param params 可变参数，需要通过intent传递的参数 eg:"key" to "value"
     */
    inline fun <reified T: OneActivity> startActivityAndFinish(vararg params: Pair<String, Any?>) {
        startActivity<T>(*params)
        finish()
    }

}
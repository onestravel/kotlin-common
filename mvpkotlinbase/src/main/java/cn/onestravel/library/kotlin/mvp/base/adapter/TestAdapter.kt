package cn.onestravel.library.kotlin.mvp.base.adapter

import android.widget.LinearLayout

/**
 * @name cn.onestravel.library.kotlin.mvp.base.adapter.TestAdapter
 * @description //TODO
 * @createTime 2018/11/29 15:26
 * @author onestravel
 * @version 1.0.0
 */
class TestAdapter(): BaseRecyclerAdapter<String>() {
    override fun getLayoutId(viewType: Int): Int {
        return 0
    }

    override fun onBindVH(holder: VH, position: Int, data: String) {
        println(data)
    }
}
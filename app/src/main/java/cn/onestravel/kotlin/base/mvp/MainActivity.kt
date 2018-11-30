package cn.onestravel.kotlin.base.mvp


import android.widget.ListView
import android.widget.TextView
import cn.onestravel.library.kotlin.mvp.base.activity.BaseListActivity
import cn.onestravel.library.kotlin.mvp.base.adapter.BaseRecyclerAdapter
import cn.onestravel.library.kotlin.mvp.base.adapter.VH

class MainActivity : BaseListActivity() {
    val adapter = TestAdapter()


    override fun initView() {
        super.initView()
        canLoadMore(true)
    }

    override fun initListener() {
        super.initListener()

        adapter.setOnItemClick { view, position, data ->
            showToast("点击了条目$position")
        }

        adapter.setOnItemLongClick { view, position, data ->
            showToast("长按了条目$position")
            return@setOnItemLongClick true
        }
    }

    override fun initData() {
        super.initData()
        setAdapter(adapter)
        addData(adapter)
        refreshComplete()
        loadMoreComplete()
    }

    private fun addData(adapter: TestAdapter, isRefresh: Boolean = false) {
        val list = ArrayList<User>()
        val url = "http://static.open-open.com/lib/uploadImg/20150716/20150716115042_431.jpg"
        (1..10).forEach {
            val user = User()
            user.name = "我是张三 $it"
            user.headUrl = url
            list.add(user)
        }
        if (isRefresh) {
            adapter.setDatas(list)
        } else {
            adapter.addDatas(list)
        }
    }

    override fun refreshDatas() {
        super.refreshDatas()
        getRecyclerView().postDelayed({
            refreshComplete()
            addData(adapter, true)
        }, 2000)
    }


    override fun loadMoreDatas() {
        super.loadMoreDatas()
        getRecyclerView().postDelayed({
            loadMoreComplete()
            addData(adapter)
        }, 2000)
    }


}


class TestAdapter() : BaseRecyclerAdapter<User>() {
    override fun onBindVH(holder: VH, position: Int, data: User) {
        holder.setText(R.id.tvName, data.name)
        holder.setImageUrl(R.id.ivHead, data.headUrl)
//        holder.setOnClick(R.id.tvName){
//            showToast(data.name)
//        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.layout_item
    }
}

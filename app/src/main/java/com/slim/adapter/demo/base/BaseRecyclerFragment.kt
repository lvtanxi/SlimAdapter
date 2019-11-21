package com.slim.adapter.demo.base

import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.slim.adapter.SlimAdapter
import com.slim.adapter.demo.R
import com.slim.http.intes.PageWidgetInterface
import com.slim.adapter.demo.helper.OnRefreshAndLoadMoreDelegate
import com.slim.adapter.demo.helper.PageDelegate


abstract class BaseRecyclerFragment : BaseFragment(), PageWidgetInterface {
    protected var refreshLayout: SmartRefreshLayout? = null
    protected var recyclerView: RecyclerView? = null
    private var isRefresh: Boolean = false
    protected var slimAdapter: SlimAdapter? = null
    private var pageDelegate = PageDelegate()
    private var refreshAndLoadMoreDelegate: OnRefreshAndLoadMoreDelegate? = null


    override fun getContentLayoutId(): Int {
        if (fra == null || fra!!.contentLayout == 0) {
            return R.layout.widget_refresh_layout
        }
        return fra!!.contentLayout
    }


    override fun initRootView() {
        super.initRootView()
        refreshLayout = contentView?.findViewById(R.id.smart_refresh_layout)
        recyclerView = contentView?.findViewById(R.id.refresh_recycler_view)
    }

    override fun initData() {
        super.initData()
        refreshLayout?.setEnableLoadMoreWhenContentNotFull(false)
        slimAdapter = obtainAdapter()
        recyclerView?.adapter = slimAdapter
        refreshAndLoadMoreDelegate = OnRefreshAndLoadMoreDelegate {
            if (it)
                pageDelegate.resetPageNo()
            isRefresh = it
            loadData()
        }
    }

    //获取
    abstract fun obtainAdapter(): SlimAdapter

    open fun renderAdapter(render: SlimAdapter.() -> Unit) {
    }

    override fun onProcessLogic() {
        fromProcess = true
        refreshAndLoadMoreDelegate?.startRefresh(refreshLayout)
    }

    override fun stopRefresh(isSuccess: Boolean) {
        fromProcess = false
        refreshLayout ?: return
        refreshAndLoadMoreDelegate?.finishRefresh(refreshLayout, isSuccess, isRefresh) {
            pageDelegate.checkLoadEnd(isSuccess)
        }
        showEmptyView(isSuccess)
    }

    override fun bindListener() {
        super.bindListener()
        refreshLayout?.setOnRefreshLoadMoreListener(refreshAndLoadMoreDelegate)
    }

    override fun showEmptyView(isSuccess: Boolean) {
        if (stateLayout == null || !isSuccess)
            return
        if (adapterIsEmpty())
            stateLayout?.showEmptyView()
        else
            stateLayout?.showContentView()
    }

    override fun showErrorView(message: CharSequence?) {
        if (adapterIsEmpty())
            super.showErrorView(message)
        else
            super.toastFail(message)
    }

    override fun toastFail(message: CharSequence?) {
        if (adapterIsEmpty())
            showErrorView(message)
        else
            super.toastFail(message)
    }

    override fun addItems(items: Any?) {
        if (items != null && items is MutableList<*>) {
            pageDelegate.lastDataSize = items.size
            slimAdapter?.addItems(items as MutableList<Any>, isRefresh)
            return
        }
        pageDelegate.lastDataSize = 0
    }


    override fun clearLoadMoreListener() {
        refreshLayout?.isEnableLoadMore = false
        refreshLayout?.setOnLoadMoreListener(null)
    }

    override fun getPageMap() = pageDelegate.getPagingParam()

    private fun adapterIsEmpty(): Boolean {
        return slimAdapter == null || slimAdapter!!.isEmpty()
    }
}
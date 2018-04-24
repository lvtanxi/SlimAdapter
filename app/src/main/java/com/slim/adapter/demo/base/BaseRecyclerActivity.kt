package com.slim.adapter.demo.base

import android.support.v7.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.slim.adapter.SlimAdapter
import com.slim.adapter.demo.R
import com.slim.adapter.demo.helper.OnRefreshAndLoadMoreDelegate
import com.slim.http.delegate.PageWidgetInterface

/**
 * Date: 2018-04-17
 * Time: 16:46
 * Description:
 */
abstract class BaseRecyclerActivity : BaseActivity(), PageWidgetInterface {
    protected var refreshLayout: SmartRefreshLayout? = null
    protected var recyclerView: RecyclerView? = null
    private var pageNo = 1
    private var pageSize = 20
    private var lastDataSize = -1
    private var slimAdapter: SlimAdapter? = null
    private var isRefresh: Boolean = false
    private var refreshAndLoadMoreDelegate: OnRefreshAndLoadMoreDelegate? = null

    override fun getContentLayoutId() = R.layout.widget_refresh_layout

    override fun initContentView() {
        super.initContentView()
        refreshLayout = findViewById(R.id.smart_refresh_layout)
        recyclerView = findViewById(R.id.refresh_recycler_view)
    }


    override fun initData() {
        super.initData()
        slimAdapter = bindAdapter().into(recyclerView)
        refreshLayout?.setEnableLoadMoreWhenContentNotFull(false)
        refreshAndLoadMoreDelegate = OnRefreshAndLoadMoreDelegate {
            if (it)
                pageNo = 1
            isRefresh = it
            loadData()
        }
    }

    abstract fun bindAdapter(): SlimAdapter

    override fun onProcessLogic() {
        super.onProcessLogic()
        fromProcess=true
        refreshAndLoadMoreDelegate?.startRefresh(refreshLayout)
    }

    override fun stopRefresh(isSuccess: Boolean) {
        fromProcess = false
        refreshLayout ?: return
        val enableLoadMore = refreshLayout?.isEnableLoadMore ?: false
        if (enableLoadMore) {
            val loadEnd = isSuccess && (lastDataSize < pageSize)
            if (!loadEnd)
                pageNo += 1
            if (isRefresh) {
                refreshLayout?.finishRefresh(0, isSuccess)
                if (loadEnd)
                    refreshLayout?.setNoMoreData(true)
            } else if (loadEnd) {
                refreshLayout?.finishLoadMoreWithNoMoreData()
            } else {
                refreshLayout?.finishLoadMore(isSuccess)
            }
        } else {
            refreshLayout?.finishRefresh(isSuccess)
        }
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

    override fun showErrorView(message: String?) {
        if (adapterIsEmpty())
            super.showErrorView(message)
        else
            super.toastFail(message)
    }

    override fun toastFail(message: String?) {
        if (adapterIsEmpty())
            showErrorView(message)
        else
            super.toastFail(message)
    }

    override fun addItems(items: Any?) {
        if (items != null && items is MutableList<*>) {
            lastDataSize = items.size
            slimAdapter?.addItems(items as MutableList<Any>, isRefresh)
            return
        }
        lastDataSize = 0
    }

    override fun clearLoadMoreListener() {
        refreshLayout?.isEnableLoadMore = false
        refreshLayout?.setOnLoadMoreListener(null)
    }

    override fun getPageMap(): Map<String, String> {
        return mapOf("pageNo" to pageNo.toString(), "pageSize" to pageSize.toString())
    }

    private fun adapterIsEmpty(): Boolean {
        return slimAdapter == null || slimAdapter!!.isEmpty()
    }

}
package com.yuxuan.common.helper

import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener


class OnRefreshAndLoadMoreDelegate(private val loadListener: (isRefresh: Boolean) -> Unit) : OnRefreshLoadMoreListener {
    override fun onRefresh(refreshlayout: RefreshLayout?) {
        if (refreshlayout != null && refreshlayout.isEnableLoadMore)
            refreshlayout.setNoMoreData(false)
        loadListener.invoke(true)
    }

    override fun onLoadMore(refreshlayout: RefreshLayout?) {
        loadListener.invoke(false)
    }

    fun startRefresh(refreshLayout: SmartRefreshLayout?) {
        if (refreshLayout != null) {
            if (refreshLayout.state == RefreshState.None) {
                refreshLayout.autoRefresh(0)
            } else {
                refreshLayout.setReboundDuration(0)
                refreshLayout.finishRefresh(0)
                refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
                    override fun onStateChanged(refreshLayout: RefreshLayout?, oldState: RefreshState?, newState: RefreshState?) {
                        if (oldState == RefreshState.RefreshFinish && newState == RefreshState.None) {
                            refreshLayout?.setOnMultiPurposeListener(null)
                            refreshLayout?.setReboundDuration(250)
                            refreshLayout?.autoRefresh(0)
                        }
                    }
                })
            }
        }
    }

    fun finishRefresh(refreshLayout: RefreshLayout?, isSuccess: Boolean, isRefresh: Boolean, block: () -> Boolean) {
        refreshLayout ?: return
        val enableLoadMore = refreshLayout.isEnableLoadMore
        if (enableLoadMore) {
            val loadEnd = block()
            if (isRefresh) {
                refreshLayout.finishRefresh(0, isSuccess)
                if (loadEnd)
                    refreshLayout.setNoMoreData(true)
            } else if (loadEnd) {
                refreshLayout.finishLoadMoreWithNoMoreData()
            } else {
                refreshLayout.finishLoadMore(isSuccess)
            }
        } else {
            refreshLayout.finishRefresh(isSuccess)
        }
    }

}
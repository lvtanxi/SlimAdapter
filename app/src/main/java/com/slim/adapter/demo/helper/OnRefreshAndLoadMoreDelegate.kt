package com.slim.adapter.demo.helper

import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
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

    fun startRefresh(refreshLayout: SmartRefreshLayout?){
        if (refreshLayout!=null){
            if (refreshLayout.state==RefreshState.None){
                refreshLayout.autoRefresh(0)
            }else{
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

}
package com.slim.adapter.demo

import com.slim.adapter.SlimAdapter
import com.slim.adapter.demo.base.BaseRecyclerFragment
import com.slim.adapter.demo.data.User
import com.slim.adapter.demo.repository.HttpRepository
import com.slim.http.delegate.http
import com.slim.http.delegate.httpPage
import kotlinx.android.synthetic.main.item_user.view.*

/**
 * Date: 2018-04-23
 * Time: 15:54
 * Description:
 */
class ListFrag : BaseRecyclerFragment() {
    override fun obtainAdapter() = SlimAdapter()
            .map<User>(R.layout.item_user) {
                onBind { itemView, t, _ ->
                    itemView.serialNumber.text = t.name
                }
                onClick { _, t, _ ->
                    toastSuccess(t.name)
                }
            }



    override fun loadData() {
        httpPage(this) { HttpRepository.getApiService().userlist(getPageMap()) }
    }
}
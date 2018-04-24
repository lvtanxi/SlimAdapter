package com.slim.adapter.demo

import com.slim.adapter.SlimAdapter
import com.slim.adapter.Type
import com.slim.adapter.demo.base.BaseRecyclerFragment
import com.slim.adapter.demo.data.User
import com.slim.http.core.slimHttp
import com.slim.http.type.GET
import kotlinx.android.synthetic.main.item_user.view.*

/**
 * Date: 2018-04-23
 * Time: 15:54
 * Description:
 */
class ListFrag :BaseRecyclerFragment(){
    override fun bindAdapter() = SlimAdapter()
            .map(Type<User>(R.layout.item_user)
                    .onBind { holder, t ->
                        holder.itemView.serialNumber.text = t.name
                    }
                    .onClick { _, t ->
                        toastSuccess(t.name)
                    }
            )


    override fun loadData() {
        slimHttp {
            httpType = GET("list")
            otherParam = getPageMap()
            params = {
                "name"("吕檀溪")
            }
        }.mapPageSub<MutableList<User>>(this)
    }
}
package com.slim.adapter.demo

import android.support.v7.widget.RecyclerView
import com.slim.adapter.SlimAdapter
import com.slim.adapter.SlimSectionAdapter
import com.slim.adapter.Type
import com.slim.adapter.demo.base.Act
import com.slim.adapter.demo.base.BaseRecyclerActivity
import com.slim.adapter.demo.data.Sd
import com.slim.adapter.demo.data.User
import com.slim.http.core.slimHttp
import com.slim.http.type.GET
import kotlinx.android.synthetic.main.item_section.view.*
import kotlinx.android.synthetic.main.item_user.view.*

/**
 * Date: 2018-04-21
 * Time: 16:49
 * Description:
 */
@Act(title = "SectionList")
class SectionActivity : BaseRecyclerActivity() {


    override fun bindAdapter() = SlimSectionAdapter()
            .map(Type<Sd>(R.layout.item_section)
                    .onBind { holder, t ->
                        holder.itemView.title.text = t.title
                    }
            )
            .map(Type<User>(R.layout.item_user)
                    .onBind { holder, t ->
                        holder.itemView.serialNumber.text = t.name
                    }
                    .onClick { holder, t ->
                        holder.itemView.serialNumber.text = t.name
                    }
            )

    override fun loadData() {
        slimHttp {
            httpType = GET("section")
            otherParam = getPageMap()
        }.mapPageSub<MutableList<Sd>>(this)
    }
}
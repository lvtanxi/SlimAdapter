package com.slim.adapter.demo


import android.widget.ImageView
import com.bumptech.glide.Glide
import com.slim.adapter.SlimAdapter
import com.slim.adapter.SlimConvert
import com.slim.adapter.Type
import com.slim.adapter.demo.base.Act
import com.slim.adapter.demo.base.BaseRecyclerActivity
import com.slim.adapter.demo.data.User
import com.slim.http.core.slimHttp
import com.slim.http.type.GET

@Act(title = "普通List")
class ListActivity : BaseRecyclerActivity() {

    override fun bindAdapter() = SlimAdapter()
            .map(Type<User>(R.layout.item_user)
                    .onBind { holder, t ->
                        SlimConvert(holder.itemView)
                                .setText(R.id.serialNumber,t.name)
                                .with<ImageView>(R.id.image,{
                                    Glide.with(it.context).load(t.image).into(it)
                                })
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

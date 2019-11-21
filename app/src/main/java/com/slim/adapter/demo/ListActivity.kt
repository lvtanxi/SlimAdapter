package com.slim.adapter.demo


import android.widget.ImageView
import com.bumptech.glide.Glide
import com.slim.adapter.SlimAdapter
import com.slim.adapter.SlimConvert
import com.slim.adapter.Type
import com.slim.adapter.demo.base.Act
import com.slim.adapter.demo.base.BaseRecyclerActivity
import com.slim.adapter.demo.data.User

@Act(title = "普通List")
class ListActivity : BaseRecyclerActivity() {

    override fun obtainAdapter() = SlimAdapter()
            .map(Type<User>(R.layout.item_user)
                    .onBind { itemView, t, _ ->
                        SlimConvert(itemView)
                                .setText(R.id.serialNumber, t.name)
                                .with<ImageView>(R.id.image) {
                                    Glide.with(it.context).load(t.image).into(it)
                                }
                    }
                    .onClick { _, t, _ ->
                        toastSuccess(t.name)
                    }
            )


    override fun loadData() {

    }

}

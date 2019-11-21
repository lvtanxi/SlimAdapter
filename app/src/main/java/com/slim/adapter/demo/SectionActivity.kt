package com.slim.adapter.demo

import com.slim.adapter.SlimSectionAdapter
import com.slim.adapter.Type
import com.slim.adapter.demo.base.Act
import com.slim.adapter.demo.base.BaseRecyclerActivity
import com.slim.adapter.demo.data.Sd
import com.slim.adapter.demo.data.User
import com.slim.adapter.demo.helper.ImageConvert
import kotlinx.android.synthetic.main.item_section.view.*

/**
 * Date: 2018-04-21
 * Time: 16:49
 * Description:
 */
@Act(title = "SectionList")
class SectionActivity : BaseRecyclerActivity() {

    override fun initData() {
        super.initData()
    }


    override fun obtainAdapter() = SlimSectionAdapter()
            .map(Type<Sd>(R.layout.item_section)
                    .onBind { itemView, t ,_->
                        itemView.title.text = t.title
                    }
                    .onClick { _, t,_ ->
                        toastSuccess(t.title)
                    }
            )
            .map(Type<User>(R.layout.item_user)
                    .onConvert { convert, t ->
                        convert.setText(R.id.serialNumber, t.name)
                                .with(R.id.image, ImageConvert(t.image))
                    }
                    .onClick { _, t,_ ->
                        toastSuccess(t.name)
                    }
            )

    override fun loadData() {
    }
}
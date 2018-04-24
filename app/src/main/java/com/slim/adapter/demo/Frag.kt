package com.slim.adapter.demo

import com.slim.adapter.demo.base.BaseFragment
import com.slim.adapter.demo.base.Fra
import com.slim.adapter.demo.data.User
import com.slim.http.core.slimHttp
import com.slim.http.type.GET
import kotlinx.android.synthetic.main.item_car.*

/**
 * Date: 2018-04-23
 * Time: 15:32
 * Description:
 */
@Fra(contentLayout = R.layout.item_car)
class Frag : BaseFragment(){

    override fun bindListener() {
        super.bindListener()
        switch2.setOnCheckedChangeListener { _, _ ->
            slimHttp {
                httpType = GET("list")
                params = {
                    "name"("吕檀溪")
                }
            }.mapPageSub<Void>(this)
        }
    }

}
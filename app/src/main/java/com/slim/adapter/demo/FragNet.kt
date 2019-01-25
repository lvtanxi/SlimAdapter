package com.slim.adapter.demo

import com.slim.adapter.demo.base.BaseFragment
import com.slim.adapter.demo.base.Fra
import com.slim.http.core.slimHttp
import com.slim.http.type.GET

/**
 * Date: 2018-04-23
 * Time: 15:32
 * Description:
 */
@Fra(contentLayout = R.layout.item_car)
class FragNet : BaseFragment(){

    override fun onProcessLogic() {
        super.onProcessLogic()
        slimHttp {
            httpType = GET("list")
            params = {
                "name"("吕檀溪")
            }
        }.mapSub<Void>(this)
    }

}
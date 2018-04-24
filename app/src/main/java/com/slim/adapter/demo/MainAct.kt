package com.slim.adapter.demo

import android.content.Intent
import com.slim.adapter.demo.R.id.*
import com.slim.adapter.demo.base.Act
import com.slim.adapter.demo.base.BaseActivity
import com.slim.http.core.slimHttp
import com.slim.http.type.GET
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_car.*

/**
 * Date: 2018-04-21
 * Time: 16:47
 * Description: 主页
 */
@Act(contentLayout = R.layout.activity_main)
class MainAct : BaseActivity() {
    override fun initData() {
        super.initData()
        hideToolbar()
    }


    override fun bindListener() {
        super.bindListener()
        toList.setOnClickListener {
            startActivity(Intent(this@MainAct, ListActivity::class.java))
        }
        toSection.setOnClickListener {
            startActivity(Intent(this@MainAct, SectionActivity::class.java))
        }
        toTab.setOnClickListener {
            startActivity(Intent(this@MainAct, TabAct::class.java))
        }
        testNet.setOnClickListener {
            slimHttp {
                httpType = GET("list")
                params = {
                    "name"("吕檀溪")
                }
            }.mapSub<Void>(this)
                    .onSuccessWithNull { testNet.text = "测试成功" }
        }
        testVoid.setOnClickListener {
            slimHttp {
                httpType = GET("list")
                params = {
                    "name"("吕檀溪")
                }
            }.mapSub<Void>(this)
                    .onSuccessWithNull { testVoid.text = "操作成功" }
        }

        testBaci.setOnClickListener {
            slimHttp {
                httpType = GET("string")
                params = {
                    "name"("吕檀溪")
                }
            }.mapSub<String>(this)
                    .onSucess { testBaci.text = it }
        }
    }

}
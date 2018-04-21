package com.slim.adapter.demo

import android.content.Intent
import com.slim.adapter.demo.base.Act
import com.slim.adapter.demo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

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
            startActivity(Intent(this@MainAct,ListActivity::class.java))
        }
        toSection.setOnClickListener {
            startActivity(Intent(this@MainAct,SectionActivity::class.java))
        }
    }

}
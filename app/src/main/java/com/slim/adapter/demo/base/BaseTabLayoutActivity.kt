package com.slim.adapter.demo.base

import android.support.v4.view.ViewPager
import android.support.design.widget.TabLayout
import com.slim.adapter.demo.R
import android.support.v4.view.PagerAdapter


/**
 * Date: 2018-04-23
 * Time: 15:26
 * Description:
 */
abstract class BaseTabLayoutActivity : BaseActivity() {
    protected var tabLayout: TabLayout? = null
    protected var tabViewPager: ViewPager? = null

    override fun getContentLayoutId()=R.layout.widget_tab_layout

    override fun initContentView() {
        super.initContentView()
        tabLayout = findViewById(R.id.tab_layout)
        tabViewPager = findViewById(R.id.tab_view_pager)
    }

    override fun initData() {
        super.initData()
        tabViewPager?.adapter = getViewPager()
        tabLayout?.setupWithViewPager(tabViewPager)
        tabViewPager?.offscreenPageLimit = tabLayout?.tabCount ?: 0
    }

    protected abstract fun getViewPager(): PagerAdapter

}
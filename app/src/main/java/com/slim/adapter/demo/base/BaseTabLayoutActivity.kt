package com.slim.adapter.demo.base

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.slim.adapter.demo.R

abstract class BaseTabLayoutActivity : BaseActivity() {
    private var tabLayout: TabLayout? = null
    private var tabViewPager: ViewPager? = null

    override fun getContentLayoutId()= R.layout.widget_tab_layout

    override fun initRootView() {
        super.initRootView()
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
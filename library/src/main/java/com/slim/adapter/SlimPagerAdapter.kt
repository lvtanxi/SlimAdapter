package com.slim.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View

/**
 * Date: 2018-04-18
 * Time: 13:26
 * Description: FragmentStatePagerAdapter
 */
class SlimPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val pagerItems = ArrayList<Fragment>()
    private val titleItems = ArrayList<CharSequence>()


    fun add(fragment: Fragment, title: CharSequence = "") = apply {
        pagerItems.add(fragment)
        titleItems.add(title)
    }


    fun remove(position: Int) {
        pagerItems.removeAt(position)
        titleItems.removeAt(position)
    }

    fun into(viewPager: ViewPager?) = apply {
        viewPager?.adapter = this@SlimPagerAdapter
    }

    override fun getCount(): Int = pagerItems.size

    override fun getItem(position: Int) = pagerItems[position]

    override fun getPageTitle(position: Int) = titleItems[position]


}

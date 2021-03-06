package com.slim.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager


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

    fun add(fragments: List<Fragment>, titles: List<CharSequence>) = apply {
        pagerItems.addAll(fragments)
        titleItems.addAll(titles)
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

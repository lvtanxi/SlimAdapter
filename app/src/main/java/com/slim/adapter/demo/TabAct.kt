package com.slim.adapter.demo

import com.slim.adapter.SlimPagerAdapter
import com.slim.adapter.demo.base.Act
import com.slim.adapter.demo.base.BaseTabLayoutActivity

/**
 * Date: 2018-04-23
 * Time: 15:29
 * Description:
 */
@Act(title = "tab")
class TabAct :BaseTabLayoutActivity(){
    override fun getViewPager()=SlimPagerAdapter(supportFragmentManager)
            .add(Frag(),"普通")
            .add(ListFrag(),"列表")
}
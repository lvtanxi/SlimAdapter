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
            .add(Frag(),"模拟提交请求")
            .add(FragNet(),"普通进入请求")
            .add(ListFrag(),"列表请求")
}
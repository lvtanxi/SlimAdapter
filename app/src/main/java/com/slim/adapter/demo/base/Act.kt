package com.slim.adapter.demo.base


/**
 * Date: 2018-02-12
 * Time: 09:55
 * Description: 注解act 的name
 */
@Target(AnnotationTarget.CLASS)
annotation class Act(
        /**
         * act的名字
         */
        val title: String = "",

        /**
         * act的名字
         */
        val rootLayout: Int=0,
        /**
         * act的名字
         */
        val contentLayout: Int = 0,
        /**
         * act的菜单可以重写onMenuItemClick
         */
        val menuResId: Int = 0)

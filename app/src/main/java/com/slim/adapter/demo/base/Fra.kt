package com.slim.adapter.demo.base


/**
 * Date: 2018-02-12
 * Time: 09:55
 * Description: 注解Frage
 */
@Target(AnnotationTarget.CLASS)
annotation class Fra(

        /**
         * act的名字
         */
        val rootLayout: Int = 0,
        /**
         * act的名字
         */
        val contentLayout: Int = 0
)
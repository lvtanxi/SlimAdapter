package com.slim.adapter.demo.base


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
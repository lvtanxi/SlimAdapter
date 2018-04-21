package com.slim.adapter.demo.data

import com.slim.adapter.Section

/**
 * Date: 2018-04-18
 * Time: 13:52
 * Description:
 */
data class Sd(val title: String, val data: MutableList<User>) : Section {
    override fun childCount() = data.size

    override fun childItem(position: Int) = data[position]

}
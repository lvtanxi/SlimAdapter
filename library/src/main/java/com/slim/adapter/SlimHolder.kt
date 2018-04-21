
package com.slim.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * 通用的ViewHolder
 */
open class SlimHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
    internal var created = false
    lateinit var data :Any
}
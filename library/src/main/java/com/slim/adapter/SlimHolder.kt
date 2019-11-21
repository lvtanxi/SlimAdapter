
package com.slim.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 通用的ViewHolder
 */
open class SlimHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
    internal var created = false
    lateinit var data :Any
}
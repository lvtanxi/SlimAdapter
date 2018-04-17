
package com.slim.adapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * 通用的ViewHolder
 */
open class Holder<out B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root) {
    internal var created = false
}

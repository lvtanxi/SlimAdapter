package com.slim.adapter

import android.databinding.ViewDataBinding
/**
 * 多布局适配
 */
open class BaseType (open val layout: Int, open val variable: Int? = null)

abstract class AbsType<B : ViewDataBinding> (layout: Int, variable: Int? = null) : BaseType(layout, variable)

open class ItemType<B : ViewDataBinding> (layout: Int, variable: Int? = null) : AbsType<B>(layout, variable) {
    open fun onCreate(holder: Holder<B>) {}
    open fun onBind(holder: Holder<B>) {}
    open fun onRecycle(holder: Holder<B>) {}
}

open class Type<B : ViewDataBinding> (layout: Int, variable: Int? = null) : AbsType<B>(layout, variable) {
    internal var onCreate: Action<B>? = null; private set
    internal var onBind: Action<B>? = null; private set
    internal var onClick: Action<B>? = null; private set
    internal var onLongClick: Action<B>? = null; private set
    internal var onRecycle: Action<B>? = null; private set
    fun onCreate(action: Action<B>?) = apply { onCreate = action }
    fun onBind(action: Action<B>?) = apply { onBind = action }
    fun onClick(action: Action<B>?) = apply { onClick = action }
    fun onLongClick(action: Action<B>?) = apply { onLongClick = action }
    fun onRecycle(action: Action<B>?) = apply { onRecycle = action }
}

typealias Action<B> = (Holder<B>) -> Unit

package com.slim.adapter


/**
 * 多布局适配
 */
open class BaseType (open val layout: Int)

abstract class AbsType<T> (layout: Int) : BaseType(layout)

open class ItemType<T>(layout: Int) : AbsType<T>(layout) {
    open fun onCreate(slimHolder: SlimHolder) {}
    open fun onBind(slimHolder: SlimHolder) {}
    open fun onRecycle(slimHolder: SlimHolder) {}
}

open class Type<T> (layout: Int) : AbsType<T>(layout) {
    internal var onCreate: Action<T>? = null; private set
    internal var onBind: Action<T>? = null; private set
    internal var onClick: Action<T>? = null; private set
    internal var onRecycle: Action<T>? = null; private set
    fun onCreate(action: Action<T>?) = apply { onCreate = action }
    fun onBind(action: Action<T>?) = apply { onBind = action }
    fun onClick(action: Action<T>?) = apply { onClick = action }
    fun onRecycle(action: Action<T>?) = apply { onRecycle = action }
}

typealias Action<T> = (holder:SlimHolder,t:T) -> Unit

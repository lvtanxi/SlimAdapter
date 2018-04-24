package com.slim.adapter


/**
 * 多布局适配
 */
open class BaseType (open val layout: Int)

abstract class AbsType<T> (layout: Int) : BaseType(layout)


open class Type<T> (layout: Int) : AbsType<T>(layout) {
    internal var onCreate: Action<T>? = null; private set
    internal var onBind: Action<T>? = null; private set
    internal var onConvert: Convert<T>? = null; private set
    internal var onClick: Action<T>? = null; private set
    internal var onRecycle: Action<T>? = null; private set
    fun onCreate(action: Action<T>?) = apply { onCreate = action }
    fun onBind(action: Action<T>?) = apply { onBind = action }
    fun onConvert(convert: Convert<T>?) = apply { onConvert = convert }
    fun onClick(action: Action<T>?) = apply { onClick = action }
    fun onRecycle(action: Action<T>?) = apply { onRecycle = action }
}

typealias Action<T> = (holder:SlimHolder,t:T) -> Unit

typealias Convert<T> = (convert:SlimConvert,t:T) -> Unit



package com.slim.adapter


/**
 * 标记接口
 */
interface Handler

/**
 * 获取Adapter的item的类型
 */
interface TypeHandler : Handler {
    fun getItemType(item: Any, position: Int): BaseType?
}

/**
 * 根据postion获取布局文件
 */
interface LayoutHandler : Handler {
    fun getItemLayout(item: Any, position: Int): Int
}

/**
 * 标记RecyclerView平缓过度的
 */
interface StableId {
    val stableId: Long
}

interface Section {
    fun childCount(): Int

    fun childItem(position: Int): Any
}

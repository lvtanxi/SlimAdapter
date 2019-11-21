package com.slim.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * 适配
 */
@Suppress("UNCHECKED_CAST")
open class SlimAdapter : ListAdapter<Any, SlimHolder>(DiffCallback()) {
    protected val list = ArrayList<Any>()
    private var recyclerView: RecyclerView? = null
    private lateinit var inflater: LayoutInflater

    private val map = mutableMapOf<Class<*>, BaseType>()
    private var layoutHandler: LayoutHandler? = null
    private var typeHandler: TypeHandler? = null


    fun <T : Any> map(clazz: Class<T>, layout: Int) = apply { map[clazz] = BaseType(layout) }

    inline fun <reified T : Any> map(layout: Int) = map(T::class.java, layout)


    fun <T : Any> map(clazz: Class<T>, type: AbsType<T>) = apply { map[clazz] = type }

    inline fun <reified T : Any> map(type: AbsType<T>) = map(T::class.java, type)


    inline fun <reified T : Any> map(layout: Int, noinline f: (Type<T>.() -> Unit)? = null) = map(T::class.java, Type<T>(layout).apply { f?.invoke(this) })

    fun handler(handler: Handler) = apply {
        when (handler) {
            is LayoutHandler -> {
                layoutHandler = handler
            }
            is TypeHandler -> typeHandler = handler
        }
    }

    inline fun layout(crossinline f: (Any, Int) -> Int) = handler(object : LayoutHandler {
        override fun getItemLayout(item: Any, position: Int) = f(item, position)
    })

    inline fun type(crossinline f: (Any, Int) -> AbsType<Any>) = handler(object : TypeHandler {
        override fun getItemType(item: Any, position: Int) = f(item, position)
    })

    fun into(recyclerView: RecyclerView?) = apply { recyclerView?.adapter = this }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlimHolder {
        val itemView = inflater.inflate(viewType, parent, false)
        return SlimHolder(itemView)
    }

    override fun onBindViewHolder(slimHolder: SlimHolder, position: Int) {
        val type = getType(position)
        if (type is AbsType<*>) {
            slimHolder.data = getItem(position)
            if (!slimHolder.created) {
                notifyCreate(slimHolder, type)
            }
            notifyBind(slimHolder, type)
        }
    }


    override fun onViewRecycled(slimHolder: SlimHolder) {
        val position = slimHolder.adapterPosition
        if (position != RecyclerView.NO_POSITION && position < list.size) {
            val type = getType(position)!!
            if (type is AbsType<*>) {
                notifyRecycle(slimHolder, type)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return if (hasStableIds()) {
            val item = getItem(position)
            if (item is StableId) {
                item.stableId
            } else {
                throw IllegalStateException("${item.javaClass.simpleName} must implement StableId interface.")
            }
        } else {
            super.getItemId(position)
        }
    }

    override fun getItem(position: Int) = list[position]

    override fun getItemCount() = list.size

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        recyclerView = rv
        inflater = LayoutInflater.from(rv.context)
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView) {
        recyclerView = null
    }

    override fun getItemViewType(position: Int) = layoutHandler?.getItemLayout(getItem(position), position)
            ?: typeHandler?.getItemType(getItem(position), position)?.layout
            ?: getType(position)?.layout
            ?: throw RuntimeException("Invalid object at position $position: ${getItem(position).javaClass}")

    private fun getType(position: Int) = typeHandler?.getItemType(getItem(position), position)
            ?: map[getItem(position).javaClass]


    private fun <T> notifyCreate(slimHolder: SlimHolder, type: AbsType<T>) {
        if (type is Type) {
            setClickListeners(slimHolder, type)
            invokeAction(type.onCreate, slimHolder)
        }
        slimHolder.created = true
    }


    private fun <T> invokeAction(action: Action<T>?, slimHolder: SlimHolder) {
        action?.invoke(slimHolder.itemView, getItemModel(slimHolder.layoutPosition),slimHolder.layoutPosition)
    }

    private fun <T> notifyBind(slimHolder: SlimHolder, type: AbsType<T>) {
        if (type is Type) {
            if (type.onBind != null)
                invokeAction(type.onBind, slimHolder)
            else if (type.onConvert != null)
                type.onConvert?.invoke(SlimConvert(slimHolder.itemView), getItemModel(slimHolder.layoutPosition))
        }
    }


    fun clearDatas() {
        list.clear()
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean {
        return list.isEmpty()
    }


    fun isLast(position: Int): Boolean {
        return list.lastIndex == position
    }

    fun <T> getItemModel(position: Int): T {
        return getItem(position) as T
    }

    fun addItems(data: MutableList<Any>?, isRefresh: Boolean) {//刷新
        if (isRefresh)
            clearDatas()
        addItems(data)
    }


    fun addItems(items: MutableList<Any>?) {
        if (items != null && !items.isEmpty()) {
            if (list.addAll(items))
                notifyDataSetChanged()
        }
    }


    private fun <T> notifyRecycle(slimHolder: SlimHolder, type: AbsType<T>) {
        if (type is Type)
            invokeAction(type.onRecycle, slimHolder)
    }


    private fun <T> setClickListeners(slimHolder: SlimHolder, type: Type<T>) {
        val onClick = type.onClick
        if (onClick != null) {
            slimHolder.itemView.setOnClickListener { onClick(slimHolder.itemView, getItemModel(slimHolder.layoutPosition),slimHolder.layoutPosition) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any) = oldItem === newItem

        override fun areContentsTheSame(oldItem: Any, newItem: Any) = oldItem == newItem
    }

}

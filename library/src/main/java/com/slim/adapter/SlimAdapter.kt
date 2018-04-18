package com.slim.adapter

import android.databinding.DataBindingUtil
import android.databinding.ObservableList
import android.databinding.OnRebindCallback
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * 适配
 */
open class SlimAdapter(private val list: List<Any>,
                  private val variable: Int? = null,
                  stableIds: Boolean = false) : RecyclerView.Adapter<SlimHolder<ViewDataBinding>>() {

    private val invalidation = Any()
    private val callback = SlimObservableCallback(this)
    private var recyclerView: RecyclerView? = null
    private lateinit var inflater: LayoutInflater

    private val map = mutableMapOf<Class<*>, BaseType>()
    private var layoutHandler: LayoutHandler? = null
    private var typeHandler: TypeHandler? = null

    init {
        setHasStableIds(stableIds)
    }

    fun <T : Any> map(clazz: Class<T>, layout: Int, variable: Int? = null) = apply { map[clazz] = BaseType(layout, variable) }

    inline fun <reified T : Any> map(layout: Int, variable: Int? = null) = map(T::class.java, layout, variable)


    fun <T : Any> map(clazz: Class<T>, type: AbsType<*>) = apply { map[clazz] = type }

    inline fun <reified T : Any> map(type: AbsType<*>) = map(T::class.java, type)

    inline fun <reified T : Any, B : ViewDataBinding> map(layout: Int,
                                                          variable: Int? = null,
                                                          noinline f: (Type<B>.() -> Unit)? = null) = map(T::class.java, Type<B>(layout, variable).apply { f?.invoke(this) })

    fun handler(handler: Handler) = apply {
        when (handler) {
            is LayoutHandler -> {
                if (variable == null) {
                    throw IllegalStateException("No variable specified in SlimAdapter constructor")
                }
                layoutHandler = handler
            }
            is TypeHandler -> typeHandler = handler
        }
    }

    inline fun layout(crossinline f: (Any, Int) -> Int) = handler(object : LayoutHandler {
        override fun getItemLayout(item: Any, position: Int) = f(item, position)
    })

    inline fun type(crossinline f: (Any, Int) -> AbsType<*>?) = handler(object : TypeHandler {
        override fun getItemType(item: Any, position: Int) = f(item, position)
    })

    fun into(recyclerView: RecyclerView?) = apply { recyclerView?.adapter = this }


    override fun onCreateViewHolder(view: ViewGroup, viewType: Int): SlimHolder<ViewDataBinding> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, view, false)
        val holder = SlimHolder(binding)
        binding.addOnRebindCallback(object : OnRebindCallback<ViewDataBinding>() {
            override fun onPreBind(binding: ViewDataBinding) = recyclerView?.isComputingLayout ?: false
            override fun onCanceled(binding: ViewDataBinding) {
                if (recyclerView?.isComputingLayout != false) {
                    return
                }
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(position, invalidation)
                }
            }
        })
        return holder
    }

    override fun onBindViewHolder(slimHolder: SlimHolder<ViewDataBinding>, position: Int) {
        val type = getType(position)!!
        slimHolder.binding.setVariable(getVariable(type), getItem(position))
        slimHolder.binding.executePendingBindings()
        @Suppress("UNCHECKED_CAST")
        if (type is AbsType<*>) {
            if (!slimHolder.created) {
                notifyCreate(slimHolder, type as AbsType<ViewDataBinding>)
            }
            notifyBind(slimHolder, type as AbsType<ViewDataBinding>)
        }
    }

    override fun onBindViewHolder(slimHolder: SlimHolder<ViewDataBinding>, position: Int, payloads: List<Any>) {
        if (isForDataBinding(payloads)) {
            slimHolder.binding.executePendingBindings()
        } else {
            super.onBindViewHolder(slimHolder, position, payloads)
        }
    }

    override fun onViewRecycled(slimHolder: SlimHolder<ViewDataBinding>) {
        val position = slimHolder.adapterPosition
        if (position != RecyclerView.NO_POSITION && position < list.size) {
            val type = getType(position)!!
            if (type is AbsType<*>) {
                @Suppress("UNCHECKED_CAST")
                notifyRecycle(slimHolder, type as AbsType<ViewDataBinding>)
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

   open fun getItem(position: Int) = list[position]

    override fun getItemCount() = list.size

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        if (recyclerView == null && list is ObservableList) {
            list.addOnListChangedCallback(callback)
        }
        recyclerView = rv
        inflater = LayoutInflater.from(rv.context)
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView) {
        if (recyclerView != null && list is ObservableList) {
            list.removeOnListChangedCallback(callback)
        }
        recyclerView = null
    }

    override fun getItemViewType(position: Int) = layoutHandler?.getItemLayout(getItem(position), position)
            ?: typeHandler?.getItemType(getItem(position), position)?.layout
            ?: getType(position)?.layout
            ?: throw RuntimeException("Invalid object at position $position: ${getItem(position).javaClass}")

    private fun getType(position: Int) = typeHandler?.getItemType(getItem(position), position)
            ?: map[getItem(position).javaClass]

    private fun getVariable(type: BaseType) = type.variable
            ?: variable
            ?: throw IllegalStateException("No variable specified for type ${type.javaClass.simpleName}")

    private fun isForDataBinding(payloads: List<Any>): Boolean {
        if (payloads.isEmpty()) {
            return false
        }
        payloads.forEach {
            if (it != invalidation) {
                return false
            }
        }
        return true
    }

    private fun notifyCreate(slimHolder: SlimHolder<ViewDataBinding>, type: AbsType<ViewDataBinding>) {
        when (type) {
            is Type -> {
                setClickListeners(slimHolder, type)
                type.onCreate?.invoke(slimHolder)
            }
            is ItemType -> type.onCreate(slimHolder)
        }
        slimHolder.created = true
    }

    private fun notifyBind(slimHolder: SlimHolder<ViewDataBinding>, type: AbsType<ViewDataBinding>) {
        when (type) {
            is Type -> type.onBind?.invoke(slimHolder)
            is ItemType -> type.onBind(slimHolder)
        }
    }

    private fun notifyRecycle(slimHolder: SlimHolder<ViewDataBinding>, type: AbsType<ViewDataBinding>) {
        when (type) {
            is Type -> type.onRecycle?.invoke(slimHolder)
            is ItemType -> type.onRecycle(slimHolder)
        }
    }


    private fun setClickListeners(slimHolder: SlimHolder<ViewDataBinding>, type: Type<ViewDataBinding>) {
        val onClick = type.onClick
        if (onClick != null) {
            slimHolder.itemView.setOnClickListener {
                onClick(slimHolder)
            }
        }
        val onLongClick = type.onLongClick
        if (onLongClick != null) {
            slimHolder.itemView.setOnLongClickListener {
                onLongClick(slimHolder)
                true
            }
        }
    }

}

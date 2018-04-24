package com.slim.adapter

import android.graphics.drawable.Drawable
import android.os.Build
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.util.TypedValue
import android.widget.Checkable


/**
 * Date: 2018-04-24
 * Time: 09:17
 * Description:
 */
class SlimConvert(private val itemView: View) {
    private val viewSparseArray = SparseArray<View>()

    interface Convert<V> {
        fun convert(v: V)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : View> findView(viewId: Int): T {
        var view = viewSparseArray.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            viewSparseArray.put(viewId, view)
        }
        return view as T
    }

    fun findTextView(viewId: Int) = findView<TextView>(viewId)

    fun findImageView(viewId: Int) = findView<ImageView>(viewId)

    fun setTag(viewId: Int, tag: Any?) = apply {
        findView<View>(viewId).tag = tag
    }

    fun setText(viewId: Int, charSequence: CharSequence?) = apply {
        findTextView(viewId).text = charSequence
    }

    fun setText(viewId: Int, res: Int) = apply {
        findTextView(viewId).setText(res)
    }

    fun setIntText(viewId: Int, text: Int) = apply {
        findTextView(viewId).text = text.toString()
    }

    fun setTextColor(viewId: Int, color: Int) = apply {
        findTextView(viewId).setTextColor(color)
    }

    fun setTextSize(viewId: Int, sp: Float) = apply {
        findTextView(viewId).setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    }

    fun setAlpha(viewId: Int, alpha: Float) = apply {
        findView<View>(viewId).alpha = alpha
    }

    fun setImage(viewId: Int, res: Int) = apply {
        findImageView(viewId).setImageResource(res)
    }

    fun setImage(viewId: Int, drawable: Drawable) = apply {
        findImageView(viewId).setImageDrawable(drawable)
    }

    fun setBackground(viewId: Int, res: Int) = apply {
        findView<View>(viewId).setBackgroundResource(res)
    }

    fun setBackground(viewId: Int, drawable: Drawable) = apply {
        val view = findView<View>(viewId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = drawable
        } else {
            view.setBackgroundDrawable(drawable)
        }
    }

    fun visibility(viewId: Int, show: Boolean = true) = apply {
        findView<View>(viewId).visibility = if (show) View.VISIBLE else View.GONE
    }

    fun invisible(viewId: Int) = apply {
        findView<View>(viewId).visibility = View.INVISIBLE
    }

    fun gone(viewId: Int) = apply {
        findView<View>(viewId).visibility = View.GONE
    }

    fun visibility(viewId: Int, visibility: Int) = apply {
        findView<View>(viewId).visibility = visibility
    }

    fun <T : View> with(viewId: Int, convert: Convert<T>) = apply {
        convert.convert(findView(viewId))
    }

    fun <T : View> with(viewId: Int, action: (view: T) -> Unit) = apply {
        action.invoke(findView(viewId))
    }

    fun enable(viewId: Int, enable: Boolean = true) = apply {
        findView<View>(viewId).isEnabled = enable
    }

    fun disable(viewId: Int) = apply {
        enable(viewId, false)
    }

    fun checked(viewId: Int, checked: Boolean = true) = apply {
        val view = findView<View>(viewId)
        if (view is Checkable)
            view.isChecked = checked
    }

    fun selected(viewId: Int, selected: Boolean = true) = apply {
        findView<View>(viewId).isSelected = selected
    }

    fun pressed(viewId: Int, pressed: Boolean = true) = apply {
        findView<View>(viewId).isPressed = pressed
    }

}
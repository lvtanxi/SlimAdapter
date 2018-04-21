package com.slim.adapter.demo.helper
import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.slim.adapter.demo.R
import com.slim.adapter.demo.util.inflate
import kotlinx.android.synthetic.main.widget_toast.view.*


class ToastUtils private constructor(context: Context) {
    private val toast: Toast = Toast(context.applicationContext)
    private val layout: View = context.inflate(R.layout.widget_toast)

    init {
        toast.view=layout
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        private var toastUtils: ToastUtils? = null
        fun getInstance(context: Context): ToastUtils {
            if (toastUtils == null)
                toastUtils = ToastUtils(context)
            return toastUtils!!
        }
    }

    fun toastError(message: CharSequence?) {
        toast(R.drawable.toast_error, message)
    }

    fun toastSuccess(message: CharSequence?) {
        toast(R.drawable.toast_success, message)
    }

    private fun toast(imageId: Int, message: CharSequence?) {
        message?.let {
            layout.toast_image.setImageResource(imageId)
            layout.toast_text.text = message
            toast.show()
        }
    }

}
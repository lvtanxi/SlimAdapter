package com.slim.adapter.demo.helper

import android.view.View
import com.slim.http.delegate.WidgetInterface
import java.lang.ref.WeakReference

/**
 * Date: 2018-04-17
 * Time: 15:25
 * Description:
 */
class StateLayoutDelegate(weakReference: WidgetInterface) : View.OnClickListener {
    private val weakReference = WeakReference<WidgetInterface>(weakReference)

    override fun onClick(v: View?) {
        weakReference.get()?.showContentView()
        weakReference.get()?.onProcessLogic()
    }
}
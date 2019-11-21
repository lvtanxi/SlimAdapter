package com.slim.adapter.demo.base
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lvtanxi.layout.FadeViewAnimProvider
import com.lvtanxi.layout.StateLayout
import com.slim.adapter.demo.R
import com.slim.adapter.demo.helper.LoadingDialog
import com.slim.adapter.demo.helper.StateLayoutDelegate
import com.slim.adapter.demo.helper.ToastUtils
import com.slim.adapter.demo.util.X
import com.slim.http.intes.WidgetInterface


abstract class BaseFragment : Fragment(), WidgetInterface {
    protected var contentView: View? = null
    private var rootView: View? = null
    private var hasCreateView = false
    private var isFragmentVisible = false
    private var needProcess = true
    protected var stateLayout: StateLayout? = null
    private var loadingDialog: LoadingDialog? = null
    protected var fra: Fra? = null
    protected var fromProcess=false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fra = javaClass.getAnnotation(Fra::class.java)
        initFragment(inflater)
        hasCreateView = true
        return rootView
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            onFragmentVisibleChange(true)
            isFragmentVisible = true
            return
        }
        if (isFragmentVisible) {
            onFragmentVisibleChange(false)
            isFragmentVisible = false
        }
    }

    private fun onFragmentVisibleChange(change: Boolean) {
        if (change && needProcess && hasCreateView) {
            needProcess = false
            onProcessLogic()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRootData()
        initData()
        bindListener()
        if (hasCreateView && (isFragmentVisible || userVisibleHint)) {
            onFragmentVisibleChange(true)
            isFragmentVisible = true
        }
    }

    private fun initFragment(inflater: LayoutInflater?) {
        rootView = inflater?.inflate(getRootLayoutId(), null)
        stateLayout = rootView?.findViewById(R.id.state_layout)
        contentView = inflater?.inflate(getContentLayoutId(), null)
        initRootView()
    }


    override fun getRootLayoutId(): Int {
        if (fra == null || fra!!.rootLayout == 0)
            return R.layout.fragment_base
        return fra!!.rootLayout
    }

    override fun getContentLayoutId(): Int {
        if (fra != null || fra!!.contentLayout != 0)
            return fra!!.contentLayout
        throw IllegalArgumentException("ContentLayout not exist")
    }

    override fun initRootView() {
        stateLayout?.addView(contentView)
    }


    override fun initRootData() {
        stateLayout?.setViewSwitchAnimProvider(FadeViewAnimProvider())
    }

    override fun initData() {
    }

    override fun bindListener() {
        val stateLayoutDelegate = StateLayoutDelegate(this)
        stateLayout?.setErrorAction(stateLayoutDelegate)
        stateLayout?.setEmptyAction(stateLayoutDelegate)
    }
    override fun onProcessLogic() {

    }

    override fun showLoadingView() {
        if (loadingDialog == null)
            loadingDialog = LoadingDialog(activity!!)
        loadingDialog?.show()
    }

    override fun hideLoadingView() {
        loadingDialog?.dismiss()
        fromProcess = false
    }

    override fun toastSuccess(message: CharSequence?) {
        ToastUtils.getInstance(activity!!).toastSuccess(message)
    }

    override fun toastFail(message: CharSequence?) {
        ToastUtils.getInstance(activity!!).toastError(message)
    }

    override fun showErrorView(message: CharSequence?) {
        if (fromProcess)
            if (X.isNetworkConnected(activity!!))
                stateLayout?.showErrorView(message?.toString())
            else
                stateLayout?.showNetWorkError()
        else
            ToastUtils.getInstance(activity!!).toastError(message)
    }

    override fun showContentView() {
        stateLayout?.showContentView()
        fromProcess=true
    }


    override fun onDestroyView() {
        needProcess = true
        hasCreateView = false
        super.onDestroyView()
    }
}
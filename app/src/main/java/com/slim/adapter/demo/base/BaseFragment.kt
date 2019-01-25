package com.slim.adapter.demo.base
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lvtanxi.layout.FadeViewAnimProvider
import com.lvtanxi.layout.StateLayout
import com.slim.adapter.demo.R
import com.slim.adapter.demo.helper.LoadingDialog
import com.slim.adapter.demo.helper.StateLayoutDelegate
import com.slim.adapter.demo.helper.ToastUtils
import com.slim.adapter.demo.util.X
import com.slim.http.delegate.WidgetInterface
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


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
    private var compositeDisposable: CompositeDisposable? = null

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
            if (!fromProcess)
                fromProcess = compositeDisposable != null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContentData()
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
        initContentView()
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

    override fun initContentView() {
        stateLayout?.addView(contentView)
    }


    override fun initContentData() {
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

    override fun toastSuccess(message: String?) {
        ToastUtils.getInstance(activity!!).toastSuccess(message)
    }

    override fun toastFail(message: String?) {
        ToastUtils.getInstance(activity!!).toastError(message)
    }

    override fun showErrorView(message: String?) {
        if (fromProcess)
            if (X.isNetworkConnected(activity!!))
                stateLayout?.showErrorView(message)
            else
                stateLayout?.showNetWorkError()
        else
            ToastUtils.getInstance(activity!!).toastError(message)
    }

    override fun showContentView() {
        stateLayout?.showContentView()
        fromProcess=true
    }

    override fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null)
            compositeDisposable = CompositeDisposable()
        compositeDisposable?.add(disposable)
    }


    override fun onDestroyView() {
        needProcess = true
        hasCreateView = false
        super.onDestroyView()
    }

    override fun onDestroy() {
        compositeDisposable?.dispose()
        compositeDisposable = null
        super.onDestroy()
    }
}
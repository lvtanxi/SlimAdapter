package com.slim.adapter.demo.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
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

/**
 * Date: 2018-04-13
 * Time: 18:07
 * Description:
 */
abstract class BaseActivity : AppCompatActivity(), WidgetInterface {
    private var compositeDisposable: CompositeDisposable? = null
    protected var statusView: View? = null
    protected var stateLayout: StateLayout? = null
    private var act: Act? = null
    private var menuResId: Int = 0
    protected var fromProcess = false
    private var toolbarView: Toolbar? = null
    private var titleView: TextView? = null
    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = javaClass.getAnnotation(Act::class.java)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(getRootLayoutId())
        initContentView()
        initContentData()
        initData()
        bindListener()
        onProcessLogic()
        if (!fromProcess)
            fromProcess = compositeDisposable != null
    }

    override fun getRootLayoutId(): Int {
        if (act == null || act!!.rootLayout == 0)
            return R.layout.widget_root_view
        return act!!.rootLayout
    }

    override fun getContentLayoutId(): Int {
        if (act != null || act!!.contentLayout != 0)
            return act!!.contentLayout
        throw IllegalArgumentException("ContentLayout not exist")
    }

    override fun initContentView() {
        toolbarView = findViewById(R.id.toolbar)
        titleView = findViewById(R.id.toolbar_title)
        stateLayout = findViewById(R.id.state_layout)
        statusView = LayoutInflater.from(this).inflate(getContentLayoutId(), null)
        stateLayout?.addView(statusView)
    }

    override fun initContentData() {
        toolbarView?.let { setSupportActionBar(toolbarView) }
        stateLayout?.setViewSwitchAnimProvider(FadeViewAnimProvider())
        act?.let {
            setTitleStr(act!!.title)
            menuResId = act!!.menuResId
        }
    }

    protected fun setTitleStr(titleStr: CharSequence?) {
        titleView?.text = titleStr
    }


    override fun initData() {
    }

    override fun bindListener() {
        stateLayout?.setErrorAndEmptyAction(StateLayoutDelegate(this))
    }

    override fun onProcessLogic() {
    }

    override fun showLoadingView() {
        if (loadingDialog == null)
            loadingDialog = LoadingDialog(this)
        loadingDialog?.show()
    }

    override fun hideLoadingView() {
        loadingDialog?.dismiss()
    }

    override fun toastSuccess(message: String?) {
        ToastUtils.getInstance(this).toastSuccess(message)
    }

    override fun toastFail(message: String?) {
        ToastUtils.getInstance(this).toastError(message)
    }

    override fun showErrorView(message: String?) {
        if (fromProcess)
            if (X.isNetworkConnected(this))
                stateLayout?.showErrorView(message)
            else
                stateLayout?.showNetWorkError()
        else
            ToastUtils.getInstance(this).toastError(message)
    }

    protected fun hideToolbar() {
        supportActionBar?.hide()
    }

    override fun showContentView() {
        stateLayout?.showContentView()
        fromProcess = true
    }

    override fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null)
            compositeDisposable = CompositeDisposable()
        compositeDisposable?.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable?.dispose()
        compositeDisposable = null
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        } else if (menuResId != 0) {
            onMenuItemClick(item)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onMenuItemClick(item: MenuItem?) {

    }


}
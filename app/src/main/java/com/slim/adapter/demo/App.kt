package com.slim.adapter.demo

import android.app.Application
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.slim.adapter.demo.helper.ProtocolInterceptor
import com.slim.http.core.slimClient
import java.util.concurrent.TimeUnit

/**
 * Date: 2018-04-13
 * Time: 11:17
 * Description:
 */
class App :Application() {
    override fun onCreate() {
        super.onCreate()
        slimClient {
            timeUnit = TimeUnit.SECONDS
            connectTimeout = 10
            readTimeout = 10
            writeTimeout = 10
            baseUrl = "http://10.12.194.93:8080/"
            interceptors(ProtocolInterceptor())
            retryOnConnectionFailure = true
        }
    }

    companion object {
        //static 代码段可以防止内存泄露
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)//全局设置主题颜色
                ClassicsHeader(context)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
                //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter(context).setDrawableSize(20f)
            }
        }
    }
}
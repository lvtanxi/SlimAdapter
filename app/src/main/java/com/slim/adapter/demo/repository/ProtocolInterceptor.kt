package com.slim.adapter.demo.repository

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException

/**
 * Date: 2017-06-21
 * Time: 15:30
 * Description:
 */
class ProtocolInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val medizType = MediaType.parse("application/json; chartset='utf-8'")
        //判断请求是否成功了，然后再根据业务处理
        if (response.isSuccessful) {
            val data = parseDataFromBody(response.body()?.string())
            return response.newBuilder().body(ResponseBody.create(medizType, data)).build()

        }
        throw  IOException("网络请求异常,请稍后重试!")
    }

    private fun parseDataFromBody(body: String?): String {
        if (body == null) {
            throw  IOException("未获取到数据,请稍后重试!")
        }
        try {
            val json = JSONObject(body)
            val code = json.optInt("code", -1)
            if (code == 0)
                return json.optString("data")
            throw IOException(json.optString("msg"))
        } catch (e: Exception) {
            throw  IOException("数据解析错误,请稍后重试!")
        }
    }

}
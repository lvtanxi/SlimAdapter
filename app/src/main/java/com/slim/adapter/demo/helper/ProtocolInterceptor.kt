package com.slim.adapter.demo.helper

import android.util.SparseArray
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
    override fun intercept(chain: Interceptor.Chain?): Response {
        chain ?: throw RuntimeException("this Interceptor.Chain is empty")
        val response = chain.proceed(chain.request())
        val medizType = MediaType.parse("application/json; chartset='utf-8'")
        //判断请求是否成功了，然后再根据业务处理
        if (response.isSuccessful) {
            val data = parseDataFromBody(response.body()?.string())
            return response.newBuilder().body(ResponseBody.create(medizType, data)).build()

        }
        throw  IOException(errorMessage.get(response.code(), "请求失败请稍候再试！"));
    }

    private fun parseDataFromBody(body: String?): String {
        try {
            val json = JSONObject(body)
            val code = json.optInt("code", 0)
            if (code == 200)
                return json.optString("data")
            throw IOException(json.optString("msg"))
        } catch (e: Exception) {
            throw  IOException("数据解析错误,请稍后重试!")
        }
    }
    companion object {
        private val errorMessage = object : SparseArray<String>() {
            init {
                put(404, "无效的URL路径")
                put(408, "请求超时")
                put(415, "请求到不支持的媒体类型")
                put(500, "服务器遇到错误，无法完成请求")
                put(501, "服务器不具备完成请求的功能")
                put(502, "错误网关")
                put(503, "服务器目前无法使用")
                put(504, "网关超时")
            }
        }
    }
}
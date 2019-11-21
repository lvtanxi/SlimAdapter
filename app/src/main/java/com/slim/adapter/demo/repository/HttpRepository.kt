package com.slim.adapter.demo.repository

import com.google.gson.GsonBuilder
import com.slim.http.delegate.CoroutineAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object HttpRepository {
     fun getApiService(): Api {
        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(ProtocolInterceptor())
        return Retrofit.Builder()
                .baseUrl("http://10.12.204.159:8060/app/")
                .client(clientBuilder.build())
                .addCallAdapterFactory(CoroutineAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(Api::class.java)
    }


}


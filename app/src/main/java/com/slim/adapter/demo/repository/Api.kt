package com.slim.adapter.demo.repository

import com.slim.adapter.demo.data.User
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface Api {

    @GET("list")
    fun userlist(@QueryMap param: Map<String, String>): Deferred<MutableList<User>>

    @GET("notjoin")
    fun notJoin(): Deferred<Any>
}
package com.example.carcommander

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/user/signup")
    fun postRequestRegister(@Body body: Map<String, String>): Call<ResponseBody>
    @POST("/user/login")
    fun postRequestLogin(@Body body: Map<String, String>): Call<ResponseBody>
}

package com.harshul.workmanager.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

interface FileApi {

    @GET("Qb3y158/wallpaper.jpg")
    suspend fun downloadImage(): Response<ResponseBody>

    companion object {
        val instance by lazy {
            Retrofit.Builder()
                .baseUrl("https://i.ibb.co/")
                .build()
                .create(FileApi::class.java)
        }
    }
}
package com.multitv.eventbuilder.retrofit

import com.multitv.eventbuilder.apimethods.AllApiMethods
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

 /*   private val okHttpBuilder = UnsafeOkHttpClient.unsafeOkHttpClient.newBuilder()*/
    private val BASE_URl = "https://vapi.multitvsolution.com/msvc/"

    /*init {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        okHttpBuilder.protocols(listOf(Protocol.HTTP_1_1))
            .addInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
    }*/


    fun getOkHttpClient() : OkHttpClient{

        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(60,TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .build()
    }

   fun getRetrofit() : AllApiMethods {
        return Retrofit.Builder()
            .baseUrl(BASE_URl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
        /*    .client(okHttpBuilder.build())*/
            .build().create(AllApiMethods::class.java)

   }
}
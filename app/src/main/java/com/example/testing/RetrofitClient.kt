package com.example.testing

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        val BASE_URL = "https://nodered.kuntotekniikka.fi/"
        var retrofit: Retrofit? = null;
        var token: String? = null
        fun getRetrofitObject(): Retrofit? {
            if (retrofit == null) {
                synchronized(RetrofitClient::class.java) {
                    retrofit = Retrofit.Builder()
                        .client(OkHttpClient.Builder().addInterceptor { chain ->
                            val request = chain.request().newBuilder().addHeader("X-Authorization", "Bearer $token").build()
                            chain.proceed(request)
                        }.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build()
                }
            }

            return retrofit
        }

    }
}
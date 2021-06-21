package com.example.testing

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("rolloApp-image")
    suspend fun postImage(
        @Part file: MultipartBody.Part,
        @Part("Kuvaus") Kuvaus: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody
        ): ImgIdResponse

    @FormUrlEncoded
    @POST("rolloApp-login")
    suspend fun login(
        @Field(value = "email")  email: String,
        @Field(value = "password") password: String
    ): TokenResponse


    @POST("https://www.infraweb-rws.fi/api/v1/{deviceToken}/telemetry")
    suspend fun saveDeviceAttributes(
        @Path("deviceToken", encoded = true) deviceToken: String,
        @Body desc: JsonObject,
    )
}
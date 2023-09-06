package com.example.sciflare.network

import com.example.sciflare.response.UserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface API {

    @GET("prasanth")
    suspend fun getPictures(): Response<UserResponse>

    @POST("prasanth")
    suspend fun loginUser(@Body body:RequestBodies.LoginBody): Response<UserResponse>

    @DELETE("prasanth/{id}")
    suspend fun deleteItem(@Path ("id") deleteId: String): Response<UserResponse>

    @DELETE("prasanth/{id}")
    fun deletePost(@Path("id") id: String): Response<UserResponse>

}
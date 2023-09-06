package com.example.sciflare.repository

import com.example.sciflare.network.RequestBodies
import com.example.sciflare.network.RetrofitInstance
import retrofit2.Call


class UserRepository {

    suspend fun getPictures() = RetrofitInstance.picsumApi.getPictures()

    suspend fun loginUser(body: RequestBodies.LoginBody) = RetrofitInstance.picsumApi.loginUser(body)

    suspend fun deleteItem(deleteId: String) = RetrofitInstance.picsumApi.deleteItem(deleteId)
}
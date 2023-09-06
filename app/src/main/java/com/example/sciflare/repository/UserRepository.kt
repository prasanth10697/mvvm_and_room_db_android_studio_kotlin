package com.example.sciflare.repository

import com.example.sciflare.database.UserDetailsDao
import com.example.sciflare.database.UserDetailsEntity
import com.example.sciflare.network.RequestBodies
import com.example.sciflare.network.RetrofitInstance
import com.example.sciflare.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response


class UserRepository(private val userDetailDao: UserDetailsDao) {

    suspend fun getPictures() = RetrofitInstance.userDetailsApi.getUserDetails()

    suspend fun loginUser(body: RequestBodies.LoginBody) = RetrofitInstance.userDetailsApi.loginUser(body)

    suspend fun deleteItem(deleteId: String)    = RetrofitInstance.userDetailsApi.deleteItem(deleteId)

    suspend fun fetchTheUserDetails() {
        withContext(Dispatchers.IO) {
            try {
                val response: Response<UserResponse> = RetrofitInstance.userDetailsApi.getUserDetails()

                if (response.isSuccessful) {
                    val userDetailsStories = response.body()
                    val storyEntities = userDetailsStories?.map { story ->
                        UserDetailsEntity(story._id.toInt(), story.gender, story.email,story.name)
                    }
                    storyEntities?.let {
                        userDetailDao.insertStories(it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    suspend fun getAllUserDetailsDatabase(): List<UserDetailsEntity> {
        return userDetailDao.getAllStories()
    }
}
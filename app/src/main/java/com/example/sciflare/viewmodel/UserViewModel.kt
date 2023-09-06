package com.example.sciflare.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sciflare.network.RequestBodies
import com.example.sciflare.response.UserResponse
import com.example.sciflare.repository.UserRepository
import com.example.sciflare.util.Event
import com.example.sciflare.util.Resource
import com.example.sciflare.util.Utils.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class UserViewModel(
    app: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(app) {

    private val postLiveResponse = MutableLiveData<Event<Resource<UserResponse>>>()
    val postResponse: LiveData<Event<Resource<UserResponse>>> = postLiveResponse

    private val deleteLiveResponse = MutableLiveData<Event<Resource<UserResponse>>>()
    val deleteResponse: LiveData<Event<Resource<UserResponse>>> = deleteLiveResponse

    val picsData: MutableLiveData<Resource<UserResponse>> = MutableLiveData()

    init {
        getUserDetails()
    }

    private fun getUserDetails() = viewModelScope.launch {
        fetchUserDetails()
        userRepository.fetchTheUserDetails()
    }

    fun addUser(body: RequestBodies.LoginBody) = viewModelScope.launch {
        userDetails(body)
    }

    fun deleteUser(deleteId: String) = viewModelScope.launch {
        delete(deleteId)
    }


    private suspend fun fetchUserDetails() {
        picsData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(getApplication())) {
                val response = userRepository.getPictures()
                picsData.postValue(handlePicsResponse(response))
            } else {
                picsData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> picsData.postValue(
                    Resource.Error(
                        "Network Failure"
                    )
                )
                else -> picsData.postValue(
                    Resource.Error(
                        "Conversion Error"
                    )
                )
            }
        }
    }

    private fun handlePicsResponse(response: Response<UserResponse>): Resource<UserResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private suspend fun userDetails(body: RequestBodies.LoginBody) {
        postLiveResponse.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection(getApplication())) {
                val response = userRepository.loginUser(body)
                postLiveResponse.postValue(handlePost(response))
            } else {
                postLiveResponse.postValue(Event(Resource.Error("No internet connection")))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    postLiveResponse.postValue(
                        Event(Resource.Error(
                            "Network Failure"
                        ))
                    )
                }
                else -> {
                    postLiveResponse.postValue(
                        Event(Resource.Error(
                            "Conversion Error"
                        ))
                    )
                }
            }
        }
    }



    private fun handlePost(response: Response<UserResponse>): Event<Resource<UserResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }


    private suspend fun delete(deleteId: String) {
        deleteLiveResponse.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection(getApplication())) {
                val response = userRepository.deleteItem(deleteId)
                deleteLiveResponse.postValue(handlePost(response))
            } else {
                deleteLiveResponse.postValue(Event(Resource.Error("No internet connection")))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    deleteLiveResponse.postValue(
                        Event(Resource.Error(
                            "Network Failure"
                        ))
                    )
                }
                else -> {
                    deleteLiveResponse.postValue(
                        Event(Resource.Error(
                            "Conversion Error"
                        ))
                    )
                }
            }
        }
    }

}
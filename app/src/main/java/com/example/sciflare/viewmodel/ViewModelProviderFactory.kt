package com.example.sciflare.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sciflare.repository.UserRepository

class ViewModelProviderFactory(
    val app: Application,
    val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(app, userRepository) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }

}
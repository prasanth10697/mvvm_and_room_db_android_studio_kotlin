package com.example.sciflare.util

open class Event<out T>(private val content: T) {

    private var hasBeenHandled = false

    fun getContentIfNotHandled() = if(hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }
}

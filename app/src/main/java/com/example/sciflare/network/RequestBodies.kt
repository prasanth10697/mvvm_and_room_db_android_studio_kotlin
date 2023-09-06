package com.example.sciflare.network

object RequestBodies {

    data class LoginBody(
        val gender:String,
        val email: String,
        val name: String
    )

    data class DeleteBody(
        val id:String,
    )

}
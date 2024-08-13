package com.example.annaclinic.domain.model

data class User(
    val userId: String? = "",
    val email: String? = "",
    val password: String? = "",
    val accountType: String? = "",
    val name: String? = "",
)

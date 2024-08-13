package com.example.annaclinic.domain.model

data class Reservation(
    var id: String = "",
    val name: String,
    val email: String,
    val phone: String,
    val service: String,
    val price: String,
    var queue: Int,
    val description: String,
    val date: String,
    val product: String? = null,
    val totalProductPrice: String? = null,
    val totalPrice: String,
)

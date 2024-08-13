package com.example.annaclinic.core.utils

sealed class Response<out T> {
    data object Loading: Response<Nothing>()

    data class Success<out T>(
        val data: T
    ): Response<T>()

    data class Empty(
        val msg: String?
    ): Response<Nothing>()

    data class Failure(
        val msg: String?
    ): Response<Nothing>()
}
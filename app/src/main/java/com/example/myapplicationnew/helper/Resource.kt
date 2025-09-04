package com.example.myapplicationnew.helper


sealed class Resource<out T> {
    data class Success<T>(val data: T): Resource<T>()
    data class Error(val message: String, val cause: Throwable? = null): Resource<Nothing>()
    data object Loading: Resource<Nothing>()
}

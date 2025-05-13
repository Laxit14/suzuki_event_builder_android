package com.multitv.eventbuilder.sealed

 sealed class Generic<out T> {
     data class Success<T>(val data : T) : Generic<T>()
     data class Error<T>(val message: String) : Generic<T>()
     data class Failure<T>(val exception : Throwable) : Generic<T>()
     object Loading : Generic<Nothing>()
}
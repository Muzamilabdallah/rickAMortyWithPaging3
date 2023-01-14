package com.muzamil.rickamortywithpaging3.presentation.model

sealed class ViewState<T>(
    val value: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ViewState<T>(data)
    class Error<T>(message: String?, data: T? = null) : ViewState<T>(data, message)
    class Loading<T> : ViewState<T>()
}
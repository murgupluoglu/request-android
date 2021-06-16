package com.murgupluoglu.request

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Mustafa Ürgüplüoğlu on 06.09.2019.
 * Copyright © 2019 Mustafa Ürgüplüoğlu. All rights reserved.
 */

sealed class Request<out T> {
    class Success<T>(val data: T, val isFromCache: Boolean = false) : Request<T>()
    class Error(val error: RequestError) : Request<Nothing>()
    class Loading(val isLoading: Boolean) : Request<Nothing>()
    object Empty : Request<Nothing>()
}

data class RequestError(
    var code: Int = -1,
    var message: String = "",
    var body: ResponseBody? = null
)

enum class RequestErrors {
    Unknown,
    UnknownHostException,
    SocketException,
    SocketTimeoutException,
}

interface CacheListener {
    fun getCachedResponse(): Any
}

@Suppress("UNCHECKED_CAST")
fun <T> requestFlow(
    suspendFun: suspend () -> T
): Flow<Request<T>> {
    return requestFlow(suspendFun, null, null)
}

@Suppress("UNCHECKED_CAST")
fun <T> requestFlow(
    suspendFun: suspend () -> T,
    modifyFun: ((Request<T>) -> Request<T>)? = null,
    cacheListener: CacheListener? = null,
): Flow<Request<T>> {
    return flow {

        if (cacheListener != null) {
            delay(100)
            val cacheValue = cacheListener.getCachedResponse()
            emit(Request.Success(cacheValue as T, isFromCache = true))
        }

        try {
            val result = suspendFun()
            if (modifyFun != null) {
                val modified = modifyFun(Request.Success(result))
                emit(modified)
            } else {
                emit(Request.Success(result))
            }
        } catch (e: Exception) {
            var code = RequestErrors.Unknown.ordinal
            var message = "Unknown Error"
            when (e) {
                is HttpException -> {
                    code = e.code()
                    message = e.message()
                }
                is UnknownHostException -> {
                    code = RequestErrors.UnknownHostException.ordinal
                    message = e.message.toString()
                }
                is SocketException -> {
                    code = RequestErrors.SocketException.ordinal
                    message = e.message.toString()
                }
                is SocketTimeoutException -> {
                    code = RequestErrors.SocketTimeoutException.ordinal
                    message = e.message.toString()
                }
            }
            val apiError = RequestError(code = code, message = message)
            if (modifyFun != null) {
                val modified = modifyFun(Request.Error(apiError))
                emit(modified)
            } else {
                emit(Request.Error(apiError))
            }
        }
    }.onStart {
        emit(Request.Loading(true))
    }.onCompletion {
        emit(Request.Loading(false))
    }
}

@Suppress("UNCHECKED_CAST")
fun <T, R> requestTransformFlow(
    suspendFun: suspend () -> T,
    transform: ((T) -> R),
    cacheListener: CacheListener? = null,
): Flow<Request<R>> {
    return flow {

        if (cacheListener != null) {
            delay(100)
            val cacheValue = cacheListener.getCachedResponse()
            emit(Request.Success(cacheValue as R, isFromCache = true))
        }

        try {
            val result = suspendFun()
            val modified = transform(result)
            emit(Request.Success(modified))
        } catch (e: Exception) {
            var code = RequestErrors.Unknown.ordinal
            var message = "Unknown Error"
            when (e) {
                is HttpException -> {
                    code = e.code()
                    message = e.message()
                }
                is UnknownHostException -> {
                    code = RequestErrors.UnknownHostException.ordinal
                    message = e.message.toString()
                }
                is SocketException -> {
                    code = RequestErrors.SocketException.ordinal
                    message = e.message.toString()
                }
                is SocketTimeoutException -> {
                    code = RequestErrors.SocketTimeoutException.ordinal
                    message = e.message.toString()
                }
            }
            val apiError = RequestError(code = code, message = message)
            emit(Request.Error(apiError))
        }
    }.onStart {
        emit(Request.Loading(true))
    }.onCompletion {
        emit(Request.Loading(false))
    }
}
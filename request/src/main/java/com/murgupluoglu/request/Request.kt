package com.murgupluoglu.request

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import okhttp3.ResponseBody
import retrofit2.HttpException
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

interface CacheListener {
    fun getCachedResponse(): Any
}

@Suppress("UNCHECKED_CAST")
fun <T> requestFlow(
    suspendfun: suspend () -> T
): Flow<Request<T>> {
    return requestFlow(suspendfun, null)
}

@Suppress("UNCHECKED_CAST")
fun <T> requestFlow(
    suspendfun: suspend () -> T,
    cacheListener: CacheListener? = null
): Flow<Request<T>> {
    return flow {

        if (cacheListener != null) {
            delay(100)
            val cacheValue = cacheListener.getCachedResponse()
            emit(Request.Success(cacheValue as T, isFromCache = true))
        }

        try {
            val result = suspendfun()
            emit(Request.Success(result))
        } catch (httpE: HttpException) {
            val apiError = RequestError(
                code = httpE.code(),
                message = httpE.message()
            )
            emit(Request.Error(apiError))
        } catch (unknownHostE: UnknownHostException) {
            val apiError = RequestError(
                code = -1,
                message = unknownHostE.message.toString()
            )
            emit(Request.Error(apiError))
        } catch (e: Exception) {
            val apiError = RequestError(
                code = -2,
                message = e.message.toString()
            )
            emit(Request.Error(apiError))
        }
    }.onStart {
        emit(Request.Loading(true))
    }.onCompletion {
        emit(Request.Loading(false))
    }
}
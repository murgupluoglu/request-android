package com.murgupluoglu.request

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.UnknownHostException

/**
 * Created by Mustafa Ürgüplüoğlu on 06.09.2019.
 * Copyright © 2019 Mustafa Ürgüplüoğlu. All rights reserved.
 */

const val STATUS_LOADING = 1
const val STATUS_SUCCESS = 2
const val STATUS_ERROR = 3

data class RESPONSE<T>(
    var status: Int = STATUS_LOADING,
    var errorCode: Int = -1,
    var errorMessage: String = "",
    var errorBody: ResponseBody? = null,
    var isFromCache: Boolean = false,
    var responseObject: T? = null
)

interface CacheListener{
    fun getCachedResponse() : Any
}

@Suppress("UNCHECKED_CAST")
fun <T> MutableLiveData<RESPONSE<T>>.request(viewModelScope : CoroutineScope, suspendfun: suspend () -> T, cacheListener: CacheListener? = null) {

    viewModelScope.launch {

        val response = RESPONSE<T>()
        response.status = STATUS_LOADING
        this@request.value = response

        if(cacheListener != null){
            val cacheValue = cacheListener.getCachedResponse()
            response.status = STATUS_SUCCESS
            response.isFromCache = true
            response.responseObject =  cacheValue as T
            this@request.value = response
        }


        try {
            val result = suspendfun()
            response.status = STATUS_SUCCESS
            response.responseObject = result
            response.isFromCache = false
        } catch (httpE: HttpException) {
            response.status = STATUS_ERROR
            response.errorMessage = httpE.message()
            response.errorCode = httpE.code()
            response.errorBody = httpE.response()?.errorBody()
        } catch (unknownHostE: UnknownHostException) {
            response.status = STATUS_ERROR
            response.errorMessage = unknownHostE.message.toString()
        } catch (e : Exception){
            response.status = STATUS_ERROR
            response.errorMessage = e.message.toString()
        }

        this@request.value = response
    }
}

fun <T> MutableLiveData<RESPONSE<T>>.request(suspendfun: suspend () -> T, cacheListener: CacheListener? = null): Job {

    val job = Job()
    val scope = CoroutineScope(job + Dispatchers.Main)

    request(scope, suspendfun, cacheListener)

    return job
}
package com.murgupluoglu.requestsample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.murgupluoglu.request.CacheListener
import com.murgupluoglu.request.Request
import com.murgupluoglu.request.requestFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Mustafa Ürgüplüoğlu on 06.09.2019.
 * Copyright © 2019 Mustafa Ürgüplüoğlu. All rights reserved.
 */

class MainViewModel : ViewModel() {

    private val _peoplesResponse = MutableStateFlow<Request<PeopleResponse>>(Request.Empty)
    val peoplesResponse: StateFlow<Request<PeopleResponse>> get() = _peoplesResponse

    //Use dependency injection in real apps
    val networkModule = NetworkModule()

    fun getPeoples() {
        viewModelScope.launch {
            requestFlow { networkModule.service().getPeoples(1) }.collect { response ->
                _peoplesResponse.value = response
            }
        }
    }

    fun getPeoplesWithCache() {
        viewModelScope.launch {
            requestFlow(
                { networkModule.service().getPeoples(1) },
                cacheListener = object : CacheListener {
                    override fun getCachedResponse(): Any {
                        return PeopleResponse()
                    }
                }).collect { response ->
                _peoplesResponse.value = response
            }
        }
    }
}
package com.murgupluoglu.requestsample

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.murgupluoglu.request.RESPONSE
import com.murgupluoglu.request.request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Mustafa Ürgüplüoğlu on 06.09.2019.
 * Copyright © 2019 Mustafa Ürgüplüoğlu. All rights reserved.
 */

class MainViewModel() : ViewModel() {

    val peoplesResponse: MutableLiveData<RESPONSE<PeopleResponse>> = MutableLiveData()

    fun getPeoples(){
        //Use dependency injection in real apps
        val networkModule = NetworkModule()
        peoplesResponse.request({networkModule.service().getPeoples(1)})
    }
}
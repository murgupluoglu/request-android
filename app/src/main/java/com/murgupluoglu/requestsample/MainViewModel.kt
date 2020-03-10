package com.murgupluoglu.requestsample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.murgupluoglu.request.RESPONSE
import com.murgupluoglu.request.request

/**
 * Created by Mustafa Ürgüplüoğlu on 06.09.2019.
 * Copyright © 2019 Mustafa Ürgüplüoğlu. All rights reserved.
 */

class MainViewModel : ViewModel() {

    val peoplesResponse: MutableLiveData<RESPONSE<PeopleResponse>> = MutableLiveData()

    fun getPeoples(){
        //Use dependency injection in real apps
        val networkModule = NetworkModule()
        peoplesResponse.request({networkModule.service().getPeoples(1)})
    }
}
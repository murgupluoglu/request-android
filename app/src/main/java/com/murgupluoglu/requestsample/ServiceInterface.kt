package com.murgupluoglu.requestsample

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Mustafa Ürgüplüoğlu on 06.09.2019.
 * Copyright © 2019 Mustafa Ürgüplüoğlu. All rights reserved.
 */

interface ServiceInterface {

    @GET("/")
    suspend fun getPeoples(@Query("results") size : Int): PeopleResponse

}
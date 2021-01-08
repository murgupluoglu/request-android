package com.murgupluoglu.requestsample

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by Mustafa Ürgüplüoğlu on 06.09.2019.
 * Copyright © 2019 Mustafa Ürgüplüoğlu. All rights reserved.
 */

interface ServiceInterface {

    @GET("/")
    suspend fun getPeoples(@Query("results") size: Int): PeopleResponse

    @GET
    suspend fun http400(@Url url: String = "https://httpstat.us/400"): PeopleResponse

}
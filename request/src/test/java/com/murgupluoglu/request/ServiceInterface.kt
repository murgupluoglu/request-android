package com.murgupluoglu.request

import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceInterface {

    @GET("/")
    suspend fun getPeoples(@Query("results") size: Int): PeopleResponse

}
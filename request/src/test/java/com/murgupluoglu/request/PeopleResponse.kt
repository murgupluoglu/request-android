package com.murgupluoglu.request

data class PeopleResponse(
    var info: Info? = null
)

data class Info(
    val page: Int,
    val results: Int,
    val seed: String,
    val version: String
)
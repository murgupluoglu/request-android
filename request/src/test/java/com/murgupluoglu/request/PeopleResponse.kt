package com.murgupluoglu.request

data class PeopleResponse(
    var info: Info? = null
)

data class Info(
    var page: Int,
    var results: Int,
    var seed: String,
    var version: String
)
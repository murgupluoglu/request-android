package com.murgupluoglu.requestsample

/**
 * Created by Mustafa Ürgüplüoğlu on 06.09.2019.
 * Copyright © 2019 Mustafa Ürgüplüoğlu. All rights reserved.
 */

data class PeopleResponse(
    var info: Info? = null,
    var results: List<User> = arrayListOf()
)

data class Info(
    val page: Int,
    val results: Int,
    val seed: String,
    val version: String
)

data class User(
    var cell: String,
    val dob: Dob,
    var email: String,
    var gender: String,
    val id: IdClass,
    val location: Location,
    val login: Login,
    val name: Name,
    var nat: String,
    var phone: String,
    val picture : Picture,
    val registered: Registered
)

data class Picture(
    val large: String,
    val medium: String,
    val thumbnail: String
)

data class Login(
    val md5: String,
    val password: String,
    val salt: String,
    val sha1: String,
    val sha256: String,
    val username: String,
    val uuid: String
)

data class Dob(
    val age: Int,
    val date: String
)

data class Location(
    val city: String,
    val coordinates: Coordinates,
    val postcode: String,
    val state: String,
    val street: String,
    val timezone: Timezone
)

data class Timezone(
    val description: String,
    val offset: String
)

data class Coordinates(
    val latitude: String,
    val longitude: String
)

data class Name(
    val first: String,
    val last: String,
    val title: String
)

data class IdClass(
    val name: String,
    val value: String
)

data class Registered(
    val age: Int,
    val date: String
)
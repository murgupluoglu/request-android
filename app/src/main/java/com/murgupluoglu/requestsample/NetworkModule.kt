package com.murgupluoglu.requestsample

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Mustafa Ürgüplüoğlu on 06.09.2019.
 * Copyright © 2019 Mustafa Ürgüplüoğlu. All rights reserved.
 */

class NetworkModule{

    companion object {
        var BASE_URL = "https://api.randomuser.me"
    }

    fun service(): ServiceInterface {
        val logging = HttpLoggingInterceptor()
        if(BuildConfig.DEBUG){
            logging.level = HttpLoggingInterceptor.Level.BODY
        }else{
            logging.level = HttpLoggingInterceptor.Level.NONE
        }


        val httpClient = okhttp3.OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                .header("Content-Type", "application/json")
                .build()

            chain.proceed(request)
        }

        val clt = httpClient.build()
        val retrofit = retrofit2.Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(clt)
            .build()

        return retrofit.create<ServiceInterface>(ServiceInterface::class.java)
    }
}
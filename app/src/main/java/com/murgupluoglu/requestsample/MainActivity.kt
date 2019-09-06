package com.murgupluoglu.requestsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.murgupluoglu.request.RESPONSE
import com.murgupluoglu.request.STATUS_ERROR
import com.murgupluoglu.request.STATUS_LOADING
import com.murgupluoglu.request.STATUS_SUCCESS

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Use dependency injection in real apps
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.peoplesResponse.observe(this@MainActivity, Observer<RESPONSE<PeopleResponse>> { result ->
            when (result.status) {
                STATUS_LOADING -> {
                    Log.d(TAG, "Loading")
                }
                STATUS_ERROR -> {
                    Log.d(TAG, "Error")
                }
                STATUS_SUCCESS -> {
                    Log.d(TAG, "Success ${result.responseObject}")
                }
            }
        })

        viewModel.getPeoples()
    }
}

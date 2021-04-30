package com.murgupluoglu.requestsample

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.murgupluoglu.request.STATUS_ERROR
import com.murgupluoglu.request.STATUS_LOADING
import com.murgupluoglu.request.STATUS_SUCCESS

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    lateinit var textView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)

        //Use dependency injection in real apps
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.peoplesResponse.observe(this@MainActivity, { result ->
            when (result.status) {
                STATUS_LOADING -> {
                    Log.d(TAG, "Loading")
                    textView.text = "Loading"
                }
                STATUS_ERROR -> {
                    val errorText =
                        "errorCode ${result.errorCode} errorMessage ${result.errorMessage}"
                    Log.e(TAG, errorText)
                    textView.text = errorText
                }
                STATUS_SUCCESS -> {
                    Log.d(TAG, "Success ${result.responseObject}")
                    Log.d(TAG, "isFromCache ${result.isFromCache}")
                    textView.text = result.responseObject.toString()
                }
            }
        })

        viewModel.getPeoples()
    }
}

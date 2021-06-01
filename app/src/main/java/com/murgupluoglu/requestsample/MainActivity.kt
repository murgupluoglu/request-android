package com.murgupluoglu.requestsample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.murgupluoglu.request.Request
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    lateinit var textView: TextView
    lateinit var loadingTextView: TextView
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        loadingTextView = findViewById(R.id.loadingTextView)

        //Use dependency injection in real apps
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupObservers()

        viewModel.getPeoples()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.peoplesResponse.collect { result ->
                when (result) {
                    is Request.Loading -> {
                        Log.d(TAG, "Loading: " + result.isLoading)
                        if (result.isLoading) {
                            loadingTextView.visibility = View.VISIBLE
                        } else {
                            loadingTextView.visibility = View.INVISIBLE
                        }
                    }
                    is Request.Success -> {
                        Log.d(TAG, "Success ${result.data}")
                        Log.d(TAG, "isFromCache ${result.isFromCache}")
                        textView.text = result.data.toString()
                    }
                    is Request.Error -> {
                        val errorText =
                            "errorCode ${result.error.code} errorMessage ${result.error.message}"
                        Log.e(TAG, errorText)
                        textView.text = errorText
                    }
                }
            }
        }
    }
}

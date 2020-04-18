package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.test.databinding.ActivityMainBinding
import com.example.test.R

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var viewModel : MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding.viewModel = viewModel



        val adapter = NonFilteringArrayAdapter<String>(
                this,
                R.layout.dropdown_menu_popup_item,
                resources.getStringArray(R.array.exposed_dropdown)
        )


        binding.exposedDropdown.setAdapter(adapter)

        binding.exposedDropdown.dialogCallback = {
            Log.i(TAG, "dialogCallback was called")
        }

    }
}

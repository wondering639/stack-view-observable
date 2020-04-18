package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding.viewModel = viewModel



        val adapter = NonFilteringArrayAdapter<String>(
                this,
                R.layout.dropdown_menu_popup_item,
                resources.getStringArray(R.array.exposed_dropdown)
        )

        binding.exposedDropdown.setAdapter(adapter)
        // set initial selection to "first item".
        // viewModel property "realValue" (which will be data bound to exposedDropdown) defaults
        // to "second item". so we can easily see that data binding from ViewModel to the exposed dropdown
        // works when the shown value is "second item"
        binding.exposedDropdown.setText("first item")

        binding.exposedDropdown.dialogCallback = {
            Log.i(TAG, "dialogCallback was called")
        }

    }
}

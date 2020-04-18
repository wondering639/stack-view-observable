package com.example.test

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

// Non-filtering ArrayAdapter - for Exposed Dropdown Menu
// based on https://medium.com/@rmirabelle/there-is-no-material-design-spinner-for-android-3261b7c77da8
class NonFilteringArrayAdapter<String>(context: Context, layout: Int, var values: Array<String>) :
    ArrayAdapter<String>(context, layout, values) {

    private val nonFilteringFilter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            // directly return unfiltered value
            // ".also" = work on object (it) and also return it
            return FilterResults().also {
                it.values = values
                it.count = values.size
            }
        }
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return nonFilteringFilter
    }
}
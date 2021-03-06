package com.upineda.frostapp.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.upineda.frostapp.R
import com.upineda.frostapp.adapter.LocationAdapter
import com.upineda.frostapp.viewModel.SearchActivityViewModel
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchActivityViewModel
    private lateinit var adapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this).get(SearchActivityViewModel::class.java)

        iv_search.setOnClickListener {
            if (et_search.text!!.isNotEmpty() && search_progress.visibility == GONE)
                viewModel.searchLocation(et_search.text.toString())
        }

        et_search.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                iv_search.performClick()
                return@OnKeyListener true
            }
            false
        })

        viewModel.showProgress.observe(this, Observer {
            if (it) {
                search_progress.visibility = VISIBLE
                tv_no_results_search.visibility = GONE
            }
            else
                search_progress.visibility = GONE
        })

        viewModel.locationList.observe(this, Observer {
            if (it != null)
                adapter.setLocationList(it)
            else
                tv_no_results_search.visibility = VISIBLE
        })

        adapter = LocationAdapter(this)
        rv_search.adapter = adapter

    }
}
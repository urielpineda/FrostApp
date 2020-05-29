package com.upineda.frostapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.upineda.frostapp.network.model.Location
import com.upineda.frostapp.repository.SearchActivityRepository

class SearchActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SearchActivityRepository(application)
    val showProgress: LiveData<Boolean>
    val locationList: LiveData<Location>

    init {
        this.showProgress = repository.showProgress
        this.locationList = repository.locationList
    }

    fun searchLocation(searchString: String) {
        repository.searchLocation(searchString)
    }
}
package com.upineda.frostapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.upineda.frostapp.network.model.WeatherResponse
import com.upineda.frostapp.repository.DetailsActivityRepository

class DetailsActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DetailsActivityRepository(application)
    val showProgress: LiveData<Boolean>
    val response: LiveData<WeatherResponse>

    init {
        showProgress = repository.showProgress
        response = repository.response
    }

    fun getWeather(id: String, date: String) {
        repository.getWeather(id, date)
    }
}
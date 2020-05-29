package com.upineda.frostapp.network

import com.upineda.frostapp.network.model.Location
import com.upineda.frostapp.network.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://frost.met.no/"

interface WeatherNetwork {

    @GET("sources/v0.jsonld?types=SensorSystem&country=NO")
    fun getLocation(@Query("municipality") searchString: String): Call<Location>

    @GET("observations/v0.jsonld?elements=air_temperature%2Cwind_speed%2Cboolean_fair_weather(cloud_area_fraction%20P1D)")
    fun getWeather(
        @Query("sources") id: String,
        @Query("referencetime") date: String
    ): Call<WeatherResponse>

}
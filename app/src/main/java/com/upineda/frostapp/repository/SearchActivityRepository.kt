package com.upineda.frostapp.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.upineda.frostapp.network.BASE_URL
import com.upineda.frostapp.network.WeatherNetwork
import com.upineda.frostapp.network.model.Location
import okhttp3.Credentials
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SearchActivityRepository(val application: Application) {

    val showProgress = MutableLiveData<Boolean>()
    val locationList = MutableLiveData<Location>()
    val authToken = Credentials.basic("9e0e4f43-d6da-4572-8dd9-f6bad9d545c0", "")

    fun searchLocation(searchString: String) {
        showProgress.value = true
        // Networkcall

        val builder = OkHttpClient().newBuilder()
        builder.readTimeout(50, TimeUnit.SECONDS)
        builder.connectTimeout(50, TimeUnit.SECONDS)

        builder.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                var request = chain.request()
                val headers: Headers =
                    request.headers().newBuilder().add("Authorization", authToken)
                        .build()
                request = request.newBuilder().headers(headers).build()

                return chain.proceed(request)
            }

        })

        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build()


        val service = retrofit.create(WeatherNetwork::class.java)

        service.getLocation(searchString).enqueue(object : Callback<Location> {
            override fun onFailure(call: Call<Location>, t: Throwable) {
                showProgress.value = false
                Toast.makeText(application, "Error wile accessing the API", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(
                call: Call<Location>,
                response: Response<Location>
            ) {
                locationList.value = response.body()
                showProgress.value = false
            }

        })
    }
}
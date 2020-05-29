package com.upineda.frostapp.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.upineda.frostapp.R
import com.upineda.frostapp.adapter.WeatherAdapter
import com.upineda.frostapp.network.model.WeatherData
import com.upineda.frostapp.network.model.WeatherResponse
import com.upineda.frostapp.viewModel.DetailsActivityViewModel
import kotlinx.android.synthetic.main.activity_details.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailsActivityViewModel
    private lateinit var adapter: WeatherAdapter
    private lateinit var locationId: String
    private lateinit var fromDate: String
    private lateinit var toDate: String
    private var days: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val fromCalender = Calendar.getInstance()
        val toCalendar = Calendar.getInstance()


        bt_from.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    fromDate = formatDate(mMonth, mDay, mYear)

                    fromCalender.set(mYear, mMonth, mDay)
                    tv_from.setText(fromDate)
                    tv_to.setText("")
                    bt_to.performClick()
                },
                year,
                month,
                day
            )
            datePicker.show()
        }

        bt_to.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    toDate = formatDate(mMonth, mDay, mYear)
                    tv_to.setText(toDate)
                    toCalendar.set(mYear, mMonth, mDay)

                    val diff = toCalendar.timeInMillis - fromCalender.timeInMillis
                    days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()

                    viewModel.getWeather(locationId, "$fromDate/$toDate")
                },
                year,
                month,
                day
            )

            datePicker.datePicker.minDate = fromCalender.timeInMillis + 24 * 60 * 60 * 1000
            datePicker.show()
        }

        viewModel = ViewModelProvider(this).get(DetailsActivityViewModel::class.java)

        if (intent.hasExtra("id")) {
            locationId = intent.getStringExtra("id")
        }
        if (intent.hasExtra("name")) {
            tv_location.text = intent.getStringExtra("name")
        }
        if (intent.hasExtra("shortName")) {
            tv_location_short.text = intent.getStringExtra("shortName")
            if (tv_location_short.text.isEmpty())
                tv_location_short.visibility = View.GONE
        }
        if (intent.hasExtra("municipality")) {
            tv_municipality.text = intent.getStringExtra("municipality")
            if (tv_municipality.text.isEmpty())
                tv_municipality.visibility = View.GONE
        }

        viewModel.showProgress.observe(this, Observer {
            if (it) {
                details_progress.visibility = VISIBLE
                tv_no_results_weather.visibility = GONE
            } else
                details_progress.visibility = GONE
        })

        viewModel.response.observe(this, Observer {
            var filteredWeatherResponse = WeatherResponse(emptyList<WeatherData>())
            var weatherData: MutableList<WeatherData> = mutableListOf<WeatherData>()
            var hour = 0
            if (it != null) {
                if (days > 1)
                    hour = 12

                for (data in it.data) {
                    if (hour > 23)
                        break

                    val formatTime = DecimalFormat("00").format(hour)
                    if (data.referenceTime.contains("T$formatTime:00:00.000Z")) {
                        weatherData.add(data)
                        if (days == 1)
                            hour++
                    }
                }
                filteredWeatherResponse.data = weatherData
                adapter.setWeatherList(filteredWeatherResponse)
            } else {
                tv_no_results_weather.visibility = VISIBLE
            }
        })


        adapter = WeatherAdapter(this)
        rv_weather.adapter = adapter


        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -1)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        fromDate = dateFormat.format(yesterday.time)
        tv_from.text = fromDate

        val today = Calendar.getInstance().time
        val todayFormat = SimpleDateFormat("yyyy-MM-dd")
        toDate = todayFormat.format(today.time)
        tv_to.text = toDate

        viewModel.getWeather(locationId, "$fromDate/$toDate")
    }

    fun formatDate(month: Int, day: Int, year: Int): String {
        val returnString =
            "" + year + "-" + DecimalFormat("00").format(month + 1) + "-" + DecimalFormat(
                "00"
            ).format(day)
        return returnString
    }
}
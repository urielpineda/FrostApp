package com.upineda.frostapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upineda.frostapp.R
import com.upineda.frostapp.network.model.WeatherResponse
import kotlinx.android.synthetic.main.rv_weather_child.view.*

class WeatherAdapter(private val context: Context) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    private var weather: WeatherResponse = WeatherResponse(emptyList())

    fun setWeatherList(weather: WeatherResponse) {
        this.weather = weather
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return weather.data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var time = weather.data[position].referenceTime
        time = timeFormater(time)

        holder.time.text = time

        for (element in weather.data[position].observations) {
            if (element.elementId.equals("boolean_fair_weather(cloud_area_fraction P1D)")) {
                if (element.value.toInt() == 1)
                    holder.icon.setImageResource(R.drawable.sun)
                else if (element.value.toInt() == 0)
                    holder.icon.setImageResource(R.drawable.cloud)
                holder.icon.visibility = View.VISIBLE
            } else if (element.elementId.equals("wind_speed")) {
                holder.wind.visibility = View.VISIBLE
                holder.windSpeed.visibility = View.VISIBLE
                holder.windSpeed.text = "" + element.value + element.unit
            } else if (element.elementId.equals("air_temperature")) {
                var temperature = element.value.toString()
                if (element.unit.equals("degC"))
                    temperature += " °C"
                holder.temperature.text = temperature
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_weather_child,
                parent,
                false
            )
        )
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val temperature = v.tv_temperature!!
        val time = v.tv_time!!
        val icon = v.iv_icon!!
        val windSpeed = v.tv_wind_speed!!
        val wind = v.tv_wind!!
    }

    // format date and time to look more readable
    // before: 2019-11-25T00:00:00.000Z
    // after:  2019-11-25 00:00
    fun timeFormater(time: String): String {
        var retVal = time.replace("T", " ", true)
        retVal = retVal.replace("Z", "", true)
        retVal = retVal.substring(0, time.length - 8);
        return retVal;
    }

}
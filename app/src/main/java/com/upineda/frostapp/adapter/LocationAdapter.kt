package com.upineda.frostapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upineda.frostapp.R
import com.upineda.frostapp.network.model.Location
import com.upineda.frostapp.view.DetailsActivity
import kotlinx.android.synthetic.main.rv_location_child.view.*

class LocationAdapter(private val context: Context) :
    RecyclerView.Adapter<LocationAdapter.ViewHolder>() {
    private var location: Location = Location(emptyList(), 0)

    fun setLocationList(location: Location) {
        this.location = location
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return this.location.totalItemCount
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name =
            if (location.data[position].name.isEmpty()) "N/A" else location.data[position].name
        val shortName =
            if (location.data[position].shortName.isEmpty()) name else location.data[position].shortName

        holder.name.text = name
        holder.shortName.text = shortName
        holder.rootView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("municipality", location.data[position].municipality)
            intent.putExtra("id", location.data[position].id)
            intent.putExtra("name", location.data[position].name)
            intent.putExtra("shortName", location.data[position].shortName)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_location_child,
                parent,
                false
            )
        )
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name = v.tv_location_name!!
        val shortName = v.tv_short_name!!
        val rootView = v.child_root!!
    }

}
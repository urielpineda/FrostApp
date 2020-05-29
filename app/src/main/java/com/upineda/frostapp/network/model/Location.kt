package com.upineda.frostapp.network.model

import kotlinx.android.parcel.Parcelize

class Station(
    val id: String = "",
    val municipality: String = "",
    val name: String = "",
    val shortName: String = ""
)

class Location(
    val data: List<Station>,
    val totalItemCount: Int
)
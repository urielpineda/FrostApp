package com.upineda.frostapp.network.model

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
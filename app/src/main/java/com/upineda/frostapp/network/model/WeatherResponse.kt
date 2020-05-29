package com.upineda.frostapp.network.model

class WeatherResponse(
    var data: List<WeatherData>
)

class WeatherData(
    val observations: List<Observations>,
    val referenceTime: String
)

class Observations(
    val value: Double,
    val elementId: String,
    val unit: String
)

class Level(
    val value: Int
)
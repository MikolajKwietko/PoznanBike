package com.example.poznanbike.bikestations
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BikeStations(
    @Json(name = "features")
    val items: List<BikeStation>
)
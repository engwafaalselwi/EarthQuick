package com.example.earthquick

import com.google.gson.annotations.SerializedName

data class EarthData(
    @SerializedName("properties") var properties: Properties,
    @SerializedName("geometry") var geometry: Geometry,
    @SerializedName("id") var id: String
)
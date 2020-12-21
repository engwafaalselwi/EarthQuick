package com.example.earthquick
import com.google.gson.annotations.SerializedName

data class EarthResponse(
    @SerializedName("features") var earthItems: List<EarthData>
)
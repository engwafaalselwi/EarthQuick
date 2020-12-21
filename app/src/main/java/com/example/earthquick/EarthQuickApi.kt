package com.example.earthquick

import retrofit2.Call
import retrofit2.http.GET

interface EarthQuickApi {
    @GET("/fdsnws/event/1/query?format=geojson&limit=10")
    fun fetchContents() : Call<EarthResponse>
}
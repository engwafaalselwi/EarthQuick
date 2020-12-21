package com.example.earthquick

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "ErthFetcher"

class ErthQuickFetcher {
    private val earthApi:EarthQuickApi

    init {
        val retrofit: Retrofit = Retrofit.
        Builder()
            .baseUrl("https://earthquake.usgs.gov/fdsnws/event/1/")
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build()
        earthApi = retrofit.create(EarthQuickApi::class.java)
    }
    fun fetchContents(): LiveData<List<EarthData>> {
        val responseLiveData: MutableLiveData<List<EarthData>> = MutableLiveData()
        val erthRequest: Call<EarthResponse> = earthApi.fetchContents()
        erthRequest.enqueue(object : Callback <EarthResponse> {
            override fun onFailure(call: Call<EarthResponse>,t: Throwable) {
                Log.e(TAG, "Failed to fetch erthItems", t)            }
            override fun onResponse(
                call: Call<EarthResponse>,
                response: Response<EarthResponse>
            )
            {
                Log.d(TAG, "Response received ${response.code().toString()} ")
                val erthResponse: EarthResponse? = response.body()

                var erthItems: List<EarthData> = erthResponse?.earthItems
                    ?: mutableListOf()

                responseLiveData.value = erthItems
            }
        })
        return responseLiveData}

}

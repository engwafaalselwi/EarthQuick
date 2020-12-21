package com.example.earthquick

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class EarthQuickViewModel :ViewModel() {

    val earthItemsLiveData: LiveData<List<EarthData>>

    init {
        earthItemsLiveData = ErthQuickFetcher().fetchContents()
    }
}
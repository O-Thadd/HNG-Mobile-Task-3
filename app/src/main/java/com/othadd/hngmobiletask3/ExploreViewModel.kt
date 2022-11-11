package com.othadd.hngmobiletask3

import android.app.Application
import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.othadd.hngmobiletask3.network.Country
import com.othadd.hngmobiletask3.network.NetworkApi
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class ExploreViewModel: ViewModel() {

    private var _country = MutableLiveData<String>()
    val country: LiveData<String> get() = _country

    init {
        viewModelScope.launch {
            val countriesJsonString = NetworkApi.retrofitService.getAllCountries()
            val jsonArray = JSONArray(countriesJsonString)
            val country1 = Country(jsonArray.getJSONObject(0))
            _country.value = country1.currency
        }
    }
}

class ExploreViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExploreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExploreViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
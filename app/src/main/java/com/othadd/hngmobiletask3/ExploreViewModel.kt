package com.othadd.hngmobiletask3

import androidx.lifecycle.*
import com.othadd.hngmobiletask3.network.Country
import com.othadd.hngmobiletask3.network.NetworkApi
import kotlinx.coroutines.launch
import org.json.JSONArray

class ExploreViewModel: ViewModel() {

    private var _country = MutableLiveData<String>()
    val country: LiveData<String> get() = _country

    private val countries = mutableListOf<Country>()

    init {
        viewModelScope.launch {
            val countriesJsonString = NetworkApi.retrofitService.getAllCountries()
            val countriesJsonArray = JSONArray(countriesJsonString)
            populateCountries(countriesJsonArray)
        }
    }

    private fun populateCountries(countriesJsonArray: JSONArray) {
        for (index in 1..countriesJsonArray.length()){
            countries.add(Country(countriesJsonArray.getJSONObject(index - 1)))
        }
        _country.value = countries.size.toString()
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
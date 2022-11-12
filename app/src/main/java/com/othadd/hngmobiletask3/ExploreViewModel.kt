package com.othadd.hngmobiletask3

import androidx.lifecycle.*
import com.othadd.hngmobiletask3.models.Country
import com.othadd.hngmobiletask3.models.CountryAlphabetGroup
import com.othadd.hngmobiletask3.models.UICountry
import com.othadd.hngmobiletask3.network.NetworkApi
import com.othadd.hngmobiletask3.util.toUICountries
import kotlinx.coroutines.launch
import org.json.JSONArray

class ExploreViewModel: ViewModel() {

    private var _countriesForRecyclerView = MutableLiveData<List<Any>>()
    val countriesForRecyclerView: LiveData<List<Any>> get() = _countriesForRecyclerView

    private var _selectedCountry = MutableLiveData<Country>()
    val selectedCountry: LiveData<Country> get() = _selectedCountry

    private val countriesStore = mutableListOf<Country>()

    fun setSelectedCountry(name: String){
        _selectedCountry.value = countriesStore.find { it.name == name }
    }

//    private val countriesForRecyclerview = mutableListOf<Any>()


    init {
        viewModelScope.launch {
            val countriesJsonString = NetworkApi.retrofitService.getAllCountries()
            val countriesJsonArray = JSONArray(countriesJsonString)
            val countries = parseJsonIntoCountries(countriesJsonArray)

            // save countries to field
            countriesStore.addAll(countries)

            val uiCountries = countries.toUICountries()
            val countryGroupsByAlphabet = sortCountriesIntoAlphabetGroups(uiCountries)
            populateListForRecyclerView(countryGroupsByAlphabet)
        }
    }

    private fun populateListForRecyclerView(countryGroupsByAlphabet: MutableList<CountryAlphabetGroup>) {
        val listForRecyclerview = mutableListOf<Any>()
        for (alphabetGroup in countryGroupsByAlphabet){
            listForRecyclerview.add(alphabetGroup.alphabet.uppercase())
            listForRecyclerview.addAll(alphabetGroup.countries)
        }
        _countriesForRecyclerView.value = listForRecyclerview
    }

    private fun sortCountriesIntoAlphabetGroups(uiCountries: List<UICountry>): MutableList<CountryAlphabetGroup> {
        val countryAlphabetGroups = mutableListOf<CountryAlphabetGroup>()

        fun findOrCreateAlphabetGroup(alphabet: String): Pair<CountryAlphabetGroup, Boolean>{
            val countryGroup = countryAlphabetGroups.find { it.alphabet == alphabet }
            if (countryGroup == null){
                return Pair(CountryAlphabetGroup(alphabet), true)
            }
            return Pair(countryGroup, false)
        }

        for (country in uiCountries){
            val response = findOrCreateAlphabetGroup(country.name[0].toString())
            response.first.countries.add(country)
            if (response.second){
                countryAlphabetGroups.add(response.first)
            }
        }

        countryAlphabetGroups.sortBy { it.alphabet }

        return countryAlphabetGroups
    }

    private fun parseJsonIntoCountries(countriesJsonArray: JSONArray): MutableList<Country> {
        val countries = mutableListOf<Country>()
        for (index in 1..countriesJsonArray.length()){
            countries.add(Country(countriesJsonArray.getJSONObject(index - 1)))
        }
        return countries
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
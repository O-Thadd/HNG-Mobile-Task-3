package com.othadd.hngmobiletask3

import androidx.lifecycle.*
import com.othadd.hngmobiletask3.models.Country
import com.othadd.hngmobiletask3.models.CountryAlphabetGroup
import com.othadd.hngmobiletask3.models.UICountry
import com.othadd.hngmobiletask3.network.NetworkApi
import com.othadd.hngmobiletask3.util.Filter
import com.othadd.hngmobiletask3.util.Filter.Companion.filterCountries
import com.othadd.hngmobiletask3.util.toUICountries
import kotlinx.coroutines.launch
import org.json.JSONArray

class ExploreViewModel : ViewModel() {

    private var _countriesForRecyclerView = MutableLiveData<List<Any>>()
    val countriesForRecyclerView: LiveData<List<Any>> get() = _countriesForRecyclerView

    private var _selectedCountry = MutableLiveData<Country>()
    val selectedCountry: LiveData<Country> get() = _selectedCountry

    private val countriesStore = mutableListOf<Country>()

    private var currentCountriesList = listOf<Country>()

    private var _topImageGroupPosition = MutableLiveData<Int>()
    val topImageGroupPosition: LiveData<Int> get() = _topImageGroupPosition

    private var _languageTag = MutableLiveData<String>()
    val languageTag: LiveData<String> get() = _languageTag

    private var _filters = MutableLiveData<Pair<List<String>, List<String>>>()
    val filters: LiveData<Pair<List<String>, List<String>>> get() = _filters

    private val selectedFilters = mutableListOf<String>()

    private var _hideDialogs = MutableLiveData<Boolean>()
    val hideDialogs: LiveData<Boolean> get() = _hideDialogs



    fun updateSelectedFilters(filter: String): Boolean {
        return if (selectedFilters.contains(filter)) {
            selectedFilters.remove(filter)
            false
        } else {
            selectedFilters.add(filter)
            true
        }
    }

    private fun applyFilters(filters: MutableList<String> = selectedFilters){
        hideDialogs()
        if (filters.isEmpty()){
            populateRecyclerviewList(countriesStore)
        }
        else{
            val filteredCountries = filterCountries(countriesStore, filters)
            populateRecyclerviewList(filteredCountries)
        }

        updateFiltersLiveData(filters.toList())
        filters.clear()
    }

    fun applyFilters(){
        applyFilters(selectedFilters)
    }

    fun resetFilters(){
        selectedFilters.clear()
        applyFilters(selectedFilters)
    }

    private fun updateFiltersLiveData(filters: List<String>){
        val otherFilters = Filter.getOtherFilters(filters)
        _filters.value = Pair(filters, otherFilters)
    }

    private fun hideDialogs() {
        // exact value not relevant. Purpose is just to trigger action
        val currentValue = _hideDialogs.value!!
        _hideDialogs.value = !currentValue
    }

    fun prepFilteration(){
        selectedFilters.clear()
        selectedFilters.addAll(_filters.value!!.first)
    }

    fun suspendFilteration(){
        selectedFilters.clear()
        updateFiltersLiveData(_filters.value!!.first.toList())
    }

    fun setSelectedCountry(name: String) {
        _selectedCountry.value = countriesStore.find { it.name == name }
    }

    fun alterTopImageGroupPosition() {
        _topImageGroupPosition.value = if (_topImageGroupPosition.value == 1) 2 else 1
    }

    fun setLanguage(languageId: Int){
        when(languageId){
            1 -> _languageTag.value = Languages.ENGLISH.tag
            2 -> _languageTag.value = Languages.GERMAN.tag
            3 -> _languageTag.value = Languages.FRENCH.tag
            4 -> _languageTag.value = Languages.ITALIAN.tag
            5 -> _languageTag.value = Languages.SPANISH.tag
        }
        populateRecyclerviewList(currentCountriesList)
    }



    private fun populateRecyclerviewList(countries: List<Country>){
        currentCountriesList = countries
        val uiCountries = countries.toUICountries(_languageTag.value ?: Languages.ENGLISH.tag)
        val countryAlphabetGroup = sortCountriesIntoAlphabetGroups(uiCountries)
        populateRecyclerViewListInOrder(countryAlphabetGroup)
    }

    fun searchCountries(countrySubstring: String){

        if (countrySubstring.isBlank()){
            applyFilters(_filters.value!!.first.toMutableList())
            return
        }

        val matchedCountries = currentCountriesList.filter { it.name.startsWith(countrySubstring, true) }

        populateRecyclerviewList(matchedCountries.toList())
    }

    private suspend fun getCountriesParseAndPopulateHomeScreen() {
        val countriesJsonString = NetworkApi.retrofitService.getAllCountries()
        val countriesJsonArray = JSONArray(countriesJsonString)
        val countries = parseJsonIntoCountries(countriesJsonArray).toList()

        // save countries to field
        countriesStore.addAll(countries)

        populateRecyclerviewList(countries)
    }

    private fun populateRecyclerViewListInOrder(countryGroupsByAlphabet: MutableList<CountryAlphabetGroup>) {
        val listForRecyclerview = mutableListOf<Any>()
        for (alphabetGroup in countryGroupsByAlphabet) {
            listForRecyclerview.add(alphabetGroup.alphabet.uppercase())
            listForRecyclerview.addAll(alphabetGroup.countries)
        }
        _countriesForRecyclerView.value = listForRecyclerview
    }

    private fun sortCountriesIntoAlphabetGroups(uiCountries: List<UICountry>): MutableList<CountryAlphabetGroup> {
        val countryAlphabetGroups = mutableListOf<CountryAlphabetGroup>()

        fun findOrCreateAlphabetGroup(alphabet: String): Pair<CountryAlphabetGroup, Boolean> {
            val countryGroup = countryAlphabetGroups.find { it.alphabet == alphabet }
            if (countryGroup == null) {
                return Pair(CountryAlphabetGroup(alphabet), true)
            }
            return Pair(countryGroup, false)
        }

        for (country in uiCountries) {
            val response = findOrCreateAlphabetGroup(country.name[0].toString())
            response.first.countries.add(country)
            if (response.second) {
                countryAlphabetGroups.add(response.first)
            }
        }

        countryAlphabetGroups.sortBy { it.alphabet }

        return countryAlphabetGroups
    }

    private fun parseJsonIntoCountries(countriesJsonArray: JSONArray): MutableList<Country> {
        val countries = mutableListOf<Country>()
        for (index in 1..countriesJsonArray.length()) {
            countries.add(Country(countriesJsonArray.getJSONObject(index - 1)))
        }
        return countries
    }

    init {
        viewModelScope.launch {
            _languageTag.value = Languages.ENGLISH.tag
            getCountriesParseAndPopulateHomeScreen()
            _topImageGroupPosition.value = 1
            updateFiltersLiveData(listOf())
            _hideDialogs.value = false
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

enum class Languages(val tag: String){
    ENGLISH("en"),
    GERMAN("de"),
    FRENCH("fr"),
    ITALIAN("it"),
    SPANISH("es"),
}
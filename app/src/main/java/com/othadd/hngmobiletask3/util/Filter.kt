package com.othadd.hngmobiletask3.util

import com.othadd.hngmobiletask3.models.Country

const val AFRICA =  "Africa"
const val ANTARCTICA =  "Antarctica"
const val ASIA =  "Asia"
const val AUSTRALIA =  "Australia"
const val EUROPE =  "Europe"
const val NORTH_AMERICA =  "North America"
const val SOUTH_AMERICA =  "South America"
const val UTC_PLUS_1 =  "UTC+01:00"
const val UTC_PLUS_2 =  "UTC+02:00"
const val UTC_PLUS_3 =  "UTC+03:00"
const val UTC_MINUS_1 =  "UTC-01:00"
const val UTC_MINUS_2 =  "UTC-02:00"
const val UTC_MINUS_3 =  "UTC-03:00"

class Filter {

    companion object{
        private val continentFilterTypes = listOf(AFRICA, ANTARCTICA, ASIA, AUSTRALIA, EUROPE, NORTH_AMERICA, SOUTH_AMERICA)
        private val timeZoneFilterType = listOf(UTC_PLUS_1, UTC_PLUS_2, UTC_PLUS_3, UTC_MINUS_1, UTC_MINUS_2, UTC_MINUS_3)
        private val allFilters = listOf(AFRICA, ANTARCTICA, ASIA, AUSTRALIA, EUROPE, NORTH_AMERICA, SOUTH_AMERICA, UTC_PLUS_1, UTC_PLUS_2, UTC_PLUS_3, UTC_MINUS_1, UTC_MINUS_2, UTC_MINUS_3)

        fun filterCountries(countries: List<Country>, filters: List<String>): List<Country> {

            val continentFilters = filters.filter { continentFilterTypes.contains(it) }
            val timeZoneFilters = filters.filter { timeZoneFilterType.contains(it) }

            if (continentFilters.isEmpty()) {
                return filter(countries, timeZoneFilters)
            }

            if (timeZoneFilters.isEmpty()) {
                return filter(countries, continentFilters)
            }

            return doDoubleFilteration(countries, continentFilters, timeZoneFilters)
        }

        private fun doDoubleFilteration(countries: List<Country>, continentFilters: List<String>, timeZoneFilters: List<String>): List<Country> {
            val continentFiltered = filter(countries, continentFilters)
            return filter(continentFiltered, timeZoneFilters)
        }

        private fun filter(countries: List<Country>, filters: List<String>): List<Country>{
            val filteredCountries = mutableListOf<Country>()
            for(filter in filters){
                filteredCountries.addAll(countries.filter { it.timezone == filter || it.continent == filter })
            }
            return filteredCountries
        }

        fun getOtherFilters(filters: List<String>): List<String>{
            return allFilters.toMutableList().also { it.removeAll(filters) }
        }
    }
}
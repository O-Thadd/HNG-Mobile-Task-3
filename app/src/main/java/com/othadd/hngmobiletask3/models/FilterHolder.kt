package com.othadd.hngmobiletask3.models

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

data class FilterHolder(val value: String) {

    companion object{
        fun filterList(countries: List<Country>, filters: List<FilterHolder>): List<Country>{
            val filteredCountries = mutableListOf<Country>()
            for(filter in filters){
                filteredCountries.addAll(countries.filter { it.continent == filter.value })
                filteredCountries.addAll(countries.filter { it.timezone == filter.value })
            }
            return filteredCountries
        }
    }
}
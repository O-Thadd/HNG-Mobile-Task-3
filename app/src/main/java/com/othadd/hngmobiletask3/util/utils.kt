package com.othadd.hngmobiletask3.util

import com.othadd.hngmobiletask3.models.Country
import com.othadd.hngmobiletask3.models.UICountry

fun MutableList<Country>.toUICountries(): List<UICountry>{
    return this.map { it.toUICountry() }
}
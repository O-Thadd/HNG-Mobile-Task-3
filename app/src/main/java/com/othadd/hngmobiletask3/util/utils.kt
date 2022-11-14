package com.othadd.hngmobiletask3.util

import com.othadd.hngmobiletask3.Languages
import com.othadd.hngmobiletask3.models.Country
import com.othadd.hngmobiletask3.models.UICountry

fun MutableList<Country>.toUICountries(languageTag: String): List<UICountry> {
    return this.map { it.toUICountry(languageTag) }
}

val APILanguageTag = hashMapOf(
    Languages.ENGLISH.tag to "eng",
    Languages.GERMAN.tag to "deu",
    Languages.FRENCH.tag to "fra",
    Languages.ITALIAN.tag to "ita",
    Languages.SPANISH.tag to "spa"
)

@JvmName("listToUICountries")
fun List<Country>.toUICountries(languageTag: String): List<UICountry> {
    return this.map { it.toUICountry(APILanguageTag[languageTag] ?: "eng") }
}


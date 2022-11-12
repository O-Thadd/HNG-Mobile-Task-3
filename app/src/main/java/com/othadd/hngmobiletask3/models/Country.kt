package com.othadd.hngmobiletask3.models

import android.util.Log
import org.json.JSONObject

const val NA = "not available"

data class Country(val json: JSONObject) {
    lateinit var name: String
    lateinit var officialName: String
    var translatedNames = HashMap<String, String>()
    lateinit var flagURL: String
    lateinit var coatOfArmURL: String
    lateinit var capital: String
    var population: Long = 0
    var area: Long = 0
    lateinit var continent: String
    lateinit var landLocked: String
    lateinit var independence: String
    lateinit var memberOfUN: String
    lateinit var region: String
    lateinit var subRegion: String
    val languages = mutableListOf<String>()
    lateinit var currency: String
    lateinit var timezone: String
    lateinit var dialingCode: String
    lateinit var drivingSide: String

    fun toUICountry(): UICountry{
        return UICountry(name, capital, flagURL)
    }

    init {
        initializeFields(json)
    }

    private fun initializeFields(json: JSONObject) {
        name = json.getJSONObject("name")
            .getString("common")
        officialName = json.getJSONObject("name")
            .getString("official")

        // logging
        Log.e("parsing json", "started $name")

        val nameTranslationsJSONObject = json.getJSONObject("translations")
        val nameTranslationsIterator = nameTranslationsJSONObject.keys()
        while (nameTranslationsIterator.hasNext()) {
            val languageTranslationKey = nameTranslationsIterator.next()
            val languageTranslationValue =
                nameTranslationsJSONObject.getJSONObject(languageTranslationKey)
                    .getString("official")
            translatedNames[languageTranslationKey] = languageTranslationValue
        }

        flagURL = json.getJSONObject("flags").getString("png")

        coatOfArmURL = try {
            json.getJSONObject("coatOfArms").getString("png")
        } catch (e: Exception) {
            NA
        }

        capital = try {
            json.getJSONArray("capital")[0] as String
        } catch (e: Exception) {
            NA
        }

        population = json.getLong("population")

        area = json.getLong("area")

        continent = json.getJSONArray("continents").getString(0)

        landLocked = json.getString("landlocked")

        independence = try { json.getString("independent") } catch (e: Exception) { NA }

        memberOfUN = json.getString("unMember")

        region = json.getString("region")

        subRegion = try {
            json.getString("subregion")
        } catch (e: Exception) {
            NA
        }

        try {
            val languagesJSONObject = json.getJSONObject("languages")
            val languagesIterator = languagesJSONObject.keys()
            while (languagesIterator.hasNext()) {
                val languageKey = languagesIterator.next()
                val languageValue = languagesJSONObject.getString(languageKey)
                languages.add(languageValue)
            }
        } catch (e: Exception) { }

        currency =
            try {
                val currenciesJSONObject = json.getJSONObject("currencies")
                val currenciesIterator = currenciesJSONObject.keys()
                if (currenciesIterator.hasNext()) {
                    val currencyKey = currenciesIterator.next()
                    val currencyJSONObject = currenciesJSONObject.getJSONObject(currencyKey)
                    currencyJSONObject.getString("name")
                } else {
                    NA
                }
            } catch (e: Exception) {
                NA
            }

        timezone = json.getJSONArray("timezones")[0] as String

        dialingCode =
            try {
                val dialingCodeJSONObject = json.getJSONObject("idd")
                val dialingCodeRoot = dialingCodeJSONObject.getString("root")
                val dialingCodeSuffix = dialingCodeJSONObject.getJSONArray("suffixes")[0] as String
                dialingCodeRoot + dialingCodeSuffix
            } catch (e: Exception) {
                NA
            }

        val carJSONObject = json.getJSONObject("car")
        drivingSide = carJSONObject.getString("side")

    }
}
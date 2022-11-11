package com.othadd.hngmobiletask3.network

import org.json.JSONObject

data class Country(val json: JSONObject) {
    lateinit var name: String
    var translatedNames = HashMap<String, String>()
    lateinit var flagURL: String
    lateinit var coatOfArmURL: String
    lateinit var capital: String
    var population: Long = 0
    var area: Long = 0
    lateinit var landLocked: String
    lateinit var independence: String
    lateinit var memberOfUN: String
    lateinit var region: String
    lateinit var subRegion: String
    private val languages = mutableListOf<String>()
    lateinit var currency: String
    lateinit var timezone: String
    lateinit var dialingCode: String
    lateinit var drivingSide: String

    init {
        initializeFields(json)
    }

    private fun initializeFields(json: JSONObject) {
        name = json.getJSONObject("name")
            .getString("official")

        val nameTranslationsJSONObject = json.getJSONObject("translations")
        val nameTranslationsIterator = nameTranslationsJSONObject.keys()
        while (nameTranslationsIterator.hasNext()){
            val languageTranslationKey = nameTranslationsIterator.next()
            val languageTranslationValue = nameTranslationsJSONObject.getJSONObject(languageTranslationKey).getString("official")
            translatedNames[languageTranslationKey] = languageTranslationValue
        }

        flagURL = json.getJSONObject("flags").getString("png")

        coatOfArmURL = json.getJSONObject("coatOfArms").getString("png")

        capital = json.getJSONArray("capital")[0] as String

        population = json.getLong("population")

        area = json.getLong("area")

        landLocked = json.getString("landlocked")

        independence = json.getString("independent")

        memberOfUN = json.getString("unMember")

        region = json.getString("region")

        subRegion = json.getString("subregion")

        val languagesJSONObject = json.getJSONObject("languages")
        val languagesIterator = languagesJSONObject.keys()
        while (languagesIterator.hasNext()){
            val languageKey = languagesIterator.next()
            val languageValue = nameTranslationsJSONObject.getJSONObject(languageKey).getString("official")
            languages.add(languageValue)
        }

        val currenciesJSONObject = json.getJSONObject("currencies")
        val currenciesIterator = currenciesJSONObject.keys()
        currency = if (currenciesIterator.hasNext()){
            val currencyKey = currenciesIterator.next()
            val currencyJSONObject = currenciesJSONObject.getJSONObject(currencyKey)
            currencyJSONObject.getString("name")
        } else{
            "NA"
        }

        timezone = json.getJSONArray("timezones")[0] as String

        val dialingCodeJSONObject = json.getJSONObject("idd")
        val dialingCodeRoot = dialingCodeJSONObject.getString("root")
        val dialingCodeSuffix = dialingCodeJSONObject.getJSONArray("suffixes")[0] as String
        dialingCode = dialingCodeRoot + dialingCodeSuffix

        val carJSONObject = json.getJSONObject("car")
        drivingSide = carJSONObject.getString("side")

    }
}
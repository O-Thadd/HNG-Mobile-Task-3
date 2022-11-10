package com.othadd.hngmobiletask3.network

import org.json.JSONObject

data class Country(val name: String, val json: JSONObject) {
    lateinit var translatedNames: HashMap<String, String>
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
    val languages = mutableListOf<String>()
    lateinit var currency: String
    lateinit var timezone: String
    lateinit var dialingCode: String
    lateinit var drivingSide: String
}
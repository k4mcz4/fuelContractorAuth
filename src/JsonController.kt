package com.fuelContractorAuth

import com.google.gson.Gson
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

class JsonController {
    fun parseJson(jsonString: String) {
        val test: String = Gson().toJson(jsonString)
    }

    fun writeJsonToFile(){

    }

}
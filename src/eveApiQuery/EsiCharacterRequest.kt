package com.fuelContractorAuth.apiQuery

import com.fuelContractorAuth.dataClasses.CharacterModel
import javax.net.ssl.HttpsURLConnection

class EsiCharacterRequest(val characterData: CharacterModel) {

    private val url = "https://esi.evetech.net/latest/"
    private val charId = characterData.characterId
    private val char = Modules.character

    fun setParam(param1: CharacterFirstParameters, param2: SecondParameters? = null): String {
        val secParam = when (param2) {
            null -> ""
            else -> "/$param2"
        }

        return "$url${char}/$charId/${param1.name}${secParam}"
    }
}

class EsiCorporationRequest(){

    private val url = "https://esi.evetech.net/latest/"

}

class EsiCharacterGetRequest{
    fun create(): HttpsURLConnection{

        return TODO("Not implemented")
    }
}
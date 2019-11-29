package com.fuelContractorAuth

import com.fuelContractorAuth.auth.OAuth2
import com.fuelContractorAuth.dataClasses.CharacterModel
import com.fuelContractorAuth.dataClasses.TokenModel
import com.fuelContractorAuth.dataClasses.esi.WalletBalance
import com.google.gson.Gson
import java.io.DataOutputStream
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

class EsiApi(private val sessionId: String) {

    val urlFirstPart = "https://esi.evetech.net/latest/"
    val componentName = ""
    val characterId = ""
    val corporationId = ""

    private fun fetchCharacterData(): CharacterModel {
        val userData = DbController().getCharacterConnectionData(sessionId).first()
        val tokenData = DbController().getTokenData(userData.tokenId)

        return DbController().getCharacterData(userData.uniqueCharId, tokenData)
    }

    fun setupGetRequest(url: String, tokenData: TokenModel): HttpsURLConnection {
        val connection = URL(url).openConnection() as HttpsURLConnection
        connection.requestMethod = "GET"
        connection.addRequestProperty("Authorization", OAuth2.PostRequest().bearerToken(tokenData))
        connection.addRequestProperty("Host", "login.eveonline.com")
        connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        connection.doOutput = true

        return connection
    }

    fun getWalletData(characterData: CharacterModel): WalletBalance {
        val url =
            "https://esi.evetech.net/latest/characters/${characterData.characterId}/wallet/?datasource=tranquility"

        val conn = setupGetRequest(url, characterData.token)

        val json = conn.inputStream.use { it.reader().use { reader -> reader.readText() } }

        return WalletBalance(json.toDouble())
    }

    //TODO Temporary test function
    fun loadData(): User {
        val character = fetchCharacterData()
        val wallet = getWalletData(character)
        return User(character.characterName, wallet.balance.toInt())
    }

}
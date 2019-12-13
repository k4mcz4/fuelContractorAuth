package com.fuelContractorAuth

import com.fuelContractorAuth.auth.OAuth2
import com.fuelContractorAuth.dataClasses.CharacterModel
import com.fuelContractorAuth.dataClasses.TokenModel
import com.fuelContractorAuth.dataClasses.esi.CharacterAssetsFromJson
import com.fuelContractorAuth.dataClasses.esi.WalletBalance
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
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

    val characterData = fetchCharacterData()

    private fun setupGetRequest(url: String, tokenData: TokenModel): HttpsURLConnection {
        val connection = URL(url).openConnection() as HttpsURLConnection
        connection.requestMethod = "GET"
        connection.addRequestProperty("Authorization", OAuth2.PostRequest().bearerToken(tokenData))
        connection.addRequestProperty("Host", "login.eveonline.com")
        connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        connection.doOutput = true

        return connection
    }

    fun getWalletData(): WalletBalance {
        val url =
            "https://esi.evetech.net/latest/characters/${characterData.characterId}/wallet/?datasource=tranquility"
        val conn = setupGetRequest(url, characterData.token)
        val json = conn.inputStream.use { it.reader().use { reader -> reader.readText() } }

        return WalletBalance(json.toDouble())
    }

    fun getCharacterAssets(): List<CharacterAssetsFromJson>{
        var json = ""
        var exit = true
        var page = 1
        val gson = GsonBuilder().create()
        var packagesArray: List<CharacterAssetsFromJson> = mutableListOf()

        do{

            val url = "https://esi.evetech.net/latest/characters/${characterData.characterId}/assets/?datasource=tranquility&page=$page"
            val conn = setupGetRequest(url, characterData.token)
            json = conn.inputStream.use { it.reader().use { reader -> reader.readText() } }


            packagesArray += gson.fromJson(json , Array<CharacterAssetsFromJson>::class.java).toList()

            if(page < conn.getHeaderField("X-Pages").toInt()){
                exit = true
                page++
            } else {
                exit = false
            }

        } while(exit)

        return packagesArray
    }

    //TODO Temporary test function
    fun loadData()
            : User {
        val wallet = getWalletData()
        return User(
            characterData.characterName,
            wallet.balance.toInt(),
            getCharacterAssets()

        )
    }

}
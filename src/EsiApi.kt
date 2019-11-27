package com.fuelContractorAuth

import com.fuelContractorAuth.dataClasses.CharacterModel
import com.fuelContractorAuth.dataClasses.esi.WalletBalance
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

class EsiApi(private val sessionId: String) {

    private fun fetchCharacterData(): CharacterModel {
        val userData = DbController().getCharacterConnectionData(sessionId).first()
        val tokenData = DbController().getTokenData(userData.tokenId)

        return DbController().getCharacterData(userData.uniqueCharId, tokenData)
    }

    fun setupRequestUrl() {

    }

    fun getWalletData(): WalletBalance {
        return WalletBalance(1.1)
    }

    //TODO Temporary test function
    fun loadData(): User{
        val character = fetchCharacterData()
        val wallet = getWalletData()
        return User(character.characterName,wallet.balance.toInt())
    }

}
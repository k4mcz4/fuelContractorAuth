package com.fuelContractorAuth.dataClasses

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class TokenModel(
    var tokenId: Int = 0,
    var access_token: String,
    val token_type: String,
    var expires_in: Int,
    var refresh_token: String,
    var expiresAt: String = ""
){
    fun setExpiration(): String{
        return LocalDateTime.now().plusSeconds(expires_in.toLong()).toString()
    }
}

data class CharacterModel(
    var uniqueCharId: Int = 0,
    @SerializedName("CharacterID") var characterId: Int,
    @SerializedName("CharacterName") val characterName: String,
    @SerializedName("ExpiresOn") val expiresOn: String,
    @SerializedName("Scopes") val scopes: String,
    var token: TokenModel
)

data class SessionModel(
    @SerializedName("characterId") val uniqueCharId: Int,
    val tokenId: Int,
    val sessionId: Int
)

data class CorporationModel(
    var uniqueCorpId: Int = 0,
    @SerializedName("CorporationId") val corporationId: Int
)

data class PageContent(val character: String)
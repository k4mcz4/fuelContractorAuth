package com.fuelContractorAuth.dataClasses

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class TokenModel(
    var tokenId: Int = 0,
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
    val refresh_token: String,
    var expiresAt: String = ""
) {
    fun setExpiration() {
        expiresAt = LocalDateTime.now().plusSeconds(expires_in.toLong()).toString()
    }
}

data class CharacterModel(
    var uniqueCharId: Int = 0,
    @SerializedName("CharacterID") val characterId: Int,
    @SerializedName("CharacterName") val characterName: String,
    @SerializedName("ExpiresOn") val expiresOn: String,
    @SerializedName("Scopes") val scopes: String,
    var token: TokenModel?
)

data class Character(val uniqueCharId: Int, val tokenId: Int)
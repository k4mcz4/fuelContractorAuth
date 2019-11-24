package com.fuelContractorAuth.dataClasses

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
    val uniqueCharId: Int,
    val characterId: Int,
    val characterName: String,
    val expiresOn: String,
    val scopes: String,
    val tokenType: String,
    val characterOwner: OwnerModel,
    val token: TokenModel,
    val intellectualProperty: String
)

data class OwnerModel(val ownerId: Int, val ownerHash: String)

data class CharacterTokenOwner(val uniqueCharId: Int, val tokenId: Int, val ownerId: Int)
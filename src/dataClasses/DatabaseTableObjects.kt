package com.fuelContractorAuth.dataClasses

import org.jetbrains.exposed.sql.Table

object TokenList : Table("tokenList") {
    val tokenId = integer("tokenId").autoIncrement().primaryKey()
    val accessToken = varchar("accessToken", 1000)
    val tokenType = varchar("tokenType", 60)
    val expiresIn = integer("expiresIn")
    val refreshToken = varchar("refreshToken", 1000)
    val expirationDate = varchar("expirationDate", 100)
}

object Secret : Table("superSecret") {
    val clientId = varchar("ClientId", 1000)
    val secretKey = varchar("SecretKey", 1000)
}

object CharacterList : Table("characterList") {
    val uniqueCharId = integer("uniqueCharId").autoIncrement().primaryKey()
    val characterId = integer("characterId")
    val characterName = varchar("characterName", 200)
    val expiresOn = varchar("expiresOn", 100)
    val scopes = varchar("scopes", 1000)
}

object CharacterTokenList : Table("charTokenList") {
    val tokenId = integer("tokenId")
    val characterId = integer("characterId")
    val sessionId = integer("sessionId")
}

object SessionList : Table("sessionList"){
    val sessionId = integer("sessionId").autoIncrement().primaryKey()
    val sessionValue = varchar("sessionValue", 1000)
    val sessionExpires = varchar("sessionExpires", 200)
}
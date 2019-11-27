package com.fuelContractorAuth

import com.fuelContractorAuth.auth.OAuth2
import com.fuelContractorAuth.dataClasses.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DbController {

    private val conn = dbConnect()

    private fun getRoot(path: String): String {
        return System.getProperty("user.dir") + "/src" + path
    }

    private fun dbConnect(): Database {
        val db = Database.connect(
            "jdbc:sqlite:${getRoot("\\auth\\tempData\\dbank.db")}",
            driver = "org.sqlite.JDBC"
        )
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        return db
    }

    fun getSecret(): SecretModel {
        val secretTable = Secret
        val query = transaction(conn) {
            secretTable.selectAll().map { SecretModel(it[secretTable.clientId], it[secretTable.secretKey]) }
        }
        return query[0]
    }

    fun insertTokenData(
        token: TokenModel
    ): Int {
        token.setExpiration()
        val table = TokenList
        return transaction(conn) {
            table.insert {
                it[accessToken] = token.access_token
                it[tokenType] = token.token_type
                it[expiresIn] = token.expires_in
                it[refreshToken] = token.refresh_token
                it[expirationDate] = token.expiresAt
            } get table.tokenId
        }
    }

    fun insertCharacterData(
        character: CharacterModel
    ): Int {
        val table = CharacterList
        return transaction(conn) {
            table.insert {
                it[characterId] = character.characterId
                it[characterName] = character.characterName
                it[expiresOn] = character.expiresOn
                it[scopes] = character.scopes
            } get table.uniqueCharId
        }
    }


    fun updateToken(
        token: TokenModel
    ) {
        val table = TokenList
        token.setExpiration()
        transaction(conn) {
            table.update({ table.tokenId eq token.tokenId }) {
                it[accessToken] = token.access_token
                it[tokenType] = token.token_type
                it[expiresIn] = token.expires_in
                it[refreshToken] = token.refresh_token
                it[expirationDate] = token.expiresAt
            }
        }
    }

    fun insertCharacterSessionData(sessionList: SessionModel) {
        val table = CharacterTokenList

        transaction(conn) {
            table.insert {
                it[sessionId] = sessionList.sessionId
                it[characterId] = sessionList.uniqueCharId
                it[tokenId] = sessionList.tokenId
            }
        }
    }

    fun insertSessionData(uuid: String): Int {
        val table = SessionList

        return transaction(conn) {
            table.insert {
                it[sessionValue] = uuid
            }
        } get table.sessionId

    }

    fun getCharacterConnectionData(sessionId: String): List<SessionModel> {
        val table = SessionList

        return transaction(conn) {
            table.join(CharacterTokenList, JoinType.INNER, null, null) {
                table.sessionId eq CharacterTokenList.sessionId
            }
                .slice(CharacterTokenList.tokenId, CharacterTokenList.characterId, CharacterTokenList.sessionId)
                .select {
                    table.sessionValue.eq(sessionId)
                }.map {
                    SessionModel(
                        it[CharacterTokenList.characterId],
                        it[CharacterTokenList.tokenId],
                        it[CharacterTokenList.sessionId]
                    )
                }
        }
    }

    fun getTokenData(tokenId: Int): TokenModel {
        val table = TokenList

        var tokenData = transaction(conn) {
            table.slice(
                table.accessToken,
                table.tokenType,
                table.expiresIn,
                table.expirationDate,
                table.refreshToken
            ).select {
                table.tokenId.eq(tokenId)
            }.map {
                TokenModel(
                    tokenId,
                    it[table.accessToken],
                    it[table.tokenType],
                    it[table.expiresIn],
                    it[table.refreshToken],
                    it[table.expirationDate]
                )
            }.first()
        }
        val date = LocalDateTime.parse(tokenData.expiresAt)
        val formatted = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS")
        val tokenExpiryDate = formatted.format(date)
        if (date < LocalDateTime.now()) {
            tokenData = OAuth2.PostRequest(code = tokenData.refresh_token).getRefreshTokenFromServer()
        }


        return tokenData

    }

    fun getCharacterData(uniqueCharacterId: Int, tokenData: TokenModel): CharacterModel {
        val table = CharacterList

        return transaction(conn) {
            table.select {
                table.uniqueCharId.eq(uniqueCharacterId)
            }.map {
                CharacterModel(
                    uniqueCharId = uniqueCharacterId,
                    characterId = it[table.characterId],
                    characterName = it[table.characterName],
                    expiresOn = it[table.expiresOn],
                    scopes = it[table.scopes],
                    token = tokenData
                )
            }.first()

        }
    }
}





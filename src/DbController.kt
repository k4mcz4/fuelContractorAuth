package com.fuelContractorAuth

import com.fuelContractorAuth.auth.OAuth2
import com.fuelContractorAuth.dataClasses.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.time.LocalDateTime

class DbController {

    private val conn = dbConnect()

    private fun dbConnect(): Database {
        val db = Database.connect(
            "jdbc:sqlite:${getRoot("\\dataStore\\dbank.db")}",
            driver = "org.sqlite.JDBC"
        )
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        return db
    }

    private fun getRoot(path: String): String {
        return System.getProperty("user.dir") + "/src" + path
    }

    fun getSecret(): SecretModel {
        val secretTable = Secret
        return transaction(conn) {
            secretTable.selectAll().map { SecretModel(it[secretTable.clientId], it[secretTable.secretKey]) }.first()
        }
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

        if (date < LocalDateTime.now()) {
            tokenData = OAuth2.PostRequest(code = tokenData.refresh_token, storedTokenId = tokenData.tokenId)
                .getRefreshTokenFromServer()
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

    fun insertTokenData(
        token: TokenModel
    ): Int {
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
        token: TokenModel,
        storedTokenId: Int
    ) {
        val table = TokenList
        transaction(conn) {
            table.update({ table.tokenId eq storedTokenId }) {
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

    fun validateCharacterTokenData(char: CharacterModel, sessionValue: Int) {
        val table = CharacterTokenList

        val session = transaction(conn) {
            table
                .slice(
                    CharacterTokenList.characterId,
                    CharacterTokenList.tokenId,
                    CharacterTokenList.sessionId
                )
                .select {
                    table.characterId.eq(char.characterId)
                }.map {
                    SessionModel(
                        uniqueCharId = it[CharacterTokenList.characterId],
                        tokenId = it[CharacterTokenList.tokenId],
                        sessionId = it[CharacterTokenList.sessionId]
                    )
                }.lastOrNull()
        }

        when (session) {
            null -> {
                insertCharacterSessionData(
                    SessionModel(
                        insertCharacterData(char),
                        insertTokenData(char.token),
                        sessionValue
                    )
                )
            }
            else -> {
                insertCharacterSessionData(session)
                updateToken(char.token, session.tokenId)
            }
        }
    }
}





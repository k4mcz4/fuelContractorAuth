package com.fuelContractorAuth

import com.fuelContractorAuth.dataClasses.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.util.*

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
                it[assignedAt] = token.expiresAt
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
                it[assignedAt] = token.expiresAt
            }
        }
    }

    fun insertCharacterSessionData(sessionList: SessionInsertModel) {
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
}





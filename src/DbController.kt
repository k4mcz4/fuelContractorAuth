package com.fuelContractorAuth

import com.fuelContractorAuth.dataClasses.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

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
        val charOwnerId = insertOwnerData(character.characterOwner)
        return transaction(conn) {
            table.insert {
                it[characterId] = character.characterId
                it[characterName] = character.characterName
                it[expiresOn] = character.expiresOn
                it[scopes] = character.scopes
                it[tokenType] = character.tokenType
                it[characterOwnerId] = charOwnerId
                it[intellectualProperty] = character.intellectualProperty
            } get table.uniqueCharId
        }
    }

    private fun insertOwnerData(
        owner: OwnerModel
    ): Int {
        val table = OwnerList

        return transaction(conn) {
            table.insert {
                it[ownerHash] = owner.ownerHash
            } get table.ownerId

        }

    }

    fun insertCharacterOwnerConnection(
        charOwner: CharacterTokenOwner
    ) {
        val table = CharacterTokenOwnerList

        transaction(conn) {
            table.insert {
                it[characterId] = charOwner.uniqueCharId
                it[tokenId] = charOwner.tokenId
                it[ownerId] = charOwner.ownerId
            }
        }

    }

    fun updateToken(
        tokenId: Int,
        token: TokenModel
    ) {
        val table = TokenList
        token.setExpiration()
        transaction(conn) {
            table.update({ table.tokenId eq tokenId }) {
                it[accessToken] = token.access_token
                it[tokenType] = token.token_type
                it[expiresIn] = token.expires_in
                it[refreshToken] = token.refresh_token
                it[assignedAt] = token.expiresAt
            }
        }
    }

    fun loadOwnerData(
        ownerId: Int
    ): List<CharacterModel> {
        val ownerTable = CharacterTokenOwnerList
        val tokenTable = TokenList
        val characterTable = CharacterList
        val ownerHashTable = OwnerList
        return transaction(conn) {
            (ownerTable innerJoin tokenTable innerJoin characterTable innerJoin ownerHashTable).slice(
                tokenTable.accessToken,
                tokenTable.tokenType,
                characterTable.characterId
            ).select { ownerTable.ownerId eq ownerId }
                .map {
                    CharacterModel(
                        uniqueCharId = it[characterTable.uniqueCharId],
                        characterId = it[characterTable.characterId],
                        characterName = it[characterTable.characterName],
                        expiresOn = it[characterTable.expiresOn],
                        scopes = it[characterTable.scopes],
                        tokenType = it[characterTable.tokenType],
                        characterOwner = OwnerModel(
                            it[ownerHashTable.ownerId],
                            it[ownerHashTable.ownerHash]
                        ),
                        token = TokenModel(
                            tokenId = it[tokenTable.tokenId],
                            access_token = it[tokenTable.accessToken],
                            token_type = it[tokenTable.tokenType],
                            expires_in = it[tokenTable.expiresIn],
                            refresh_token = it[tokenTable.refreshToken]
                        ),
                        intellectualProperty = it[characterTable.intellectualProperty]
                    )
                }

        }
    }

    fun isTokenValid(): Boolean {

        return true

    }
}



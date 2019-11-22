package com.fuelContractorAuth

import com.fuelContractorAuth.dataClasses.SecretModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class DbController {
    private fun dbConnect(): Database {
        val db = Database.connect(
            "jdbc:sqlite:${getRoot("\\auth\\tempData\\dbank.db")}",
            driver = "org.sqlite.JDBC"
        )
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        return db
    }

    private fun getRoot(path: String): String {
        return System.getProperty("user.dir") + "/src" + path
    }

    object UserSession : Table("userSession") {
        val userId = varchar("userId", 100)
        val accessToken = varchar("accessToken", 1000)
        val tokenType = varchar("tokenType", 60)
        val expiresIn = integer("expiresIn")
        val refreshToken = varchar("refreshToken", 1000)
        val assignedAt = varchar("assignedAt", 100)
    }

    object Secret : Table("superSecret") {
        val clientId = varchar("ClientId", 1000)
        val secretKey = varchar("SecretKey", 1000)
    }

    fun insertUserData(
        conn: Database = dbConnect(),
        table: UserSession = UserSession,
        insertData: UserSessionModel
    ) {
        transaction(conn) {
            table.insert {
                it[accessToken] = insertData.access_token
                it[tokenType] = insertData.token_type
                it[expiresIn] = insertData.expires_in
                it[refreshToken] = insertData.refresh_token
                it[assignedAt] = insertData.expiresAt
                it[userId] = insertData.userId
            }
        }
    }

    fun selectUserData(
        conn: Database = dbConnect(),
        table: UserSession = UserSession,
        uniqueId: String
    ) {

        transaction(conn) {
            table.select { UserSession.userId.eq(uniqueId) }.map {

            }
        }

    }

    fun getSecret(): SecretModel {
        val conn = dbConnect()
        val secretTable = Secret
        val query = transaction(conn) {
            secretTable.selectAll().map { SecretModel(it[secretTable.clientId], it[secretTable.secretKey]) }
        }

        return query[0]
    }
}


data class UserSessionModel(
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
    val refresh_token: String
) {
    var userId: String = ""
    var expiresAt: String = ""
}


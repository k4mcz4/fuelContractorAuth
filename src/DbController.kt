package com.fuelContractorAuth

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

class DbController {
    fun dbConnect(): Database {
        val db = Database.connect(
            "jdbc:sqlite:C:\\Users\\ScaryDomain\\IdeaProjects\\fuelContractorAuth\\src\\auth\\tempData\\dbank.db",
            driver = "org.sqlite.JDBC"
        )
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        return db
    }

    object userSession : Table() {
        val userId = varchar("userId", 100)
        val accessToken = varchar("accessToken", 1000)
        val tokenType = varchar("tokenType", 60)
        val expiresIn = integer("expiresIn")
        val refreshToken = varchar("refreshToken", 1000)
        val assignedAt = varchar("assignedAt", 100)
    }

    fun insertData(){
        
    }
}




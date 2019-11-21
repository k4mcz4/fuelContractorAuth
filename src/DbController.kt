package com.fuelContractorAuth

import org.jetbrains.exposed.sql.Database

class DbController{
    fun dbConnect(): Database{
        return Database.connect("jdbc:sqlite:/src/auth/tempData/dbank.db", driver = "org.sqlite.JDBC")
    }
}
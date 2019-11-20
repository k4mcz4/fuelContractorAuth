package com.fuelContractorAuth.dataClasses

import java.io.File

data class SecretModel (val ClientId: String, val SecretKey: String){
    companion object{
        val secretFile = File("./src/auth/secret.json").readText(Charsets.UTF_8)
    }
}
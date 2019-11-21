package com.fuelContractorAuth.dataClasses

import java.io.File

data class SecretModel(val ClientId: String, val SecretKey: String) {
    companion object {
        val secretFile = File("./src/auth/secret.json").readText(Charsets.UTF_8)
    }
}

data class AuthResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
    val refresh_token: String
) {
    val tokenData = File("./src/tempData/tokenData.json")
}
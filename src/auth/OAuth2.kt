package com.fuelContractorAuth.auth


import com.fuelContractorAuth.dataClasses.AuthResponse
import com.fuelContractorAuth.dataClasses.SecretModel
import com.google.gson.Gson
import java.io.DataOutputStream
import java.io.File
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

class OAuth2 {

    companion object {
        val clientSecret: SecretModel = Gson().fromJson(SecretModel.secretFile, SecretModel::class.java)
        const val redirectUri: String = "https://login.eveonline.com/oauth/authorize"
        const val callbackURL: String = "http://localhost:8080/callback"
        const val tokenAuthUrl: String = "https://login.eveonline.com/oauth/token"
        const val responseType: String = "code"

        val scopeList: List<String> = listOf(
            "esi-wallet.read_character_wallet.v1",
            "esi-assets.read_assets.v1",
            "esi-industry.read_character_jobs.v1",
            "esi-industry.read_corporation_mining.v1"
        )
    }

    fun getUrl(): String {
        return "$redirectUri?response_type=${responseType}&redirect_uri=$callbackURL&client_id=${clientSecret.ClientId}&scope=${scopeList.joinToString(
            separator = "%20"
        ) { it }}"
    }


    class Response(
        val contentType: String,
        val code: String?
    ) {
        private val host: String = "login.eveonline.com"
        private val grantType: String = "authorization_code"

        private fun encodeAuth(): String {
            val encode = "${clientSecret.ClientId}:${clientSecret.SecretKey}"
            return "Basic ${base64(encode)}"
        }

        private fun base64(string: String): String {
            val data = string.toByteArray()
            return Base64.getEncoder().encodeToString(data)
        }


        fun postAuthenticate() {
            val connection = URL(tokenAuthUrl).openConnection() as HttpsURLConnection
            connection.requestMethod = "POST"
            connection.addRequestProperty("Authorization", encodeAuth())
            connection.addRequestProperty("Content-Type", contentType)
            connection.addRequestProperty("Host", host)

            val param = "grant_type=$grantType&code=$code"
            connection.doOutput = true
            val wr = DataOutputStream(connection.outputStream)
            wr.writeBytes(param)
            wr.flush()
            wr.close()

            val text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }

            println(text)

        }

    }
}


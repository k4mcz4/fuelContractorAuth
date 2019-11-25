package com.fuelContractorAuth.auth


import com.fuelContractorAuth.DbController
import com.fuelContractorAuth.dataClasses.TokenModel
import com.fuelContractorAuth.dataClasses.SecretModel
import com.google.gson.Gson
import java.io.DataOutputStream
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

class OAuth2 {

    companion object {
        val clientSecret: SecretModel = DbController().getSecret()
        const val redirectUri: String = "https://login.eveonline.com/oauth/authorize"
        const val tokenAuthUrl: String = "https://login.eveonline.com/v2/oauth/token"
        const val verifyToken: String = "https://login.eveonline.com/oauth/verify"
        const val callbackURL: String = "http://localhost:8080/callback"
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


    class PostRequest(
        private val contentType: String = "application/x-www-form-urlencoded",
        val code: String?
    ) {
        private val host: String = "login.eveonline.com"
        private var grantType: String = "authorization_code"

        private fun encodeAuth(): String {
            val encode = "${clientSecret.ClientId}:${clientSecret.SecretKey}"
            return "Basic ${base64(encode)}"
        }

        private fun base64(string: String): String {
            val data = string.toByteArray()
            return Base64.getEncoder().encodeToString(data)
        }

        private fun bearerAuth(){

        }

        private fun setupConnection(requestMethod: String): HttpsURLConnection {

            val connection = URL(tokenAuthUrl).openConnection() as HttpsURLConnection
            connection.requestMethod = requestMethod
            connection.addRequestProperty("Authorization", encodeAuth())
            connection.addRequestProperty("Content-Type", contentType)
            connection.addRequestProperty("Host", host)
            connection.doOutput = true

            return connection
        }

        fun postAuthenticate(refreshToken: Boolean = false) {

            var param = "grant_type=$grantType&code=$code"

            if (refreshToken) {
                grantType = "refresh_token"
                param = "grant_type=$grantType&refresh_token=$code"
            }

            val connection = setupConnection("POST")

            val wr = DataOutputStream(connection.outputStream)
            wr.writeBytes(param)
            wr.flush()
            wr.close()

            val text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            val userInsert = Gson().fromJson(text, TokenModel::class.java)

            DbController().insertTokenData(userInsert)

        }

        fun refreshToken() {
            postAuthenticate(true)
        }
    }


    fun authenticate(){

    }


}


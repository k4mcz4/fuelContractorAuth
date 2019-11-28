package com.fuelContractorAuth.auth


import com.fuelContractorAuth.DbController
import com.fuelContractorAuth.dataClasses.CharacterModel
import com.fuelContractorAuth.dataClasses.TokenModel
import com.fuelContractorAuth.dataClasses.SecretModel
import com.fuelContractorAuth.dataClasses.SessionModel
import com.google.gson.Gson
import java.io.DataOutputStream
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

class OAuth2 {

    companion object {
        val clientSecret: SecretModel = DbController().getSecret()
        const val redirectUri: String = "https://login.eveonline.com/oauth/authorize"
        const val tokenAuthUrl: String = "https://login.eveonline.com/oauth/token"
        const val verifyToken: String = "https://login.eveonline.com/oauth/verify"
        const val callbackURL: String = "http://localhost:8080/callback"
        const val responseType: String = "code"
        const val POST: String = "POST"
        const val GET: String = "GET"

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
        val code: String? = null
    ) {
        private val host: String = "login.eveonline.com"
        private var grantType: String = "authorization_code"

        private fun encodedAuth(): String {
            val encode = "${clientSecret.ClientId}:${clientSecret.SecretKey}"
            return "Basic ${base64(encode)}"
        }

        private fun base64(string: String): String {
            val data = string.toByteArray()
            return Base64.getEncoder().encodeToString(data)
        }

        fun bearerToken(tokenData: TokenModel): String {
            //TODO Add new method to DbController for accessToken pickup
            return "${tokenData.token_type} ${tokenData.access_token}"
        }

        private fun setupRequest(
            requestMethodSetup: String,
            authorizationProperty: String = encodedAuth(),
            hostType: String = host,
            url: String = tokenAuthUrl
        ): HttpsURLConnection {

            val connection = URL(url).openConnection() as HttpsURLConnection
            connection.requestMethod = requestMethodSetup
            connection.addRequestProperty("Authorization", authorizationProperty)
            connection.addRequestProperty("Host", hostType)

            if (requestMethodSetup == POST) {
                connection.addRequestProperty("Content-Type", contentType)
            }

            connection.doOutput = true

            return connection
        }

        private fun getTokenFromServer(isRefreshToken: Boolean = false): TokenModel {
            var param = "grant_type=$grantType&code=$code"

            if (isRefreshToken) {
                grantType = "refresh_token"
                param = "grant_type=$grantType&refresh_token=$code"
            }

            val connectionPost = setupRequest(requestMethodSetup = POST)

            val wr = DataOutputStream(connectionPost.outputStream)
            wr.writeBytes(param)
            wr.flush()
            wr.close()

            val json = connectionPost.inputStream.use { it.reader().use { reader -> reader.readText() } }
            val tokenData = Gson().fromJson(json, TokenModel::class.java)
            tokenData.setExpiration()

            if (isRefreshToken) {
                DbController().updateToken(tokenData)
            } else {
                tokenData.tokenId = DbController().insertTokenData(tokenData)
            }

            return tokenData

        }

        fun getRefreshTokenFromServer(): TokenModel {
            return getTokenFromServer(true)
        }

        private fun getCharacterDataFromServer(tokenData: TokenModel): CharacterModel {

            val connectionGet =
                setupRequest(
                    requestMethodSetup = GET,
                    authorizationProperty = bearerToken(tokenData),
                    url = verifyToken
                )
            connectionGet.addRequestProperty("User-Agent", "EVE - FUEL CONTRACTOR")


            val json = connectionGet.inputStream.use { it.reader().use { reader -> reader.readText() } }
            val characterData = Gson().fromJson(json, CharacterModel::class.java)
            characterData.token = tokenData

            characterData.uniqueCharId = DbController().insertCharacterData(characterData)

            return characterData
        }

        fun runAuthenticationFlow(): String {

            val token = getTokenFromServer()
            val char = getCharacterDataFromServer(token)

            //TODO Crosscheck if UUID is not duplicated
            val uuid = UUID.randomUUID().toString()


            val sessionValue = DbController().insertSessionData(uuid)
            DbController().insertCharacterSessionData(
                SessionModel(
                    char.uniqueCharId,
                    token.tokenId,
                    sessionValue
                )
            )

            return uuid
        }
    }

}


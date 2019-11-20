package com.fuelContractorAuth.auth

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext
import com.fuelContractorAuth.dataClasses.SecretModel
import com.google.gson.Gson
import io.ktor.auth.OAuthServerSettings
import io.ktor.auth.Principal
import io.ktor.http.HttpMethod
import io.ktor.http.auth.HttpAuthHeader
import kotlinx.html.InputType
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*
import javax.net.ssl.HttpsURLConnection
import io.ktor.auth.Credential as Credential1

sealed class OAuthAccessTokenResponse : Principal {

    data class OAuth2(
        val accessToken: String, val tokenType: String,
        val expiresIn: Long, val refreshToken: String?,
        val extraParameters: HttpAuthHeader.Parameters = HttpAuthHeader.Parameters
    ) : OAuthAccessTokenResponse()
}

class OAuth2() {

    companion object {
        val clientSecret: SecretModel = Gson().fromJson(SecretModel.secretFile, SecretModel::class.java)
        const val redirectUri: String = "https://login.eveonline.com/oauth/authorize"
        const val callbackURL: String = "http://localhost:8080/callback"
        const val tokenAuthUrl: String = "https://login.eveonline.com/oauth/token"

        val scopeList: List<String> = listOf(
            "esi-wallet.read_character_wallet.v1",
            "esi-assets.read_assets.v1",
            "esi-industry.read_character_jobs.v1",
            "esi-industry.read_corporation_mining.v1"
        )
    }

    data class Request(
        val response_type: String = "code",
        val redirect_uri: String = redirectUri,
        val client_id: String = clientSecret.ClientId,
        val scope: List<String> = scopeList,
        val callBackUrl: String = callbackURL
    ) {
        fun createUrl(): String {
            return "$redirect_uri?response_type=${response_type}&redirect_uri=$callbackURL&client_id=$client_id&scope=${scope.joinToString(
                separator = "%20"
            ) { it }}"
        }
    }

    data class Response(
        val contentType: String,
        val code: String?,
        val host: String = "login.eveonline.com",
        val grantType: String = "authorization_code"
    ) {
        val auth: String = encodeAuth()

        private fun encodeAuth(): String {
            val encode = "${clientSecret.ClientId}:${clientSecret.SecretKey}"
            return "Basic ${base64(encode)}"
        }

        private fun base64(string: String): String {
            val data = string.toByteArray()
            return Base64.getEncoder().encodeToString(data)
        }

        /*
        fun sendPostRequest(){


            var reqParam = URLEncoder.encode()

            with(URL(tokenAuthUrl).openConnection() as HttpsURLConnection){
                requestMethod = "POST"

                val wr = OutputStreamWriter(getOutputStream())
                wr.write(reqParam)
                wr.flush()

                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null){
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    println("Response : $response")
                }

            }
        }
         */

        fun test(){

        }

    }
}


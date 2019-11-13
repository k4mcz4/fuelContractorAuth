package com.fuelContractorAuth.auth

import io.ktor.auth.OAuthServerSettings
import io.ktor.auth.Principal
import io.ktor.http.HttpMethod
import io.ktor.http.auth.HttpAuthHeader

sealed class OAuthAccessTokenResponse : Principal {
    data class OAuth1a(
        val token: String, val tokenSecret: String,
        val extraParameters: HttpAuthHeader.Parameters = HttpAuthHeader.Parameters
    ) : OAuthAccessTokenResponse()

    data class OAuth2(
        val accessToken: String, val tokenType: String,
        val expiresIn: Long, val refreshToken: String?,
        val extraParameters: HttpAuthHeader.Parameters = HttpAuthHeader.Parameters
    ) : OAuthAccessTokenResponse()
}

class OAuth2(){
    val eveOauthProvider = OAuthServerSettings.OAuth2ServerSettings(
        name = "eve-esi",
        authorizeUrl = "",
        accessTokenUrl = "",
        requestMethod = HttpMethod.Post,
        clientId = "",
        clientSecret = "",
        defaultScopes = listOf("")
    )
}
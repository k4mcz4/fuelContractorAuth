package com.fuelContractorAuth

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import com.fuelContractorAuth.auth.OAuth2
import freemarker.cache.ClassTemplateLoader
import io.ktor.client.HttpClient
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.server.engine.embeddedServer
import java.io.File

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Authentication) {
        /*
        oauth("eve-esi"){
            client = HttpClient()
            providerLookup = { OAuth2().eveOauthProvider }

        }
        */

    }

    install(FreeMarker){
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "/templates")
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }


    fun coreFilePath(fileName: String): String{
        val corePath = "C:\\Users\\kczarniecki\\Downloads\\fuelContractorAuth\\src\\"
        return corePath + fileName
    }



    routing {
        get("/"){
            val html = File(coreFilePath("frontEndTemplate\\authPage.html")).readText()
            call.respondText(html, ContentType.Text.Html)
        }

        get("/damian"){
            val user = User(name = "Herr_Oqtavian", account = 20000000)
            call.respond(FreeMarkerContent("authPage.html.ftl", mapOf("user" to user), "e"))
        }

        get("/auth"){

        }
    }
}

data class User(var name: String, var account: Int)

data class UserAssets(val assetName: String, val assetLocation: String)

data class UserOrders(val orderId: Int, val orderName: String, var orderQty: Int)
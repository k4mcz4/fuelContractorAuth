package com.fuelContractorAuth

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import com.fuelContractorAuth.auth.OAuth2
import com.fuelContractorAuth.dataClasses.CharacterModel
import com.google.gson.Gson
import freemarker.cache.ClassTemplateLoader
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Authentication) {

    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "/templates")
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }


    routing {
        get("/") {

            val user = User(name = "Herr_Oqtavian", account = 80000000)
            call.respond(FreeMarkerContent("authPage.html", mapOf("user" to user), "e"))
            println("Someone opened!")

        }

        get("/test") {

        }


        get("/auth") {
            call.respondRedirect(OAuth2().getUrl())
        }
        get("/callback") {
            val code: String? = call.request.queryParameters["code"]
            val data = OAuth2.PostRequest(code = code).runAuthenticationFlow().characterId
            call.respondRedirect("/character/data=$data")
        }

        //TODO Absolutely unsafe. Create additional authorization Cookie + IP

        get("/character/{user}") {
            val userData = call.request.queryParameters["user"]
        }
    }

}

data class User(var name: String, var account: Int) {
    fun loadCharacterData() {

    }
}

data class UserAssets(val assetName: String, val assetLocation: String)

data class UserOrders(val orderId: Int, val orderName: String, var orderQty: Int)
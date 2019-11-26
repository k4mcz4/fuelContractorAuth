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
import io.ktor.sessions.*
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Authentication) {

    }

    install(Sessions) {
        cookie<SampleSession>("EveContractor")
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
            //TODO Add auth button
        }

        get("/cookie") {
            call.sessions.set(SampleSession(name = "Test", value = 432))
            val data = 14
            call.respondRedirect("/character/page?session_id=$data")
        }

        get("/test"){

        }

        get("/auth") {
            call.respondRedirect(OAuth2().getUrl())
        }

        get("/callback") {
            val code: String? = call.request.queryParameters["code"]
            val data = OAuth2.PostRequest(code = code).runAuthenticationFlow()
            call.respondRedirect("/character/page?session_id=$data")
        }

        //TODO Absolutely unsafe. Create additional authorization Cookie + IP
        get("/character/{page}") {
            val user: String? = call.request.queryParameters["session_id"]
            call.respond(FreeMarkerContent("authPage.html", mapOf("user" to user), "e"))
        }
    }

}

data class User(var name: String, var account: Int)

data class UserAssets(val assetName: String, val assetLocation: String)

data class UserOrders(val orderId: Int, val orderName: String, var orderQty: Int)

data class SampleSession(val name: String, val value: Int)
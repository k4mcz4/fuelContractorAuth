package com.fuelContractorAuth

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import com.fuelContractorAuth.auth.OAuth2
import freemarker.cache.ClassTemplateLoader
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.content.files
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.sessions.*
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        //Setting routing for Static content
        static("/static"){
            staticRootFolder = File("resources/templates")
            files("authButtons")
        }

        get("/") {
            val user = User("a",1)
            call.respond(FreeMarkerContent("authPage.html",mapOf("user" to user),"e"))
        }

        get("/test1"){
            val user = User("a",1)
            println("Ktos wbil")
            call.respond(FreeMarkerContent("dashboard.html",mapOf("user" to user),"e"))
        }

        get("/cookie") {
            //call.sessions.set(SampleSession(name = "Test", value = 14))
            val data = 14
            call.respondRedirect("/character/page?session_id=$data")
        }

        get("/test"){
            val sessionId: SampleSession? = call.sessions.get<SampleSession>()
            if(sessionId?.value == null){
                println("Wlasnie Cie przekierowalem")
            } else {
                println(sessionId.value)
            }


        }

        get("/auth") {
            call.respondRedirect(OAuth2().getUrl())
        }

        get("/oops"){
            call.respond("Something went wrong...")
        }

        get("/callback") {
            val code: String? = call.request.queryParameters["code"]
            val data = OAuth2.PostRequest(code = code).runAuthenticationFlow()

            //TODO Change it
            call.sessions.set(SampleSession(name = "TestSession", value = data))
            call.respondRedirect("/character")
        }

        //TODO Absolutely unsafe. Create additional authorization Cookie + IP
        get("/character/") {
            val sessionId: SampleSession? = call.sessions.get<SampleSession>()
            if(sessionId?.value == null){
                call.respondRedirect("/oops")
            } else {
                val user = EsiApi(sessionId.value).loadData()
                call.respond(FreeMarkerContent("dashbord.html", mapOf("user" to user), "e"))
            }

        }
    }

}

data class User(var name: String, var account: Int)

data class UserAssets(val assetName: String, val assetLocation: String)

data class UserOrders(val orderId: Int, val orderName: String, var orderQty: Int)

data class SampleSession(val name: String, val value: String)
package com.fuelContractorAuth

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import com.fuelContractorAuth.auth.OAuth2
import com.fuelContractorAuth.dataClasses.esi.CharacterAssetsFromJson
import freemarker.cache.ClassTemplateLoader
import io.ktor.client.request.request
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.content.*
import io.ktor.request.host
import io.ktor.request.uri
import io.ktor.sessions.*
import io.ktor.util.pipeline.intercept
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
        cookie<UserSession>(name = "ECC")
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "/templates")
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(Routing) {
        eveCcApi()
    }

    var stationDummyData = listOf(
        CharacterAssetsFromJson(
            isBlueprintCopy = null,
            isSingleton = null,
            itemId = 0,
            locationFlag = null,
            locationId = 0,
            locationType = null,
            quantity = 1,
            typeId = 0
        )
    )

    routing {
        //Setting routing for Static content
        static("/static") {
            staticRootFolder = File("resources/templates")
            files("authButtons")
            files("css")
            file("jquery-3.4.1.min.js")
        }

        get("/") {
            val user = User("Kamil Czar", 1000000, stationDummyData)
            call.respond(FreeMarkerContent("authPage.html", mapOf("user" to user), "e"))
        }

        get("/auth") {
            call.respondRedirect(OAuth2().getUrl())
        }

        get("/callback") {
            val code: String? = call.request.queryParameters["code"]
            val data = OAuth2.PostRequest(code = code).runAuthenticationFlow()

            //TODO Change it
            call.sessions.set(UserSession(name = "ECC", value = data))
            call.respondRedirect("/cc/character")
        }

        get("/oops") {
            call.respond("Something went wrong...")
        }

        route("/cc") {
            intercept(ApplicationCallPipeline.Call) {

                val callUri = call.request.uri
                val sessionId: UserSession? = call.sessions.get<UserSession>()
                val userIp = call.request.host()
                println(userIp)

                if (sessionId?.value == null) {
                    call.respondRedirect("/")
                } else {
                    val sessionInCookie =
                        DbController().getCharacterConnectionData(sessionId?.value).lastOrNull()?.sessionId
                    if (sessionInCookie == null) {
                        call.respondRedirect("/")
                    }
                }
            }

            get("/test1") {
                val sessionId: UserSession? = call.sessions.get<UserSession>()
                if (sessionId?.value == null) {
                    call.respondRedirect("/")
                } else {
                    val json = EsiApi(sessionId.value).getCharacterAssets()
                    call.respond(json)
                }

            }

            get("/cookie") {
                //call.sessions.set(SampleSession(name = "Test", value = 14))
                val data = 14
                call.respondRedirect("/character/page?session_id=$data")
            }


            //TODO Absolutely unsafe. Create additional authorization Cookie + IP
            get("/character/") {
                val sessionId: UserSession? = call.sessions.get<UserSession>()
                if (sessionId?.value == null) {
                    call.respondRedirect("/oops")
                } else {
                    val user = EsiApi(sessionId.value).loadData()
                    call.respond(FreeMarkerContent("dashboard.html", mapOf("user" to user), "e"))
                }

            }
        }

    }


}

data class User(var name: String, var account: Int, var stationData: List<CharacterAssetsFromJson>)

data class StationData(
    var name: String,
    var location: String,
    var fuelBlocksRemaining: String,
    var fuelDepletes: String
)

data class UserAssets(val assetName: String, val assetLocation: String)

data class UserOrders(val orderId: Int, val orderName: String, var orderQty: Int)

data class UserSession(val name: String, val value: String)
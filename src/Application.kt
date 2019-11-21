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
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.server.engine.embeddedServer
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Path

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

/*fun main(){
    val newRequest = OAuth2.Request().createUrl()
    println(newRequest)
}
*/

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

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "/templates")
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    fun getRoot(path: String): String {
        return System.getProperty("user.dir") + "/src" + path
    }



    routing {
        get("/") {

            val user = User(name = "Herr_Oqtavian", account = 80000000)
            call.respond(FreeMarkerContent("authPage.html", mapOf("user" to user), "e"))
            println("Someone opened!")

        }

        get("/testPath"){
            val conn = DbController().dbConnect()
            var userSession: Table = Table("userSession")
            val result = transaction(conn){
                userSession.selectAll()
            }
            println(result)
        }

        get("/auth") {
            call.respondRedirect(OAuth2().getUrl())
        }
        get("/callback"){
            val code: String? = call.request.queryParameters["code"]
            OAuth2.Response(contentType = "application/x-www-form-urlencoded", code = code).postAuthenticate()

            call.respondRedirect("/")
        }
    }
}

data class User(var name: String, var account: Int)

data class UserAssets(val assetName: String, val assetLocation: String)

data class UserOrders(val orderId: Int, val orderName: String, var orderQty: Int)
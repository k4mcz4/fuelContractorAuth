package com.fuelContractorAuth

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route

fun Routing.eveCcApi() {
    route("/cc") {
        route("/api") {
            get("/stations") {

            }

            get("/assets") {

            }
        }
    }
}
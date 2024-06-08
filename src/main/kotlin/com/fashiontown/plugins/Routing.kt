package com.fashiontown.plugins

import authRoutes
import com.fashiontown.router.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
    authRoutes()
    productsRouter()
    favoriteRouter()
    myFavoriteRouter()
    getProductsRouter()
    cartRouter()
    addCartRouter()
}

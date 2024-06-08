package com.fashiontown.plugins

import authRoutes
import com.fashiontown.router.favoriteRouter
import com.fashiontown.router.getProductsRouter
import com.fashiontown.router.productsRouter
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
    getProductsRouter()
}

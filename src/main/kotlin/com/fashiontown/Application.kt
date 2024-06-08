package com.fashiontown

import com.fashiontown.db.Databaseco
import com.fashiontown.plugins.*
import io.ktor.server.application.*
import java.sql.DriverManager.getConnection

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureRouting()
}

package com.fashiontown.router

import com.fashiontown.db.DatabaseConnection
import com.fashiontown.db.Databaseco
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.DriverManager.getConnection

fun Application.getProductsRouter (){
    val db = DatabaseConnection.database
    routing {
        get("/getproducts{userid}") {
            val userid = call.parameters["userid"]
            val conn = getConnection(Databaseco.url, Databaseco.user, Databaseco.password)
            conn?.use {
                val sql = """
                        SELECT favoriteview.*, 1 AS favorite FROM favoriteview
                        INNER JOIN favorite ON favorite.favorite_product_id = favoriteview.product_id
                        AND favorite.favorite_user_id = ${userid} 
                        UNION ALL
                        SELECT favoriteview.*, 0 AS favorite FROM favoriteview
                        WHERE product_id NOT IN (
                            SELECT favoriteview.product_id FROM favoriteview
                            INNER JOIN favorite ON favorite.favorite_product_id = favoriteview.product_id
                            AND favorite.favorite_user_id = ${userid}
                        )
                    """.trimIndent()
                val stmt = it.createStatement()
                val rs = stmt.executeQuery(sql)
                val results = mutableListOf<Map<String, Any>>()
                while (rs.next()) {
                    val productId = rs.getInt("product_id")
                    val productName = rs.getString("categorie_name")
                    val favorite = rs.getInt("favorite")
                    results.add(mapOf("product_id" to productId, "favorite" to favorite, "product_name" to productName))
                }
                rs.close()
                stmt.close()
                call.respond(HttpStatusCode.OK, results)
            } ?: call.respond(HttpStatusCode.InternalServerError, "Connection failed")
        }
    }
}

fun getConnection(url: String, user: String, password: String): Connection? {
    return try {
        DriverManager.getConnection(url, user, password)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
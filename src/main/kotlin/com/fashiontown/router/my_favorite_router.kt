package com.fashiontown.router

import com.fashiontown.db.Databaseco
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.DriverManager

fun Application.favoriteRouter () {
    routing {
        get("/getfavoriteproducts{userid}") {
            val userid = call.parameters["userid"]
            val conn = DriverManager.getConnection(Databaseco.url, Databaseco.user, Databaseco.password)
            conn?.use {
                val sql = """
                    select * from myfavorite
inner join favorite on favorite.favorite_product_id = myfavorite.product_id and myfavorite.favorite_user_id = ${userid}
                    """.trimIndent()
                val stmt = it.createStatement()
                val rs = stmt.executeQuery(sql)
                val results = mutableListOf<Map<String, Any>>()
                while (rs.next()) {
                    val productId = rs.getInt("product_id")
                    val favoriteId = rs.getInt("favorite_id")
                    val favoriteUserId = rs.getInt("favorite_user_id")
                    val favoriteProductID = rs.getInt("favorite_product_id")
                    val productName = rs.getString("product_name")
                    val productImage = rs.getString("product_image")
                    val productPrice = rs.getInt("product_price")
                    val productRating = rs.getInt("product_rating")
                    results.add(mapOf("product_id" to productId, "favorite_id" to favoriteId,
                        "favorite_user_id" to  favoriteUserId,"favorite_product_id" to favoriteProductID,
                        "product_name" to productName,"product_image" to productImage,
                        "product_price" to productPrice,"product_rating" to productRating
                        ))
                }
                rs.close()
                stmt.close()
                call.respond(HttpStatusCode.OK, results)
            } ?: call.respond(HttpStatusCode.InternalServerError, "Connection failed")
        }
    }
}
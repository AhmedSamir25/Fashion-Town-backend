package com.fashiontown.router

import com.fashiontown.db.Databaseco
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.DriverManager

fun Application.cartRouter(){
    routing {
        get("/cart/user/{userid}"){
            val userid = call.parameters["userid"]
            val conn = DriverManager.getConnection(Databaseco.url, Databaseco.user, Databaseco.password)
            conn?.use{
                val sql = """ 
                    select * from mycart 
inner join cart on cart.cart_product_id= mycart.cart_product_id  and cart.cart_user_id = ${userid};
""".trimIndent()
                val stmt = it.createStatement()
                val rs = stmt.executeQuery(sql)
                val results = mutableListOf<Map<String, Any>>()
                while (rs.next()) {
                    val productId = rs.getInt("product_id")
                    val favoriteId = rs.getInt("cart_id")
                    val favoriteUserId = rs.getInt("cart_user_id")
                    val favoriteProductID = rs.getInt("cart_product_id")
                    val productName = rs.getString("product_name")
                    val productImage = rs.getString("product_image")
                    val productPrice = rs.getInt("product_price")
                    val productRating = rs.getInt("product_rating")
                    val productQt = rs.getInt("cart_product_qt")
                    results.add(mapOf("product_id" to productId, "cart_id" to favoriteId,
                        "cart_user_id" to  favoriteUserId,"cart_product_id" to favoriteProductID,
                        "product_name" to productName,"product_image" to productImage,
                        "product_price" to productPrice,"product_rating" to productRating,
                        "product_qt" to productQt
                    ))
                }
                rs.close()
                stmt.close()
                call.respond(HttpStatusCode.OK, results)
            } ?: call.respond(HttpStatusCode.InternalServerError, "Connection failed")
            }
        get("/totalpricecart/{userid}") {
            val userid = call.parameters["userid"]
            val conn = DriverManager.getConnection(Databaseco.url, Databaseco.user, Databaseco.password)
            conn?.use {
                val sql = """ 
            SELECT SUM(cart_product_qt * product_price) AS total_sum
            FROM mycart
            WHERE cart_user_id = ${userid};
""".trimIndent()
                val stmt = it.createStatement()
                val rs = stmt.executeQuery(sql)
                val results = mutableListOf<Map<String, Any>>()
                while (rs.next()) {
                    val totalSum = rs.getInt("total_sum")
                    results.add(
                        mapOf(
                            "total_sum" to totalSum
                        )
                    )
                }
                rs.close()
                stmt.close()
                call.respond(HttpStatusCode.OK, results)
            } ?: call.respond(HttpStatusCode.InternalServerError, "Connection failed")

        }
        }
    }
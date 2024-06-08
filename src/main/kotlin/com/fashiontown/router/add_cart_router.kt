package com.fashiontown.router

import com.fashiontown.db.DatabaseConnection
import com.fashiontown.entities.CartEnities
import com.fashiontown.entities.CategoriesEntities
import com.fashiontown.entities.FavoriteEntity
import com.fashiontown.models.CartModel
import com.fashiontown.models.FavoriteModel
import com.fashiontown.models.ResponseApp
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Application.addCartRouter (){
    val db = DatabaseConnection.database
    routing {
        post("/addcart") {
            val request = call.receive<CartModel>()
            val checkCart = db.from(CartEnities)
                .select()
                .where { CartEnities.cartProductId eq request.cartProductId }
                .map { it[CartEnities.cartProductId]
                it[CartEnities.cartUserId]
                    it[CartEnities.productQt]
                }
                .firstOrNull()

            if (checkCart == null) {
                val addCart = db.insert(CartEnities) {
                    set(it.cartUserId, request.cartUserId)
                    set(it.cartProductId, request.cartProductId)
                    set(it.productQt, request.productQt)
                }
                if(addCart == 1 ){
                    call.respond(
                        HttpStatusCode.OK, ResponseApp(
                            data = "done add",
                            success = true,
                        )
                    )
                }
            } else {
                val rowEffected = db.delete(CartEnities){
                    it.cartProductId eq request.cartProductId
                }
                if (rowEffected == 1){
                    call.respond(
                        HttpStatusCode.OK,
                        ResponseApp("product has been update",true)
                    )
                }else{
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ResponseApp("field update product",false)
                    )
                }
            }
        }
        put("/cart/product/{cartid}"){

        }
    }
}
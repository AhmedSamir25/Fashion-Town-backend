package com.fashiontown.router

import com.fashiontown.db.DatabaseConnection
import com.fashiontown.entities.FavoriteEntity
import com.fashiontown.models.FavoriteModel
import com.fashiontown.models.ResponseApp
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Application.favoriteRouter() {
    val db = DatabaseConnection.database
    routing {
        post("/favorite") {
            val request = call.receive<FavoriteModel>()
            val checkFavorite = db.from(FavoriteEntity)
                .select()
                .where { FavoriteEntity.favoriteProductId eq request.favoriteProductId }
                .map { it[FavoriteEntity.favoriteProductId] }
                .firstOrNull()

            if (checkFavorite == null) {
               val addFavorite = db.insert(FavoriteEntity) {
                    set(it.favoriteUserId, request.favoriteUserId)
                    set(it.favoriteProductId, request.favoriteProductId)
                }
                if(addFavorite == 1 ){
                call.respond(
                    HttpStatusCode.OK, ResponseApp(
                        data = "done add",
                        success = true,
                    )
                )
                }
            } else {
            val rowEffected =   db.delete(FavoriteEntity){
                    it.favoriteProductId eq request.favoriteProductId
                }
                if (rowEffected == 1){
                    call.respond(HttpStatusCode.OK,
                        ResponseApp("categories has been delete",true))
                }else{
                    call.respond(HttpStatusCode.BadRequest,
                        ResponseApp("field delete categories",false))
                }
            }

        }
    }
}

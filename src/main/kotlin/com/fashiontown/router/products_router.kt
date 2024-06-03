package com.fashiontown.router

import com.fashiontown.db.DatabaseConnection
import com.fashiontown.entities.ProductsEntity
import com.fashiontown.models.ProductsData
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.dsl.where

fun Application.productsRouter (){
    val db = DatabaseConnection.database
    routing {
        get("/getallproducts"){
         val getAllProducts =  db.from(ProductsEntity).select().map {
               val productId = it[ProductsEntity.productId]
               val productName = it[ProductsEntity.productName]
               val productImage = it[ProductsEntity.productImage]
               val productCategories = it[ProductsEntity.productCategories]
               val productPrice = it[ProductsEntity.productPrice]
               val productDescription = it[ProductsEntity.productDescription]
               val productReviews = it[ProductsEntity.productReviews]
               val productRating = it[ProductsEntity.productRating]
               ProductsData(productId = productId ?: -1 ,
                   productName = productName ?: "",productImage = productImage ?: "",
                   productDescription = productDescription ?: "", productPrice = productPrice ?: 0.0,
                   productCategories = productCategories ?: "", productReviews = productReviews ?: 1,
                   productRating = productRating ?: "",
                   )
           }
            call.respond(getAllProducts)
        }
    }
}
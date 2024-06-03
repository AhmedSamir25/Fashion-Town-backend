package com.fashiontown.router

import com.fashiontown.db.DatabaseConnection
import com.fashiontown.entities.ProductsEntity
import com.fashiontown.models.AddNewProduct
import com.fashiontown.models.ProductsData
import com.fashiontown.models.ResponseApp
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

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
        post("/addproduct"){
            val request = call.receive<AddNewProduct>()
            val addNewProduct =  db.insert(ProductsEntity){
                set(it.productName,request.productName)
                set(it.productImage,request.productImage)
                set(it.productPrice,request.productPrice)
                set(it.productCategories,request.productCategories)
                set(it.productDescription,request.productDescription)
                set(it.productReviews,request.productReviews)
                set(it.productRating,request.productRating)
            }
            if (addNewProduct == 1){
                call.respond(
                    HttpStatusCode.OK, ResponseApp(
                    data = "note has ben add",
                    success = true,
                ))
            }else {
                call.respond(
                    HttpStatusCode.OK, ResponseApp(
                    data = "note has ben Field",
                    success = false,
                ))
            }
        }
    }
}
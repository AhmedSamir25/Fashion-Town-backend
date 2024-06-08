package com.fashiontown.router

import com.fashiontown.db.DatabaseConnection
import com.fashiontown.entities.CategoriesEntities
import com.fashiontown.entities.ProductsEntity
import com.fashiontown.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Application.productsRouter (){
    val db = DatabaseConnection.database
    routing {
        // get all products
        get("/products") {
            val getAllProducts = db.from(ProductsEntity).select().map {
                val productId = it[ProductsEntity.productId]
                val productName = it[ProductsEntity.productName]
                val productImage = it[ProductsEntity.productImage]
                val productCategories = it[ProductsEntity.productCategories]
                val productPrice = it[ProductsEntity.productPrice]
                val productDescription = it[ProductsEntity.productDescription]
                val productReviews = it[ProductsEntity.productReviews]
                val productRating = it[ProductsEntity.productRating]
                ProductsData(
                    productId = productId ?: -1,
                    productName = productName ?: "",
                    productImage = productImage ?: "",
                    productDescription = productDescription ?: "",
                    productPrice = productPrice ?: 0.0,
                    productCategories = productCategories ?: "",
                    productReviews = productReviews ?: 1,
                    productRating = productRating ?: "",
                )
            }
            call.respond(getAllProducts)
        }
        // add product
        post("/product") {
            val request = call.receive<AddNewProduct>()
            val addNewProduct = db.insert(ProductsEntity) {
                set(it.productName, request.productName)
                set(it.productImage, request.productImage)
                set(it.productPrice, request.productPrice)
                set(it.productCategories, request.productCategories)
                set(it.productDescription, request.productDescription)
                set(it.productReviews, request.productReviews)
                set(it.productRating, request.productRating)
            }
            if (addNewProduct == 1) {
                call.respond(
                    HttpStatusCode.OK, ResponseApp(
                        data = "The product has been added successfully",
                        success = true,
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK, ResponseApp(
                        data = "Failed to add product",
                        success = false,
                    )
                )
            }
        }
        // product details
        get("/product/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val productDetails = db.from(ProductsEntity).select().where(
                ProductsEntity.productId eq id
            ).map {
                val productId = it[ProductsEntity.productId]
                val productName = it[ProductsEntity.productName]
                val productImage = it[ProductsEntity.productImage]
                val productCategories = it[ProductsEntity.productCategories]
                val productPrice = it[ProductsEntity.productPrice]
                val productDescription = it[ProductsEntity.productDescription]
                val productReviews = it[ProductsEntity.productReviews]
                val productRating = it[ProductsEntity.productRating]
                val det = ProductsData(
                    productId = productId ?: -1,
                    productName = productName ?: "",
                    productImage = productImage ?: "",
                    productDescription = productDescription ?: "",
                    productPrice = productPrice ?: 0.0,
                    productCategories = productCategories ?: "",
                    productReviews = productReviews ?: 1,
                    productRating = productRating ?: "",
                )
                call.respond(det)
            }.firstOrNull()

            if (productDetails == null) {
                call.respond(
                    HttpStatusCode.NotFound, ResponseApp(
                        data = "this product is not found",
                        success = false,
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK, ResponseApp(
                        data = "Product details fetched",
                        success = true,
                    )
                )
            }
        }
        // add categories
        post("/categories") {
            val request = call.receive<AddNewCategorie>()
            val checkcate = db.from(CategoriesEntities).select().where(
                CategoriesEntities.categorieName eq request.categorieName
            ).map {
                it[CategoriesEntities.categorieName]
            }.firstOrNull()
            if(checkcate != null){
                call.respond(
                    HttpStatusCode.BadRequest, ResponseApp(
                        data = "categorie already exits",
                        success = false,))
                return@post
            }else{
            val addNewCategories = db.insert(CategoriesEntities) {
                set(it.categorieName, request.categorieName)
            }
            if (addNewCategories == 1) {
                call.respond(
                    HttpStatusCode.OK,
                    ResponseApp(
                        data = "The Categories has been added successfully",
                        success = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ResponseApp(
                        data = "Failed to add Categories",
                        success = false
                    )
                )
            }
            }
        }
        // get all categories
        get("/categories"){

            val getCategories = db.from(CategoriesEntities).select().map {
                val categoriesId = it[CategoriesEntities.categoriesId]
                val categoriesName = it[CategoriesEntities.categorieName]
                CategoriesData(
                    categoriesId = categoriesId ?: -1,
                    categoriesName = categoriesName ?: "",
                )
            }
            call.respond(getCategories)
        }
        // get product by categories
        get("/products/categories/{categories}"){
            val categories = call.parameters["categories"]?: ""
            val categoriesProducts = db.from(ProductsEntity).select().where(
                ProductsEntity.productCategories eq categories
            ).map {
                val productId = it[ProductsEntity.productId]
                val productName = it[ProductsEntity.productName]
                val productImage = it[ProductsEntity.productImage]
                val productCategories = it[ProductsEntity.productCategories]
                val productPrice = it[ProductsEntity.productPrice]
                val productDescription = it[ProductsEntity.productDescription]
                val productReviews = it[ProductsEntity.productReviews]
                val productRating = it[ProductsEntity.productRating]
                 ProductsData(
                    productId = productId ?: -1,
                    productName = productName ?: "",
                    productImage = productImage ?: "",
                    productDescription = productDescription ?: "",
                    productPrice = productPrice ?: 0.0,
                    productCategories = productCategories ?: "",
                    productReviews = productReviews ?: 1,
                    productRating = productRating ?: "",
                    )

            }
             call.respond(categoriesProducts)

        }
        // update product
        put("/product/{id}"){
            val productId = call.parameters["id"]?.toInt() ?: -1
            val updateProduct = call.receive<ProductsData>()
            val rowEffected = db.update(ProductsEntity){
                set(it.productName,updateProduct.productName)
                set(it.productPrice,updateProduct.productPrice)
                set(it.productImage,updateProduct.productImage)
                set(it.productDescription,updateProduct.productDescription)
                set(it.productCategories,updateProduct.productCategories)
                where {it.productId eq productId}
            }
            if (rowEffected == 1){
                call.respond(HttpStatusCode.OK,ResponseApp("product has been update",true))
            }else{
                call.respond(HttpStatusCode.BadRequest,ResponseApp("field update product",false))
            }

        }
        // delete product
        delete("/product/{id}"){
            val productId = call.parameters["id"]?.toInt() ?: -1
            val rowEffected = db.delete(ProductsEntity){
                it.productId eq productId
            }
            if (rowEffected == 1){
                call.respond(HttpStatusCode.OK,ResponseApp("product has been delete",true))
            }else{
                call.respond(HttpStatusCode.BadRequest,ResponseApp("field delete product",false))
            }
        }
        // update product
        put("/categories/{id}"){
            val categoriesId = call.parameters["id"]?.toInt() ?: -1
            val updateCategories = call.receive<CategoriesData>()
            val rowEffected = db.update(CategoriesEntities){
                set(it.categorieName,updateCategories.categoriesName)
                where {it.categoriesId eq categoriesId}
            }
            if (rowEffected == 1){
                call.respond(HttpStatusCode.OK,ResponseApp("categories has been update",true))
            }else{
                call.respond(HttpStatusCode.BadRequest,
                    ResponseApp("field update categories",false))
            }
        }
        // delete product
        delete ("/categories/{id}"){
            val categoriesId = call.parameters["id"]?.toInt() ?: -1
            val updateCategories = call.receive<CategoriesData>()
            val rowEffected = db.delete(CategoriesEntities){
                 it.categoriesId eq categoriesId
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
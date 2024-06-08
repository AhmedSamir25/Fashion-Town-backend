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
        get("/getallproducts") {
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
        post("/addproduct") {
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
        get("/productdetails/{id}") {
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
        post("/addcategorie") {
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
        get("/getcategories"){

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
        get("/getproductscategories/{categories}"){
            val categories = call.parameters["categories"]?: ""
            val categoriesProducts = db.from(CategoriesEntities).select().where(
                CategoriesEntities.categorieName eq categories
            ).map {
                val categoriesId = it[CategoriesEntities.categoriesId]
                val categoriesName = it[CategoriesEntities.categorieName]
                val categoriesProduct = CategoriesData(
                    categoriesId = categoriesId ?: -1,
                    categoriesName = categoriesName ?: "",
                    )
                call.respond(categoriesProduct)
            }.firstOrNull()
            if (categoriesProducts == null) {
                call.respond(
                    HttpStatusCode.NotFound, ResponseApp(
                        data = "There are no products in this categorie",
                        success = false,
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK, ResponseApp(
                        data = "products in this categorie",
                        success = true,
                    )
                )
            }
        }
        put("/updateproduct/{id}"){
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
        delete("/deleteproduct/{id}"){
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
    }
}
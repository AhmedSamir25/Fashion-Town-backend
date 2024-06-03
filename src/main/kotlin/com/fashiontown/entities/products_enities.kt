package com.fashiontown.entities

import com.fashiontown.entities.UserEntity.primaryKey
import org.ktorm.schema.Table
import org.ktorm.schema.double
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object ProductsEntity: Table<Nothing>(tableName = "products"){
    val productId = int("id").primaryKey()
    val productName = varchar("product_Name")
    val productCategories = varchar("product_categories")
    val  productImage = varchar("product_image")
    val productPrice = double("product_price")
    val productDescription = varchar("product_description")
    val productRating = varchar("product_rating")
    val productReviews = int("product_reviews")
}


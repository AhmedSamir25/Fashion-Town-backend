package com.fashiontown.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object FavoriteEntity: Table<Nothing>(tableName = "favorite"){
    val favoriteId = int("favorite_id").primaryKey()
    val favoriteUserId = int("favorite_user_id")
    val favoriteProductId = int("favorite_product_id")
}
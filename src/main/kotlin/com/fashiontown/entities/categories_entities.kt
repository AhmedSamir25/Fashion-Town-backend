package com.fashiontown.entities

import com.fashiontown.entities.ProductsEntity.primaryKey
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object CategoriesEntities: Table<Nothing>(tableName = "categories"){
    val categoriesId = int("categorie_id").primaryKey()
    val categorieName = varchar("categorie_name")
}
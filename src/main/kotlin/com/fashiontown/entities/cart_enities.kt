package com.fashiontown.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int

object CartEnities:Table<Nothing>("cart"){
    val cartID = int("cart_id").primaryKey()
    val cartProductId = int("cart_product_id")
    val cartUserId = int("cart_user_id")
    val productQt = int("cart_product_qt")
}
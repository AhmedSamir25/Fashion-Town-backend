package com.fashiontown.models

import kotlinx.serialization.Serializable

@Serializable
data class CartModel(
    val cartId:Int,
    val cartProductId:Int,
    val cartUserId:Int,
    val productQt:Int,
)

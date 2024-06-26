package com.fashiontown.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductsData(
    val productId:Int,
    val productName:String,
    val productImage:String,
    val productCategories:String,
    val productPrice:Double,
    val productDescription:String,
    val productReviews:Int,
    val productRating:String,
)
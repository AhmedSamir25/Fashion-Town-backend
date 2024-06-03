package com.fashiontown.models

import kotlinx.serialization.Serializable

@Serializable
data class AddNewProduct(
    val productName:String,
    val productImage:String,
    val productCategories:String,
    val productPrice:Double,
    val productDescription:String,
    val productReviews:Int,
    val productRating:String,
)

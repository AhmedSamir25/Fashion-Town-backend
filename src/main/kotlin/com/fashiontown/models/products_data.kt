package com.fashiontown.models

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
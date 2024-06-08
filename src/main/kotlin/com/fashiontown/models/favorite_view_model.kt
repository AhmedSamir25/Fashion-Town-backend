package com.fashiontown.models

data class FavoriteViewModel(
    val productId:Int,
    val productName:String,
    val productImage:String,
    val productCategories:String,
    val productPrice:Double,
    val productDescription:String,
    val productReviews:Int,
    val productRating:String,
    val  categorieName:String,
    val  categorieId:Int,
)

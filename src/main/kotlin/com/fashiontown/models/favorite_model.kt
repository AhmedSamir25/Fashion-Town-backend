package com.fashiontown.models

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteModel(
    val  favoriteUserId:Int,
    val  favoriteProductId:Int
)

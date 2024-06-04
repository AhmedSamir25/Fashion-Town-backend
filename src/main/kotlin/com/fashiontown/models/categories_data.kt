package com.fashiontown.models

import kotlinx.serialization.Serializable

@Serializable
data class CategoriesData(
    val categoriesId:Int,
    val categoriesName:String,
)

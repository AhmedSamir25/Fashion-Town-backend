package com.fashiontown.models

import kotlinx.serialization.Serializable
@Serializable
data class UserSignIn(
    val id:Int,
    val name:String,
    val  email:String,
    val password:String
)
package com.fashiontown.models

data class ResponseApp<T>(
    val data:T,
    val success:Boolean
)
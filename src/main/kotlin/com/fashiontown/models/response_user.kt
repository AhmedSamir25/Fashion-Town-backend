package com.fashiontown.models

data class ResponseUser<T>(
    val data:T,
    val success:Boolean
)
package com.fashiontown.models

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserModel(
    val name:String,
    val password:String
) {
    fun EncryptingPassword():String{
        return BCrypt.hashpw(password,BCrypt.gensalt())
    }
}
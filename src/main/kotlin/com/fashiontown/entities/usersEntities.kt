package com.fashiontown.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object UserEntity: Table<Nothing>(tableName = "users"){
        val id = int("id").primaryKey()
        val name = varchar("name")
        val  email = varchar("email")
        val password = varchar("password")
}

package com.example.final_projectxml.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user-data")
data class UserDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val weight: Float = 0.0f,
    val calories: Int = 0,
    val steps: Int = 0

)

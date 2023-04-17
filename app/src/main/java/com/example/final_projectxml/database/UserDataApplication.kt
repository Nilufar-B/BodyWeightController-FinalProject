package com.example.final_projectxml.database

import android.app.Application

class UserDataApplication: Application() {
    val db by lazy {
        UserDataDatabase.getInstance(this)
    }
}
package com.example.final_projectxml.database

import android.app.Application

//initializes an instance of the UserDataDatabase
// to ensure that only one instance of the database is created
class UserDataApplication: Application() {
    val db by lazy {
        UserDataDatabase.getInstance(this)
    }
}
package com.example.final_projectxml.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserDataEntity::class], version = 1)
abstract class UserDataDatabase: RoomDatabase() {

    abstract fun userDataDao(): UserDataDao

    companion object{

        @Volatile
        private var INSTANCE: UserDataDatabase? = null

        fun getInstance(context: Context):UserDataDatabase{

            synchronized(this){
                var instance = INSTANCE

                //rebuilds instead of migrating if no migration object exists
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDataDatabase::class.java,
                        "user-data"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }

        }
    }
}
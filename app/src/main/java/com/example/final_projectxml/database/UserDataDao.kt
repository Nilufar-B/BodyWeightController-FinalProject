package com.example.final_projectxml.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDataDao {

    @Insert
    suspend fun insert(userDataEntity: UserDataEntity)

    @Update
    suspend fun  update(userDataEntity: UserDataEntity)

    @Delete
    suspend fun delete(userDataEntity: UserDataEntity)

    @Query("SELECT * FROM 'user-data'")
    fun fetchAllData(): Flow<List<UserDataEntity>>    //using flow to hold values

    @Query("SELECT * FROM 'user-data' WHERE id=:id")
    fun fetchDataById(id:Int): Flow<UserDataEntity>

}
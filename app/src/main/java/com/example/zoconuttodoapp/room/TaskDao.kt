package com.example.zoconuttodoapp.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(taskEntity: TaskEntity)


    @Update
    suspend fun update(taskEntity: TaskEntity)

    @Delete
    suspend fun delete(taskEntity: TaskEntity)

    @Query("SELECT * FROM `user-task` WHERE isActive=false")
    fun fetchNotCompleted(): LiveData<List<TaskEntity>>

    @Query("SELECT * FROM `user-task` WHERE isActive=true")
    fun fetchAllCompleted(): LiveData<List<TaskEntity>>

    @Query("SELECT * FROM `user-task`")
    fun fetchAllData(): LiveData<List<TaskEntity>>

    @Query("Select * from `user-task` where id=:id")
    fun fetchDataById(id: Int): Flow<TaskEntity>

}
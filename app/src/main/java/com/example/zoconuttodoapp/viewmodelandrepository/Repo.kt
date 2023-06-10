package com.example.zoconuttodoapp.viewmodelandrepository

import androidx.lifecycle.LiveData
import com.example.zoconuttodoapp.room.TaskDao
import com.example.zoconuttodoapp.room.TaskEntity
import kotlinx.coroutines.flow.Flow

class Repo(private val dao : TaskDao) {



    fun getAllTask(): LiveData<List<TaskEntity>>{
        return dao.fetchAllData()
    }

    fun fetchAllCompleted(): LiveData<List<TaskEntity>> {
        return dao.fetchAllCompleted()
    }

    fun fetchNotCompleted(): LiveData<List<TaskEntity>> {
        return dao.fetchNotCompleted()
    }

    fun fetchAll(): LiveData<List<TaskEntity>> {
        return dao.fetchAllData()
    }
    fun getTaskById(id : Int): Flow<TaskEntity> {
        return dao.fetchDataById(id)
    }

    suspend fun insert(taskEntity : TaskEntity){
        dao.insert(taskEntity)
    }
    suspend fun delete(taskEntity : TaskEntity){
        dao.delete(taskEntity)
    }
    suspend fun update(taskEntity : TaskEntity){
        dao.update(taskEntity)
    }

}
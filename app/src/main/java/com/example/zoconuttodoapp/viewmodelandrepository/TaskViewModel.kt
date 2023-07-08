package com.example.zoconuttodoapp.viewmodelandrepository

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.zoconuttodoapp.room.TaskDatabase
import com.example.zoconuttodoapp.room.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    val repository: Repo

    init {
        val dao = TaskDatabase.getInstance(application).taskDao()
        repository = Repo(dao)
    }

    fun getAllNotes(): LiveData<List<TaskEntity>> {
        return repository.getAllTask()
    }

    fun getTaskById(id: Int): Flow<TaskEntity> {
        return repository.getTaskById(id)
    }

    fun fetchAllCompleted(): LiveData<List<TaskEntity>> {
        return repository.fetchAllCompleted()
    }

    fun fetchNotCompleted(): LiveData<List<TaskEntity>> {
        return repository.fetchNotCompleted()
    }

    fun fetchAll(): LiveData<List<TaskEntity>> {
        return repository.fetchAll()
    }

    fun addTask(taskEntity: TaskEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(taskEntity)
    }

    fun deleteTask(taskEntity: TaskEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(taskEntity)
    }

    fun updateTask(taskEntity: TaskEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(taskEntity)
    }

}
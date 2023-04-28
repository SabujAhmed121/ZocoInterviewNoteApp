package com.example.zoconuttodoapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "user-task")
data class TaskEntity(

    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val task : String = "",
    var isActive : Boolean = false
)

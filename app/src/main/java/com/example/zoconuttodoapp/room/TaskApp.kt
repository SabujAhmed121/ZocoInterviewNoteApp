package com.example.zoconuttodoapp.room

import android.app.Application

class TaskApp : Application() {

    val db by lazy {
        TaskDatabase.getInstance(this)
    }
}
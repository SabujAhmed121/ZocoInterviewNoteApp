package com.example.zoconuttodoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.zoconuttodoapp.Main.MainActivity
import com.example.zoconuttodoapp.databinding.ActivityAdd2Binding
import com.example.zoconuttodoapp.room.TaskApp
import com.example.zoconuttodoapp.room.TaskDao
import com.example.zoconuttodoapp.room.TaskEntity
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAdd2Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val taskDataDao = (application as TaskApp).db.taskDao()


        binding.button.setOnClickListener {

            addRecord(taskDataDao)

        }
    }

    private fun addRecord(taskDao: TaskDao){


        if (binding.taskText.text.toString().isEmpty()){
            Toast.makeText(applicationContext, "Task cannot be blank",
                Toast.LENGTH_LONG).show()
        }else{

            lifecycleScope.launch {
                taskDao.insert(TaskEntity(task = binding.taskText.text.toString()))
                Toast.makeText(applicationContext, "Record Saved",
                    Toast.LENGTH_LONG).show()

            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
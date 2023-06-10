package com.example.zoconuttodoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.zoconuttodoapp.Main.MainActivity
import com.example.zoconuttodoapp.databinding.ActivityAdd2Binding
import com.example.zoconuttodoapp.room.TaskEntity
import com.example.zoconuttodoapp.viewmodelandrepository.TaskViewModel

class AddActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAdd2Binding.inflate(layoutInflater)
    }
    val viewModel : TaskViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {

            addRecord()

        }
    }

    private fun addRecord(){


        if (binding.taskText.text.toString().isEmpty()){
            Toast.makeText(applicationContext, "Task cannot be blank",
                Toast.LENGTH_LONG).show()
        }else{


                val data = TaskEntity(task = binding.taskText.text.toString())
                viewModel.addTask(data)
                Toast.makeText(applicationContext, "Record Saved",
                    Toast.LENGTH_LONG).show()

            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

}
package com.example.zoconuttodoapp.Main


import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zoconuttodoapp.AddActivity
import com.example.zoconuttodoapp.LoginActivity
import com.example.zoconuttodoapp.R
import com.example.zoconuttodoapp.databinding.ActivityMainBinding
import com.example.zoconuttodoapp.databinding.DialogUpdateBinding
import com.example.zoconuttodoapp.room.TaskEntity
import com.example.zoconuttodoapp.viewmodelandrepository.TaskViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var mAuth: FirebaseAuth
    private val viewModel : TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

       viewModel.getAllNotes().observe(this@MainActivity, Observer {list->

        list?.let { noteList ->
            setUpListOfDataIntoRecyclerView(noteList as ArrayList<TaskEntity>)
           }
      })


        binding.floatBtn.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvAll.setOnClickListener {
            viewModel.fetchAll().observe(this@MainActivity, Observer {list->

                list?.let { noteList ->
                    setUpListOfDataIntoRecyclerView(noteList as ArrayList<TaskEntity>)
                }
            })
        }

        binding.tvSelected.setOnClickListener {

            viewModel.fetchAllCompleted().observe(this@MainActivity, Observer {list->

                list?.let { noteList ->
                    setUpListOfDataIntoRecyclerView(noteList as ArrayList<TaskEntity>)
                }
            })
            }

        binding.tvNotSelected.setOnClickListener {

            viewModel.fetchNotCompleted().observe(this@MainActivity, Observer {list->

                list?.let { noteList ->
                    setUpListOfDataIntoRecyclerView(noteList as ArrayList<TaskEntity>)
                }
            })
        }
}


//setting our mainActivity recyclerview
    private fun setUpListOfDataIntoRecyclerView(taskList:ArrayList<TaskEntity>){

        if(taskList.isNotEmpty()){
           adapter = TaskAdapter(taskList,
                { updateId ->
                    updateRecordDialog(updateId)
                },
                {deleteId->
                    deleteRecordDialog(deleteId)
                },
                { it, task1->
                    updateChecked(it, task1)
                },
                this
            )
            binding.rvItemsList.layoutManager = LinearLayoutManager(this)
            binding.rvItemsList.adapter = adapter
            binding.rvItemsList.visibility = View.VISIBLE
            binding.tvNoRecordsAvailable.visibility = View.INVISIBLE

        }else{
            binding.rvItemsList.visibility = View.INVISIBLE
            binding.tvNoRecordsAvailable.visibility = View.VISIBLE

        }
    }

    //update record
    private fun updateRecordDialog(id: Int){
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)


        lifecycleScope.launch {
            viewModel.getTaskById(id).collect {

                if(it != null){
                    binding.etUpdateTask.setText(it.task)
                }
            }
        }

        binding.tvUpdate.setOnClickListener {
            val task = binding.etUpdateTask.text.toString()

            if (task.isNotEmpty()){
                    viewModel.updateTask(TaskEntity(id, task))
                    Toast.makeText(applicationContext,
                        "Record updated", Toast.LENGTH_LONG).show()
                    updateDialog.dismiss()
            }else{
                Toast.makeText(applicationContext,
                    "Name and update are Invalid", Toast.LENGTH_LONG).show()
            }
        }

        binding.tvCancel.setOnClickListener{
            updateDialog.dismiss()
        }
        updateDialog.show()
    }

    private fun updateChecked(id: Int, task1: String){

        lifecycleScope.launch {

            viewModel.updateTask(TaskEntity(id, task = task1, isActive = isActive))
            Toast.makeText(
                applicationContext,
                "Done", Toast.LENGTH_LONG
            ).show()

        }

    }

    //Delete record
    private fun deleteRecordDialog(id: Int){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            .setTitle("Delete")
            .setMessage("Are you sure ?")
            .setPositiveButton("Yes"){dialogInterface, which->
                    viewModel.deleteTask(TaskEntity(id))
                    Toast.makeText(applicationContext,
                        "Deleted", Toast.LENGTH_LONG).show()
                dialogInterface.dismiss()
            }
            .setNegativeButton("No"){dialogInterface, which->
                Toast.makeText(applicationContext,
                    "Not Deleted", Toast.LENGTH_LONG).show()
                dialogInterface.dismiss()
            }
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    // Menu Setting
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        MenuInflater(this).inflate(R.menu.menu_for_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.refresh -> recreate()
            R.id.logOut -> {

                mAuth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

            }
        }
        return super.onOptionsItemSelected(item)
    }

}
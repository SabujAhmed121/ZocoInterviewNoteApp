package com.example.zoconuttodoapp.Main


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zoconuttodoapp.AddActivity
import com.example.zoconuttodoapp.R
import com.example.zoconuttodoapp.databinding.ActivityMainBinding
import com.example.zoconuttodoapp.databinding.DialogUpdateBinding
import com.example.zoconuttodoapp.room.TaskDatabase
import com.example.zoconuttodoapp.room.TaskEntity
import com.example.zoconuttodoapp.viewmodelandrepository.Repo
import com.example.zoconuttodoapp.viewmodelandrepository.TaskViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        attachSwipeAction()


        viewModel.getAllNotes().observe(this@MainActivity, Observer { list ->

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
            viewModel.fetchAll().observe(this@MainActivity, Observer { list ->

                list?.let { noteList ->
                    setUpListOfDataIntoRecyclerView(noteList as ArrayList<TaskEntity>)
                }
            })
        }

        binding.txCompleted.setOnClickListener {

            viewModel.fetchAllCompleted().observe(this@MainActivity, Observer { list ->

                list?.let { noteList ->
                    setUpListOfDataIntoRecyclerView(noteList as ArrayList<TaskEntity>)
                }
            })
        }

        binding.tvNotCompleted.setOnClickListener {

            viewModel.fetchNotCompleted().observe(this@MainActivity, Observer { list ->

                list?.let { noteList ->
                    setUpListOfDataIntoRecyclerView(noteList as ArrayList<TaskEntity>)
                }
            })
        }
    }


    //setting our mainActivity recyclerview
    private fun setUpListOfDataIntoRecyclerView(taskList: ArrayList<TaskEntity>) {

        if (taskList.isNotEmpty()) {
            adapter = TaskAdapter(
                taskList,
                Repo(TaskDatabase.getInstance(application).taskDao()),
                this
            )
            binding.rvItemsList.layoutManager = LinearLayoutManager(this)
            binding.rvItemsList.adapter = adapter
            binding.rvItemsList.visibility = View.VISIBLE
            binding.tvNoRecordsAvailable.visibility = View.INVISIBLE

        } else {
            binding.rvItemsList.visibility = View.INVISIBLE
            binding.tvNoRecordsAvailable.visibility = View.VISIBLE

        }
    }

    private fun attachSwipeAction() {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = adapter.getTask(position)

                when (direction) {
                    ItemTouchHelper.RIGHT -> {

                        viewModel.deleteTask(task)
                    }

                    ItemTouchHelper.LEFT -> {
                        val id = task.id
                        updateRecordDialog(id)
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvItemsList)
    }

    //update record
    private fun updateRecordDialog(id: Int) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)


        lifecycleScope.launch {
            viewModel.getTaskById(id).collect {

                if (it != null) {
                    binding.etUpdateTask.setText(it.task)
                }
            }
        }

        binding.tvUpdate.setOnClickListener {
            val task = binding.etUpdateTask.text.toString()

            if (task.isNotEmpty()) {
                viewModel.updateTask(TaskEntity(id, task))
                Toast.makeText(
                    applicationContext,
                    "Record updated", Toast.LENGTH_LONG
                ).show()
                updateDialog.dismiss()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name and update are Invalid", Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }
        updateDialog.show()
    }


    //Delete record
//    private fun deleteRecordDialog(id: Int){
//        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//            .setTitle("Delete")
//            .setMessage("Are you sure ?")
//            .setPositiveButton("Yes"){dialogInterface, which->
//                    viewModel.deleteTask(TaskEntity(id))
//                    Toast.makeText(applicationContext,
//                        "Deleted", Toast.LENGTH_LONG).show()
//                dialogInterface.dismiss()
//            }
//            .setNegativeButton("No"){dialogInterface, which->
//                Toast.makeText(applicationContext,
//                    "Not Deleted", Toast.LENGTH_LONG).show()
//                dialogInterface.dismiss()
//            }
//        val alertDialog = builder.create()
//        alertDialog.setCancelable(false)
//        alertDialog.show()
//    }
//    // Menu Setting
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//
//        MenuInflater(this).inflate(R.menu.menu_for_main, menu)
//        return true
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.refresh -> recreate()
//        }
//        return super.onOptionsItemSelected(item)
//    }

}
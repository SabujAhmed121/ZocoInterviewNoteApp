package com.example.zoconuttodoapp.Main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zoconuttodoapp.databinding.RvRecyclerviewItemBinding
import com.example.zoconuttodoapp.room.TaskEntity
import com.example.zoconuttodoapp.viewmodelandrepository.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskAdapter(
    private var items: ArrayList<TaskEntity>,
    val repository: Repo,
    private val context: Context
) :
    RecyclerView.Adapter<TaskAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            RvRecyclerviewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MainHolder, position: Int) {

        val item = items[position]
        holder.bind(item)


    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getTask(position: Int): TaskEntity {
        return items[position]
    }

    inner class MainHolder(private val binding: RvRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val taskText = binding.txDetails
        private val checkBox = binding.customCheckbox

        init {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = items[position]
                    task.isActive = isChecked
                }
            }


            checkBox.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = items[position]
                    task.isActive = checkBox.isChecked
                    updateTaskView(task)


                    CoroutineScope(Dispatchers.IO).launch {
                        repository.update(task)
                    }
                }
            }
        }

        fun bind(task: TaskEntity) {
            taskText.text = task.task
            checkBox.isChecked = task.isActive
            updateTaskView(task)
        }

        private fun updateTaskView(task: TaskEntity) {


            if (task.isActive) {
                taskText.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            } else {
                taskText.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }

    }
}
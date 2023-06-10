package com.example.zoconuttodoapp.Main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zoconuttodoapp.databinding.RvRecyclerviewItemBinding
import com.example.zoconuttodoapp.room.TaskEntity

class TaskAdapter(private var items: ArrayList<TaskEntity>,
                  private val updateListener: (id : Int)-> Unit,
                  private val deleteListener: (id: Int)-> Unit,
                  private val checkedListener: (id: Int, task: String) -> Unit,
                  private val context: Context
):
    RecyclerView.Adapter<TaskAdapter.MainHolder>() {

    inner class MainHolder(val binding: RvRecyclerviewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        val taskText = binding.txTaskName
        val checkBox = binding.checkBox
        val tvEdit = binding.ivEdit
        val tvDelete = binding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            RvRecyclerviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false))
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MainHolder, position: Int) {

        val item = items[position]
        holder.taskText.text = item.task
        holder.checkBox.isChecked = item.isActive


        holder.checkBox.setOnClickListener{
             checkedListener.invoke(item.id, item.task)
         }

        holder.tvEdit.setOnClickListener {
            updateListener.invoke(item.id)
        }

        holder.tvDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
//
//    fun updateList(newList: List<TaskEntity>){
//        items.clear()
//        items.addAll(newList)
//        notifyDataSetChanged()
//    }

}
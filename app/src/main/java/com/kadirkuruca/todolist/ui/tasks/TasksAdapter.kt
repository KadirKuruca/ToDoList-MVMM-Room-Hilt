package com.kadirkuruca.todolist.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kadirkuruca.todolist.data.Task
import com.kadirkuruca.todolist.databinding.ItemTaskBinding

class TasksAdapter : ListAdapter<Task, TasksAdapter.TasksViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class TasksViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(task: Task){
            binding.apply {
                checkBoxCompleted.isChecked = task.isCompleted
                textViewName.text = task.name
                textViewName.paint.isStrikeThruText = task.isCompleted
                labelPriority.isVisible = task.isImportant
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.taskId == newItem.taskId
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

    }
}
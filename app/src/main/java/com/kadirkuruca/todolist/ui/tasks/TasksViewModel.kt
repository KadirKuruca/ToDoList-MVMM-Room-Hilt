package com.kadirkuruca.todolist.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kadirkuruca.todolist.data.TaskDao

class TasksViewModel @ViewModelInject constructor(
    private val taskDao : TaskDao
) : ViewModel() {
    val tasks = taskDao.getTasks().asLiveData()
}
package com.kadirkuruca.todolist.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kadirkuruca.todolist.data.Task
import com.kadirkuruca.todolist.data.TaskDao

class AddEditTaskViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle,
    private val taskDao: TaskDao)
    : ViewModel() {
    val task = state.get<Task>("task") //Get task value from SavedStateHandle

    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.isImportant ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }
}
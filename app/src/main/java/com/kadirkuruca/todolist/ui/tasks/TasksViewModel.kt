package com.kadirkuruca.todolist.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kadirkuruca.todolist.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao
) : ViewModel() {
    val searchQuery = MutableStateFlow("")

    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow(false)

    private val tasksFlow = combine(
        searchQuery,
        sortOrder,
        hideCompleted
    ){ searchQuery, sortOrder, hideCompleted ->
        Triple(searchQuery, sortOrder, hideCompleted)
    }.flatMapLatest { (searchQuery, sortOrder, hideCompleted) ->
        taskDao.getTasks(searchQuery, sortOrder, hideCompleted)
    }

    val tasks = tasksFlow.asLiveData()
}

enum class SortOrder { BY_NAME, BY_DATE }
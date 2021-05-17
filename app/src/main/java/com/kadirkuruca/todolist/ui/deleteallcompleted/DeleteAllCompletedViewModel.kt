package com.kadirkuruca.todolist.ui.deleteallcompleted

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.kadirkuruca.todolist.data.TaskDao
import com.kadirkuruca.todolist.di.ApplicationScope
import com.kadirkuruca.todolist.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteAllCompletedViewModel @ViewModelInject constructor(
    private val taskRepository: TaskRepository,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    fun onConfirmClick(){
        applicationScope.launch {
            taskRepository.deleteCompletedTasks()
        }
    }
}
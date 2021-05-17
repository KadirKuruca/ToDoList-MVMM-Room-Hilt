package com.kadirkuruca.todolist.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadirkuruca.todolist.data.Task
import com.kadirkuruca.todolist.data.TaskDao
import com.kadirkuruca.todolist.repository.TaskRepository
import com.kadirkuruca.todolist.ui.ADD_TASK_RESULT_OK
import com.kadirkuruca.todolist.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class AddEditTaskViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle,
    private val taskRepository: TaskRepository
) : ViewModel() {

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

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        }

        if (task != null) {
            val updatedTask = task.copy(name = taskName, isImportant = taskImportance)
            viewModelScope.launch {
                taskRepository.update(updatedTask)
                //navigate back
                addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(
                    EDIT_TASK_RESULT_OK))
            }
        } else {
            val task = Task(name = taskName, isImportant = taskImportance)
            viewModelScope.launch {
                taskRepository.insert(task)
                //navigate back
                addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(
                    ADD_TASK_RESULT_OK))
            }
        }
    }

    private fun showInvalidInputMessage(message: String) {
        viewModelScope.launch {
            addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(message))
        }
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }
}

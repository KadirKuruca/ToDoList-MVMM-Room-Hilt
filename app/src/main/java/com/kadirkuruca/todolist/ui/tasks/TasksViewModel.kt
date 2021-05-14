package com.kadirkuruca.todolist.ui.tasks

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kadirkuruca.todolist.data.PreferencesManager
import com.kadirkuruca.todolist.data.SortOrder
import com.kadirkuruca.todolist.data.Task
import com.kadirkuruca.todolist.data.TaskDao
import com.kadirkuruca.todolist.ui.ADD_TASK_RESULT_OK
import com.kadirkuruca.todolist.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    val searchQuery = state.getLiveData("searchQuery", "") //Search query gets from SavedStateHandle

    val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    private val tasksFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ) { searchQuery, filterPreferences ->
        Pair(searchQuery, filterPreferences)
    }.flatMapLatest { (searchQuery, filterPreferences) ->
        taskDao.getTasks(searchQuery, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    val tasks = tasksFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedSelected(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(task: Task) {
        viewModelScope.launch {
            tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
        }
    }

    fun onTaskCheckedChanged(task: Task, isChecked: Boolean) {
        viewModelScope.launch {
            taskDao.update(task.copy(isCompleted = isChecked))
        }
    }

    fun onTaskSwiped(task: Task) {
        viewModelScope.launch {
            taskDao.delete(task)
            tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
        }
    }

    fun onUndoDeleteClick(task: Task) {
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }

    fun onAddNewTaskClick() {
        viewModelScope.launch {
            tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
        }
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_TASK_RESULT_OK -> showTaskConfirmationMessage("Task added!")
            EDIT_TASK_RESULT_OK -> showTaskConfirmationMessage("Task updated!")
        }
    }

    private fun showTaskConfirmationMessage(msg: String) {
        viewModelScope.launch {
            tasksEventChannel.send(TasksEvent.ShowTaskConfirmationMessage(msg))
        }
    }

    fun onDeleteAllCompletedClick() {
        viewModelScope.launch {
            tasksEventChannel.send(TasksEvent.NavigateToDeleteAllCompletedTaskScreen)
        }
    }

    sealed class TasksEvent {
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
        data class ShowTaskConfirmationMessage(val msg: String) : TasksEvent()
        object NavigateToDeleteAllCompletedTaskScreen : TasksEvent()
    }
}
package com.kadirkuruca.todolist.repository

import com.kadirkuruca.todolist.data.SortOrder
import com.kadirkuruca.todolist.data.Task
import com.kadirkuruca.todolist.data.TaskDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao){

    suspend fun insert(task: Task){
        taskDao.insert(task)
    }

    suspend fun update(task: Task){
        taskDao.update(task)
    }

    suspend fun delete(task: Task){
        taskDao.delete(task)
    }

    suspend fun deleteCompletedTasks(){
        taskDao.deleteCompletedTasks()
    }

    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean) : Flow<List<Task>> {
        return taskDao.getTasks(query, sortOrder, hideCompleted)
    }
}
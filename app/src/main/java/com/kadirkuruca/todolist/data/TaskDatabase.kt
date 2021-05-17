package com.kadirkuruca.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kadirkuruca.todolist.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase(){

    abstract fun taskDao() : TaskDao

    class Callback @Inject constructor(
        private val database : Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope : CoroutineScope
    ) : RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("Wash the dishes"))
                dao.insert(Task("Cook the dinner"))
                dao.insert(Task("Study English", isImportant = true))
                dao.insert(Task("Watch LOTR 1", isCompleted = true))
                dao.insert(Task("Watch LOTR 2", isCompleted = true))
                dao.insert(Task("Watch LOTR 3"))
            }
        }
    }
}
package com.kadirkuruca.todolist.di

import android.app.Application
import androidx.room.Room
import com.kadirkuruca.todolist.data.TaskDao
import com.kadirkuruca.todolist.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: TaskDatabase.Callback): TaskDatabase {
        return Room.databaseBuilder(application, TaskDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideTaskDao(db : TaskDatabase) : TaskDao{
        return db.taskDao()
    }

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() : CoroutineScope{
        return CoroutineScope(SupervisorJob())
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
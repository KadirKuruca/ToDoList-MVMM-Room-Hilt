package com.kadirkuruca.todolist.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "task_table")
@Parcelize
data class Task @JvmOverloads constructor(
    val name : String,
    val isImportant : Boolean = false,
    val isCompleted : Boolean = false,
    val created : Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val taskId : Int = 0
) : Parcelable {
    val createdDateFormatted : String
        get() = DateFormat.getDateTimeInstance().format(created)
}

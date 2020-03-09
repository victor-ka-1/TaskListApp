package com.example.tasklistproject.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_table")
data class Task(
    var isDone:Boolean,
    @ColumnInfo(name = "taskName") var name:String,
    var info: String ="",
    @PrimaryKey(autoGenerate = true) var id :Int? = null
)
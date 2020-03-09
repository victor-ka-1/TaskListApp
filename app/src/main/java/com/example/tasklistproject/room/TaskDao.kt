package com.example.tasklistproject.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao{
    @Insert
    fun insert(task: Task)

    @Delete
    fun delete(task: Task)

    @Update
    fun update(task: Task)

    @Query("DELETE FROM tasks_table")
    fun deleteAllTasks()

    @Query("SELECT * FROM tasks_table")
    fun getAllTasks(): LiveData<List<Task>>
}

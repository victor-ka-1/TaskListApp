package com.example.tasklistproject.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao{
    @Insert
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Update
    suspend fun update(task: Task)

    @Query("DELETE FROM tasks_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM tasks_table")
     fun getAllTasks(): LiveData<List<Task>>
}

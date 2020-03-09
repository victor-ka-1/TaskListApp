package com.example.tasklistproject

import androidx.lifecycle.LiveData
import com.example.tasklistproject.room.Task
import com.example.tasklistproject.room.TaskDao

interface ITaskRepository{
    suspend fun insert(task: Task)
    suspend fun delete(task: Task)
    suspend fun update(task: Task)
    suspend fun deleteAllTasks()
    fun getAllTasks() : LiveData<List<Task>>
}

class TaskRepository( private val dao: TaskDao)  :ITaskRepository {

    override suspend fun insert(task: Task){
         dao.insert(task)
    }
    override suspend fun delete(task: Task){
         dao.delete(task)
    }
    override suspend fun update(task: Task){
        dao.update(task)
    }
    override suspend fun deleteAllTasks(){
       dao.deleteAllTasks()
    }

    override fun getAllTasks() :LiveData<List<Task>>{
        return dao.getAllTasks()
    }
}

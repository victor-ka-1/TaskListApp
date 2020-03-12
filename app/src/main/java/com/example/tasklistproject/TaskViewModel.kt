package com.example.tasklistproject

import androidx.lifecycle.*
import com.example.tasklistproject.room.Task
import com.example.tasklistproject.ui.ShowType
import kotlinx.coroutines.launch

fun <R> combineLatest(vararg dependencies: LiveData<out Any?>, combiner: () -> R?): LiveData<R> =
    MediatorLiveData<R>().also { mediatorLiveData ->
    val observer = Observer<Any?> { mediatorLiveData.value = combiner() }
    dependencies.forEach { dependencyLiveData ->
        mediatorLiveData.addSource(dependencyLiveData, observer)
    }
}

class TaskViewModel(private val repository: ITaskRepository) : ViewModel(){

    private val allTasksLiveData = repository.getAllTasks()

     val showType: MutableLiveData<ShowType> = MutableLiveData(ShowType.ALL_TASKS)

    val finalLiveData: LiveData<List<Task>> = combineLatest(showType, allTasksLiveData){
        when(showType.value){
            ShowType.ALL_TASKS -> allTasksLiveData.value
            ShowType.DONE_TASKS -> allTasksLiveData.value?.filter { task -> task.isDone }
            ShowType.UNDONE_TASKS -> allTasksLiveData.value?.filter { task -> !task.isDone }
            ShowType.IMPORTANT_TASKS -> allTasksLiveData.value?.filter { task -> task.isImportant }
            else -> null
        }
    }

    fun addNewTask(task: Task){
        viewModelScope.launch { repository.insert(task)}
    }
    fun deleteTask(task: Task){
        viewModelScope.launch { repository.delete(task) }

    }
    fun updateTask(task: Task){
        viewModelScope.launch { repository.update(task) }
    }
    fun deleteAllTasks(){
        viewModelScope.launch { repository.deleteAllTasks() }
    }
}


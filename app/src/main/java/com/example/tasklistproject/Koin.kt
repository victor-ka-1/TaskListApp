package com.example.tasklistproject

import androidx.room.Room
import com.example.tasklistproject.room.TaskDataBase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val roomDBModule = module{
    single {
        Room.databaseBuilder( get(), TaskDataBase::class.java, "task_database")
            .build()
    }
    single { get<TaskDataBase>().taskDao() }
}

val viewModelModule = module {
    viewModel { TaskViewModel( repository = get() ) }
}

val appModule = module {
    single { TaskRepository( dao = get()) as ITaskRepository }
}
//ddd
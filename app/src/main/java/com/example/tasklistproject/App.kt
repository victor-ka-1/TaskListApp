package com.example.tasklistproject

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application(){
    override fun onCreate() {
        super.onCreate()

        setUpKoin()

    }

    private fun setUpKoin(){
        startKoin {
            androidContext(this@App)
            modules(
                roomDBModule,
                viewModelModule,
                appModule
            )
        }
    }
}
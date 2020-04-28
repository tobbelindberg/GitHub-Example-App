package com.github.base

import android.app.Application
import com.github.di.AppComponent
import com.github.di.AppModule
import com.github.di.DaggerAppComponent


open class GitHubApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(applicationContext)).build()

    }

}
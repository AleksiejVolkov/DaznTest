package com.alexvolkov.dazntestapp

import android.app.Application
import com.alexvolkov.dazntestapp.di.appModules
import com.alexvolkov.dazntestapp.di.useCasesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class DaznApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DaznApp)
            // Load modules
            modules(modules = appModules + useCasesModule)
        }
    }
}
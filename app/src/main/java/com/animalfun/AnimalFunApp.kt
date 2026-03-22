package com.animalfun

import android.app.Application
import com.animalfun.di.appModules
import com.animalfun.util.LocaleHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AnimalFunApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AnimalFunApp)
            modules(appModules)
        }

        // Restore saved language preference
        applicationScope.launch {
            val savedLanguage = LocaleHelper.getLanguageFlow(this@AnimalFunApp).first()
            LocaleHelper.applyLanguage(savedLanguage)
        }
    }
}

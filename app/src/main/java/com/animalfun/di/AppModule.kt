package com.animalfun.di

import com.animalfun.data.local.AnimalDatabase
import com.animalfun.data.repository.AnimalRepository
import com.animalfun.data.repository.ProgressRepository
import com.animalfun.ui.screens.detail.AnimalDetailViewModel
import com.animalfun.ui.screens.explore.ExploreViewModel
import com.animalfun.ui.screens.home.HomeViewModel
import com.animalfun.ui.screens.quiz.QuizViewModel
import com.animalfun.ui.screens.memory.MemoryViewModel
import com.animalfun.ui.screens.settings.SettingsViewModel
import com.animalfun.ui.screens.sounds.SoundsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single { AnimalDatabase.buildDatabase(androidContext()) }

    single { get<AnimalDatabase>().animalDao() }

    single { get<AnimalDatabase>().progressDao() }
}

val repositoryModule = module {
    single { AnimalRepository(get()) }
    single { ProgressRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(androidApplication(), get(), get()) }
    viewModel { ExploreViewModel(get()) }
    viewModel { AnimalDetailViewModel(androidApplication(), get(), get()) }
    viewModel { QuizViewModel(androidApplication(), get()) }
    viewModel { SoundsViewModel(androidApplication(), get()) }
    viewModel { MemoryViewModel(androidApplication(), get()) }
    viewModel { SettingsViewModel(androidApplication(), get()) }
}

val appModules = listOf(
    databaseModule,
    repositoryModule,
    viewModelModule
)

package com.alexvolkov.dazntestapp.di

import com.alexvolkov.dazntestapp.data.repository.EventsRepository
import com.alexvolkov.dazntestapp.domain.FetchEventsUseCase
import com.alexvolkov.dazntestapp.presentation.viemodel.EventsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModules = module {
    viewModelOf(::EventsViewModel)

    single { EventsRepository() }
}

val useCasesModule = module {
    factory { FetchEventsUseCase(repository = get()) }
}
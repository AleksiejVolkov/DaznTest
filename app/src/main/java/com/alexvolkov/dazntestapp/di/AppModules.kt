package com.alexvolkov.dazntestapp.di

import com.alexvolkov.dazntestapp.data.repository.EventsRepository
import com.alexvolkov.dazntestapp.data.repository.ScheduleRepository
import com.alexvolkov.dazntestapp.domain.FetchEventsUseCase
import com.alexvolkov.dazntestapp.domain.FetchScheduleUseCase
import com.alexvolkov.dazntestapp.presentation.viemodel.EventsViewModel
import com.alexvolkov.dazntestapp.presentation.viemodel.ScheduleViewModel
import com.alexvolkov.dazntestapp.presentation.viemodel.VideoPlaybackViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModules = module {
    viewModelOf(::EventsViewModel)
    viewModelOf(::ScheduleViewModel)
    viewModelOf(::VideoPlaybackViewModel)

    single { EventsRepository() }
    single { ScheduleRepository() }
}

val useCasesModule = module {
    factory { FetchEventsUseCase(repository = get()) }
    factory { FetchScheduleUseCase(repository = get()) }
}
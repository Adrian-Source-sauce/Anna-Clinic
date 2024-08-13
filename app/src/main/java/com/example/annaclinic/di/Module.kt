package com.example.annaclinic.di

import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.data.repository.AnnaClinicRepository
import com.example.annaclinic.domain.repository.IAnnaClinicRepository
import com.example.annaclinic.domain.usecase.AnnaClinicUseCase
import com.example.annaclinic.domain.usecase.IAnnaClinicUseCase
import com.example.annaclinic.presentation.screens.login.LoginViewModel
import com.example.annaclinic.presentation.screens.main.MainViewModel
import com.example.annaclinic.presentation.screens.register.RegisterViewModel
import com.example.annaclinic.presentation.screens.reservation.detail.ReservationDetailViewModel
import com.example.annaclinic.presentation.screens.reservation.form.ReservationFormViewModel
import com.example.annaclinic.presentation.screens.reservation.list.ReservationListViewModel
import com.example.annaclinic.presentation.screens.reservation.product.ProductViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val fireBaseModule = module {
    single { FirebaseDatabase.getInstance().reference.child("anna_clinic") }
    single { FirebaseStorage.getInstance().reference.child("images") }
}

val sharedPrefModule = module {
    single { SharedPrefUtils(androidContext()) }
}

val repositoryModule = module {
    single { AnnaClinicRepository(get(), get()) } bind IAnnaClinicRepository::class
}

val useCaseModule = module {
    factory { AnnaClinicUseCase(get()) } bind IAnnaClinicUseCase::class
}

val appModule = module {
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ReservationListViewModel(get()) }
    viewModel { ReservationFormViewModel(get()) }
    viewModel { ProductViewModel(get()) }
    viewModel { ReservationDetailViewModel(get()) }
}
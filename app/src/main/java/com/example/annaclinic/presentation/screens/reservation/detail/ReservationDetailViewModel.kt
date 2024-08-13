package com.example.annaclinic.presentation.screens.reservation.detail

import androidx.lifecycle.ViewModel
import com.example.annaclinic.domain.usecase.IAnnaClinicUseCase

class ReservationDetailViewModel(private val mUseCase: IAnnaClinicUseCase) : ViewModel() {
    val getRealTimeQueue = mUseCase.getRealTimeQueue()
}
package com.example.annaclinic.presentation.screens.reservation.list

import androidx.lifecycle.ViewModel
import com.example.annaclinic.domain.usecase.AnnaClinicUseCase

class ReservationListViewModel(mUseCase: AnnaClinicUseCase): ViewModel() {
    val reservationList = mUseCase.getTreatmentList()
}
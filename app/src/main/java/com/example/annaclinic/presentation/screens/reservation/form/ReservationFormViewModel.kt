package com.example.annaclinic.presentation.screens.reservation.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.annaclinic.domain.model.Reservation
import com.example.annaclinic.domain.usecase.AnnaClinicUseCase

class ReservationFormViewModel(private val mUseCase: AnnaClinicUseCase) : ViewModel() {
    val queue = mUseCase.getRealTimeQueue().asLiveData()

    fun postTheReservation(reservation: Reservation) =
        mUseCase.postReservation(reservation).asLiveData()
}
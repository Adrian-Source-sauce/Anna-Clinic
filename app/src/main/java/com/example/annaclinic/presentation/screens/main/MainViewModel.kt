package com.example.annaclinic.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.annaclinic.domain.model.Banner
import com.example.annaclinic.domain.model.Reservation
import com.example.annaclinic.domain.usecase.IAnnaClinicUseCase

class MainViewModel(private val mUseCase: IAnnaClinicUseCase) : ViewModel() {

    /**
     * viewModel for user
     * **/

    val getRealTimeQueue = mUseCase.getRealTimeQueue()

    fun postQueue(queue: Int, accountType: String) = mUseCase.postQueue(queue, accountType).asLiveData()

    fun uploadImage(banner: Banner) = mUseCase.uploadImage(banner).asLiveData()

    val getListImage = mUseCase.getListImage()

    fun getListByEmail(email: String) = mUseCase.getReservationListByEmail(email)

    fun getQueueByEmail(email: String) = mUseCase.getUserQueueByEmail(email)

    /**
     * viewModel for admin
     * **/

    fun getUserById(userId: String) = mUseCase.getUserById(userId)

    fun getAllReservation(accountType: String) = mUseCase.getAllReservation(accountType)

    fun deleteImage(imageId: String) = mUseCase.deleteImageById(imageId).asLiveData()

    fun deleteReservation(reservationId: String) = mUseCase.deleteReservationById(reservationId).asLiveData()

    fun editReservation(reservation: Reservation) = mUseCase.editReservationById(reservation).asLiveData()
}
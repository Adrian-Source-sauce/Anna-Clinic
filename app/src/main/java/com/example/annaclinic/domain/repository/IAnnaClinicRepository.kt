package com.example.annaclinic.domain.repository

import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.domain.model.Banner
import com.example.annaclinic.domain.model.Products
import com.example.annaclinic.domain.model.Reservation
import com.example.annaclinic.domain.model.Treatments
import com.example.annaclinic.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IAnnaClinicRepository {
    fun registerUser(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): Flow<Response<Boolean>>

    fun loginUser(
        email: String,
        password: String,
        accountType: String,
    ): Flow<Response<Boolean>>

    fun getRealTimeQueue(): Flow<Response<Int>>

    fun postQueue(queue: Int, accountType: String): Flow<Response<Boolean>>

    fun uploadImage(banner: Banner): Flow<Response<Boolean>>

    fun getListImage(): Flow<Response<List<Banner>>>

    fun getTreatmentList(): Flow<Response<List<Treatments>>>

    fun postReservation(reservation: Reservation): Flow<Response<Boolean>>

    fun getReservationListByEmail(email: String): Flow<Response<List<Reservation>>>

    fun getUserById(userId: String): Flow<Response<User>>

    fun getAllReservation(accountType: String): Flow<Response<List<Reservation>>>

    fun deleteImageById(imageId: String): Flow<Response<Boolean>>

    fun deleteReservationById(reservationId: String): Flow<Response<Boolean>>

    fun editReservationById(reservation: Reservation): Flow<Response<Boolean>>

    fun getUserQueueByEmail(email: String): Flow<Response<Int>>

    fun getAllProducts(): Flow<Response<List<Products>>>
}
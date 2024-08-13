package com.example.annaclinic.domain.usecase

import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.domain.model.Banner
import com.example.annaclinic.domain.model.Products
import com.example.annaclinic.domain.model.Reservation
import com.example.annaclinic.domain.model.Treatments
import com.example.annaclinic.domain.model.User
import com.example.annaclinic.domain.repository.IAnnaClinicRepository
import kotlinx.coroutines.flow.Flow

class AnnaClinicUseCase(
    private val repository: IAnnaClinicRepository
) : IAnnaClinicUseCase {

    override fun registerUser(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Flow<Response<Boolean>> =
        repository.registerUser(name, email, password, confirmPassword)

    override fun loginUser(
        email: String,
        password: String,
        accountType: String
    ): Flow<Response<Boolean>> = repository.loginUser(email, password, accountType)

    override fun getRealTimeQueue(): Flow<Response<Int>> = repository.getRealTimeQueue()
    override fun postQueue(queue: Int, accountType: String): Flow<Response<Boolean>> =
        repository.postQueue(queue, accountType)

    override fun uploadImage(banner: Banner): Flow<Response<Boolean>> =
        repository.uploadImage(banner)

    override fun getListImage(): Flow<Response<List<Banner>>> = repository.getListImage()

    override fun getTreatmentList(): Flow<Response<List<Treatments>>> =
        repository.getTreatmentList()

    override fun postReservation(reservation: Reservation): Flow<Response<Boolean>> =
        repository.postReservation(reservation)

    override fun getReservationListByEmail(email: String): Flow<Response<List<Reservation>>> =
        repository.getReservationListByEmail(email)

    override fun getUserById(userId: String): Flow<Response<User>> = repository.getUserById(userId)

    override fun getAllReservation(accountType: String): Flow<Response<List<Reservation>>> =
        repository.getAllReservation(accountType)

    override fun deleteImageById(imageId: String): Flow<Response<Boolean>> =
        repository.deleteImageById(imageId)

    override fun deleteReservationById(reservationId: String): Flow<Response<Boolean>> =
        repository.deleteReservationById(reservationId)

    override fun editReservationById(reservation: Reservation): Flow<Response<Boolean>> =
        repository.editReservationById(reservation)

    override fun getUserQueueByEmail(email: String): Flow<Response<Int>> =
        repository.getUserQueueByEmail(email)

    override fun getAllProducts(): Flow<Response<List<Products>>> =
        repository.getAllProducts()

}
package com.example.annaclinic.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.annaclinic.domain.usecase.IAnnaClinicUseCase

class RegisterViewModel (private val mUseCase: IAnnaClinicUseCase) : ViewModel() {

    fun registerUser(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
    ) = mUseCase.registerUser(name, email, password, confirmPassword).asLiveData()
}

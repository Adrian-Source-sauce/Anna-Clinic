package com.example.annaclinic.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.annaclinic.domain.usecase.IAnnaClinicUseCase

class LoginViewModel(private val mUseCase: IAnnaClinicUseCase) : ViewModel() {

    fun loginUser(
        email: String,
        password: String,
        accountType: String
    ) = mUseCase.loginUser(email, password, accountType).asLiveData()
}
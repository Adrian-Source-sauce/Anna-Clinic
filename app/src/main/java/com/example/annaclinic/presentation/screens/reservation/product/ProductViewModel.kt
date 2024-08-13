package com.example.annaclinic.presentation.screens.reservation.product

import androidx.lifecycle.ViewModel
import com.example.annaclinic.domain.usecase.AnnaClinicUseCase

class ProductViewModel(mUseCase: AnnaClinicUseCase) : ViewModel() {
    val productList = mUseCase.getAllProducts()
}
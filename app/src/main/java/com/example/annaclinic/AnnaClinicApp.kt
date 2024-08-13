package com.example.annaclinic

import android.app.Application
import android.util.Log
import com.example.annaclinic.core.utils.Const
import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.di.appModule
import com.example.annaclinic.di.fireBaseModule
import com.example.annaclinic.di.repositoryModule
import com.example.annaclinic.di.sharedPrefModule
import com.example.annaclinic.di.useCaseModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AnnaClinicApp : Application() {
    private val sharedPrefUtils: SharedPrefUtils by inject()
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AnnaClinicApp)
            modules(
                listOf(
                    fireBaseModule,
                    repositoryModule,
                    useCaseModule,
                    appModule,
                    sharedPrefModule,
                )
            )
        }

        val email = sharedPrefUtils.getString(Const.EMAIL)
        val password = sharedPrefUtils.getString(Const.PASSWORD)
        val accountType = sharedPrefUtils.getString(Const.ACCOUNT_TYPE)

        Log.d("AnnaClinicApp", "email: $email \n password: $password \n accountType: $accountType")
    }
}
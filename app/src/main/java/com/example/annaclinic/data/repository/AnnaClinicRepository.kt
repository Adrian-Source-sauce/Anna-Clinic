package com.example.annaclinic.data.repository

import android.util.Log
import com.example.annaclinic.core.utils.Const
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.domain.model.Banner
import com.example.annaclinic.domain.model.Products
import com.example.annaclinic.domain.model.Reservation
import com.example.annaclinic.domain.model.Treatments
import com.example.annaclinic.domain.model.User
import com.example.annaclinic.domain.repository.IAnnaClinicRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class AnnaClinicRepository(
    private val clinicRef: DatabaseReference,
    private val preferences: SharedPrefUtils,
) : IAnnaClinicRepository {

    override fun registerUser(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            val usersRef = clinicRef.child(Const.USERS)
            val userId = clinicRef.push().key!!
            val user = User(
                userId = userId,
                email = email,
                password = password,
                accountType = "Pasien",
                name = name
            )

            if (password != confirmPassword) {
                emit(Response.Empty("Pastikan password yang anda masukkan sama!"))
            } else {
                usersRef.child(userId).setValue(user)
                emit(Response.Success(true))
            }

        } catch (e: Exception) {
            emit(Response.Failure(e.message))
        }

    }.flowOn(Dispatchers.IO)

    override fun loginUser(
        email: String,
        password: String,
        accountType: String
    ): Flow<Response<Boolean>> {
        return flow {
            try {
                emit(Response.Loading)
                val usersRef = clinicRef.child(Const.USERS)
                val userQuery = usersRef.orderByChild("email").equalTo(email).get().await()

                if (!userQuery.exists()) {
                    emit(Response.Failure("Email yang anda masukkan tidak terdaftar!"))
                    return@flow
                }

                val user = userQuery.children.first()
                val userPassword = user.child("password").getValue(String::class.java)
                val userAccountType = user.child("accountType").getValue(String::class.java)
                val userId = user.child("userId").getValue(String::class.java)

                if (userPassword != password) {
                    emit(Response.Failure("Password yang anda masukkan salah!"))
                    return@flow
                }

                if (userAccountType != accountType) {
                    emit(Response.Failure("Pastikan tipe Akun yang anda masukkan sesuai!"))
                    return@flow
                }

                Log.d("LoginRepository", "User -> ${user.value}")
                preferences.saveString(Const.USER_ID, userId!!)
                emit(Response.Success(true))
            } catch (e: Exception) {
                emit(Response.Failure(e.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getRealTimeQueue(): Flow<Response<Int>> = callbackFlow {
        val queueRef = clinicRef.child(Const.REALTIME_QUEUE)

        val queueListener = queueRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val realtimeQueue = snapshot.getValue(Int::class.java)
                Log.d("QueueRepository", "Queue -> $realtimeQueue")
                if (realtimeQueue != null) {
                    trySend(Response.Success(realtimeQueue)).isSuccess
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("QueueRepository", "Error -> ${error.message}")
                trySend(Response.Failure(error.message)).isSuccess
                close()
            }
        })

        awaitClose {
            queueRef.removeEventListener(queueListener)
        }
    }

    override fun postQueue(queue: Int, accountType: String): Flow<Response<Boolean>> =
        callbackFlow {
            // check if account type is not admin
            if (accountType != "Admin") {
                trySend(Response.Failure("Anda tidak memiliki akses untuk mengubah antrian!")).isSuccess
                return@callbackFlow
            }

            val queueRef = clinicRef.child("realtime_queue")
            queueRef.setValue(queue).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("QueueRepository", "Queue -> $queue")
                    trySend(Response.Success(true)).isSuccess
                } else {
                    trySend(Response.Failure(it.exception?.message)).isSuccess
                    Log.d("QueueRepository", "Error -> ${it.exception?.message}")
                }
            }

            awaitClose {}
        }

    override fun uploadImage(banner: Banner): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            val bannerRef = clinicRef.child(Const.BANNER)
            val bannerKey = clinicRef.push().key!!
            banner.id = bannerKey

            bannerRef.child(bannerKey).setValue(banner)
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override fun getListImage(): Flow<Response<List<Banner>>> =
        callbackFlow {
            val bannerRef = clinicRef.child(Const.BANNER)

            val bannerListener = bannerRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val banners = ArrayList<Banner>()

                    for (bannerSnapshot in snapshot.children) {
                        val id = bannerSnapshot.child("id").getValue(String::class.java)
                        val image = bannerSnapshot.child("imageBase64").getValue(String::class.java)
                        val date = bannerSnapshot.child("date").getValue(String::class.java)

                        if (id != null && image != null && date != null) {
                            banners.add(Banner(id, image, date))
                        }
                    }

                    if (banners.isEmpty()) {
                        trySend(Response.Empty("Banner tidak ditemukan!")).isSuccess
                    } else {
                        trySend(Response.Success(banners)).isSuccess
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(Response.Failure(error.message)).isSuccess
                    close()
                }
            })

            awaitClose {
                bannerRef.removeEventListener(bannerListener)
            }
        }

    override fun getTreatmentList(): Flow<Response<List<Treatments>>> = callbackFlow {
        val treatmentsRef = clinicRef.child(Const.RESERVATION_ITEMS).child(Const.TREATMENTS)

        treatmentsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("TreatmentsRepository", "Snapshot -> $snapshot")
                if (snapshot.exists()) {
                    val treatments = ArrayList<Treatments>()

                    for (treatmentSnapshot in snapshot.children) {
                        val name = treatmentSnapshot.child("name").getValue(String::class.java)
                        val price = treatmentSnapshot.child("price").getValue(Long::class.java)

                        if (name != null && price != null) {
                            treatments.add(Treatments(name, price))
                        }
                    }
                    Log.d("TreatmentsRepository", "Treatments -> $treatments")
                    trySend(Response.Success(treatments)).isSuccess
                }
                close()
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Response.Failure(error.message)).isSuccess
                close()
            }

        })

        awaitClose {}

    }

    override fun postReservation(reservation: Reservation): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            val reservationRef = clinicRef.child(Const.RESERVATIONS)
            val reservationKey = reservationRef.push().key!!

            // Check if a reservation with the same email and date exists
            val emailQuery = reservationRef
                .orderByChild("email")
                .equalTo(reservation.email)
                .get()
                .await()

            val dateByQuery = emailQuery.children.find {
                it.child("date").getValue(String::class.java) == reservation.date
            }

            if (dateByQuery != null) {
                emit(Response.Failure("Anda sudah melakukan reservasi pada tanggal yang sama!"))
                return@flow
            }

            // Calculate the queue number
            val queueQuery = reservationRef
                .orderByChild("date")
                .equalTo(reservation.date)
                .limitToLast(1)
                .get()
                .await()

            val queue = if (queueQuery.exists()) {
                val lastQueue = queueQuery.children.first().child("queue").getValue(Int::class.java)
                (lastQueue ?: 0) + 1
            } else {
                1
            }

            reservation.id = reservationKey
            reservation.queue = queue

            // Save the new reservation
            reservationRef.child(reservationKey).setValue(reservation)
            emit(Response.Success(true))

        } catch (e: Exception) {
            Log.d("ReservationRepository", "Error -> ${e.message}")
            emit(Response.Failure(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override fun getReservationListByEmail(email: String): Flow<Response<List<Reservation>>> =
        callbackFlow {
            val reservationRef = clinicRef.child(Const.RESERVATIONS)

            val reservationListener =
                reservationRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val reservations = ArrayList<Reservation>()

                        for (reservationSnapshot in snapshot.children) {
                            val id: String =
                                reservationSnapshot.child("id").getValue(String::class.java)!!
                            val name: String =
                                reservationSnapshot.child("name").getValue(String::class.java)!!
                            val emailUser: String =
                                reservationSnapshot.child("email").getValue(String::class.java)!!
                            val phone: String =
                                reservationSnapshot.child("phone").getValue(String::class.java)!!
                            val service: String =
                                reservationSnapshot.child("service").getValue(String::class.java)!!
                            val price: String =
                                reservationSnapshot.child("price").getValue(String::class.java)!!
                            val queue: Int =
                                reservationSnapshot.child("queue").getValue(Int::class.java)!!
                            val description: String = reservationSnapshot.child("description")
                                .getValue(String::class.java)!!
                            val date: String =
                                reservationSnapshot.child("date").getValue(String::class.java)!!
                            val product: String =
                                reservationSnapshot.child("product").getValue(String::class.java)
                                    ?: ""
                            val totalProductPrice: String =
                                reservationSnapshot.child("totalProductPrice")
                                    .getValue(String::class.java) ?: ""
                            val totalPrice: String =
                                reservationSnapshot.child("totalPrice").getValue(String::class.java)
                                    ?: ""

                            if (emailUser == email) {
                                reservations.add(
                                    Reservation(
                                        id = id,
                                        name = name,
                                        email = emailUser,
                                        phone = phone,
                                        service = service,
                                        price = price,
                                        queue = queue,
                                        description = description,
                                        date = date,
                                        product = product,
                                        totalProductPrice = totalProductPrice,
                                        totalPrice = totalPrice,
                                    )
                                )
                            }

                        }
                        if (reservations.isEmpty()) {
                            trySend(Response.Empty("Anda belum melakukan reservasi!")).isSuccess
                        } else {
                            trySend(Response.Success(reservations)).isSuccess
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        trySend(Response.Failure(error.message)).isSuccess
                        close()
                    }
                })

            awaitClose {
                reservationRef.removeEventListener(reservationListener)
            }
        }

    override fun getUserById(userId: String): Flow<Response<User>> = callbackFlow {
        val userRef = clinicRef.child(Const.USERS).child(userId)
        Log.d("UserRepository", "userId -> $userId")
        Log.d("UserRepository", "userRef -> $userRef")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                Log.d("UserRepository", "User -> $user")
                if (user != null) {
                    trySend(Response.Success(user)).isSuccess
                } else {
                    trySend(Response.Empty("User tidak ditemukan!")).isSuccess
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Response.Failure(error.message)).isSuccess
                close()
            }
        })

        awaitClose {}
    }

    override fun getAllReservation(accountType: String): Flow<Response<List<Reservation>>> =
        callbackFlow {
            val reservationRef = clinicRef.child(Const.RESERVATIONS)
            val reservationListener =
                reservationRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val reservations = ArrayList<Reservation>()

                        for (reservationSnapshot in snapshot.children) {
                            val id: String =
                                reservationSnapshot.child("id").getValue(String::class.java)!!
                            val name: String =
                                reservationSnapshot.child("name").getValue(String::class.java)!!
                            val emailUser: String =
                                reservationSnapshot.child("email").getValue(String::class.java)!!
                            val phone: String =
                                reservationSnapshot.child("phone").getValue(String::class.java)!!
                            val service: String =
                                reservationSnapshot.child("service").getValue(String::class.java)!!
                            val price: String =
                                reservationSnapshot.child("price").getValue(String::class.java)!!
                            val queue: Int =
                                reservationSnapshot.child("queue").getValue(Int::class.java)!!
                            val description: String = reservationSnapshot.child("description")
                                .getValue(String::class.java)!!
                            val date: String =
                                reservationSnapshot.child("date").getValue(String::class.java)!!
                            val product: String =
                                reservationSnapshot.child("product").getValue(String::class.java)
                                    ?: ""
                            val totalProductPrice: String =
                                reservationSnapshot.child("totalProductPrice")
                                    .getValue(String::class.java) ?: ""
                            val totalPrice: String =
                                reservationSnapshot.child("totalPrice")
                                    .getValue(String::class.java)!!

                            reservations.add(
                                Reservation(
                                    id = id,
                                    name = name,
                                    email = emailUser,
                                    phone = phone,
                                    service = service,
                                    price = price,
                                    queue = queue,
                                    description = description,
                                    date = date,
                                    product = product,
                                    totalProductPrice = totalProductPrice,
                                    totalPrice = totalPrice
                                )
                            )
                        }
                        if (accountType == "Admin") {
                            if (reservations.isEmpty()) {
                                trySend(Response.Empty("Belum ada reservasi!")).isSuccess
                            } else {
                                trySend(Response.Success(reservations)).isSuccess
                            }
                        } else {
                            trySend(Response.Empty("Anda tidak memiliki akses untuk melihat reservasi!")).isSuccess
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        trySend(Response.Failure(error.message)).isSuccess
                        close()
                    }
                })

            awaitClose {
                reservationRef.removeEventListener(reservationListener)
            }
        }

    override fun deleteImageById(imageId: String): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            val bannerRef = clinicRef.child(Const.BANNER)
            bannerRef.child(imageId).removeValue()
            emit(Response.Success(true))
        } catch (e: Exception) {
            Log.d("DeleteImageRepository", "Error -> ${e.message}")
            emit(Response.Failure(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteReservationById(reservationId: String): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            val reservationRef = clinicRef.child(Const.RESERVATIONS)
            reservationRef.child(reservationId).removeValue()
            emit(Response.Success(true))
        } catch (e: Exception) {
            Log.d("DeleteResRepo", "Error -> ${e.message}")
            emit(Response.Failure(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override fun editReservationById(reservation: Reservation): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            val reservationRef = clinicRef.child(Const.RESERVATIONS).child(reservation.id)

            if (reservationRef.key == null) {
                emit(Response.Failure("Reservasi tidak ditemukan!"))
                return@flow
            }

            reservationRef.setValue(reservation)
            emit(Response.Success(true))

        } catch (e: Exception) {
            emit(Response.Failure(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override fun getUserQueueByEmail(email: String): Flow<Response<Int>> = callbackFlow {
        val reservationRef = clinicRef.child(Const.RESERVATIONS)
        val queueQuery = reservationRef.orderByChild("email").equalTo(email)

        val userQueue = queueQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(
                    "QueueRepository",
                    "DataSnapshot -> ${dataSnapshot.childrenCount} children found"
                )

                if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                    var foundQueue = false
                    for (snapshot in dataSnapshot.children) {
                        val queueReservation = snapshot.child("queue").getValue(Int::class.java)
                        Log.d("QueueRepository", "Queue -> $queueReservation")
                        if (queueReservation != null) {
                            foundQueue = true
                            trySend(Response.Success(queueReservation)).isSuccess
                        }
                    }
                    if (!foundQueue) {
                        trySend(Response.Empty("Antrian tidak ditemukan!")).isSuccess
                    }
                } else {
                    trySend(Response.Empty("Antrian tidak ditemukan!")).isSuccess
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Response.Failure(error.message)).isSuccess
                close()
            }
        })

        awaitClose {
            reservationRef.removeEventListener(userQueue)
        }
    }

    override fun getAllProducts(): Flow<Response<List<Products>>> = callbackFlow {
        val productsRef = clinicRef.child(Const.RESERVATION_ITEMS).child(Const.PRODUCTS_WITH_DESC)

        productsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("ProductsRepository", "Snapshot -> $snapshot")

                if (snapshot.exists()) {
                    val products = ArrayList<Products>()

                    for (snapshotProduct in snapshot.children) {
                        val imageUrl =
                            snapshotProduct.child("image_url").getValue(String::class.java)
                        val name = snapshotProduct.child("name").getValue(String::class.java)
                        val price = snapshotProduct.child("price").getValue(Long::class.java)
                        val detail = snapshotProduct.child("desc").getValue(String::class.java)

                        if (imageUrl != null && name != null && price != null && detail != null) {
                            products.add(Products(imageUrl, name, price, detail))
                        }
                    }
                    trySend(Response.Success(products)).isSuccess
                }
                close()
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Response.Failure(error.message)).isSuccess
                close()
            }
        })

        awaitClose {}
    }
}
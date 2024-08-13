package com.example.annaclinic.core.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material.icons.rounded.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.annaclinic.core.theme.Green20
import com.example.annaclinic.core.theme.Red20
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.domain.model.Reservation
import com.example.annaclinic.presentation.screens.reservation.detail.ReservationDetailViewModel
import com.valentinilk.shimmer.shimmer
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ReservationTicket(
    modifier: Modifier = Modifier,
    reservation: Reservation,
    viewModel: ReservationDetailViewModel = koinInject()
) {
    val realTimeQueue = viewModel.getRealTimeQueue.collectAsState(initial = Response.Loading)
    // get current date
    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1
    val year = calendar.get(Calendar.YEAR)

    // set zero if day or month is less than 10
    val dayStr = if (day < 10) "0$day" else day.toString()
    val monthStr = if (month < 10) "0$month" else month.toString()

    val currentDate = "$dayStr-$monthStr-$year"

    // Define the date format
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    // Parse the dates to Date objects
    val currentDateObj = dateFormat.parse(currentDate)
    val reservationDateObj = dateFormat.parse(reservation.date)

    // Check if the reservation date is already passed
    val isDatePassed = reservationDateObj?.before(currentDateObj)

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Anna Clinic",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                )
            )

            Spacer(modifier = Modifier.padding(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.Email, contentDescription = null)
                    Text(
                        text = "Email",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = fontFamily,
                        )
                    )
                }

                Text(
                    text = reservation.email,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = fontFamily,
                    )
                )

            }
            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.DateRange, contentDescription = null)
                    Text(
                        text = "Tanggal Reservasi",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = fontFamily,
                        )
                    )
                }

                Text(
                    text = reservation.date,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = fontFamily,
                    )
                )

            }

            Spacer(modifier = Modifier.padding(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.Newspaper, contentDescription = null)
                    Text(
                        text = "Status Reservasi",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = fontFamily,
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            color = if (isDatePassed == true) Red20 else Green20
                        )
                ) {
                    Text(
                        modifier = modifier.padding(6.dp),
                        text = if (isDatePassed == true) "Antrian Selesai" else "Antrian Aktif",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = fontFamily,
                            color = Color.White
                        )
                    )
                }

            }

            Spacer(modifier = Modifier.padding(8.dp))

            Divider(
                modifier = Modifier.padding(16.dp),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Nomor Antrian",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = fontFamily
                    )
                )
                Text(
                    text = reservation.queue.toString(),
                    style = TextStyle(
                        fontSize = 64.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Divider(
                modifier = Modifier.padding(16.dp),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Antrian Sekarang",
                    style = TextStyle(fontSize = 16.sp, fontFamily = fontFamily)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.People, contentDescription = null)
                    when (realTimeQueue.value) {
                        is Response.Loading -> {
                            Box(
                                modifier = modifier
                                    .padding(top = 2.dp)
                                    .size(35.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .shimmer()
                                    .background(Color.LightGray)
                            )
                        }

                        is Response.Success -> {
                            val queue = (realTimeQueue.value as Response.Success).data
                            // Display the queue
                            Text(
                                text = queue.toString(), style = TextStyle(
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            )
                        }

                        is Response.Failure -> {
                            val error = (realTimeQueue.value as Response.Failure).msg
                            Log.e("CardQueue", "error: $error")
                            Text(text = "0")
                        }

                        is Response.Empty -> {}
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ReservationTicketPreview() {
    ReservationTicket(
        reservation = Reservation(
            name = "Anna",
            email = "pasien@gmail.com",
            phone = "123",
            service = "Service",
            price = 1000.toString(),
            queue = 1,
            description = "Description",
            date = "2022-01-01",
            product = "Product",
            totalProductPrice = 1000.toString(),
            totalPrice = 2000.toString(),
        )
    )
}
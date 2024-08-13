package com.example.annaclinic.core.component

import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.core.utils.Const
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.core.utils.SharedPrefUtils
import com.example.annaclinic.domain.model.Reservation
import com.example.annaclinic.presentation.screens.main.MainViewModel
import org.koin.compose.koinInject
import java.io.File
import java.io.FileOutputStream

@Composable
fun ReservationDetail(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinInject(),
    sharedPrefUtils: SharedPrefUtils = koinInject(),
    reservation: Reservation
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    var name by remember { mutableStateOf(reservation.name) }
    var email by remember { mutableStateOf(reservation.email) }
    var phone by remember { mutableStateOf(reservation.phone) }
    var nameOfService by remember { mutableStateOf(reservation.service) }
    var price by remember { mutableStateOf(reservation.price) }
    var queue by remember { mutableIntStateOf(reservation.queue) }
    var description by remember { mutableStateOf(reservation.description) }
    var date by remember { mutableStateOf(reservation.date) }
    var products by remember { mutableStateOf(reservation.product) }
    var totalProductPrice by remember { mutableStateOf(reservation.totalProductPrice) }
    var totalPrice by remember { mutableStateOf(reservation.totalPrice) }
    var isDisabledForm by remember { mutableStateOf(true) }

    Log.d("ReservationDetail", "Reservation: $reservation")

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
    ) {

        if (sharedPrefUtils.getString(Const.ACCOUNT_TYPE) == "Admin") {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Detail Reservasi",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = fontFamily
                    )
                )

                Row {

                    TextButton(onClick = {
                        // convert as pdf
                        val pdfDocument = PdfDocument()
                        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
                        val page = pdfDocument.startPage(pageInfo)

                        // Set up the canvas and paint
                        val canvas = page.canvas
                        val paint = Paint()
                        paint.textSize = 14f
                        paint.color = Color.BLACK

                        // Title and Logo
                        paint.textSize = 24f
                        paint.isFakeBoldText = true
                        canvas.drawText("Invoice Anna Clinic", 250f, 50f, paint)

                        // Draw a line separator
                        paint.strokeWidth = 2f
                        canvas.drawLine(50f, 70f, 545f, 70f, paint)

                        // Draw customer details
                        paint.textSize = 12f
                        paint.isFakeBoldText = true
                        canvas.drawText("Pembayaran kepada:", 50f, 100f, paint)
                        paint.isFakeBoldText = false
                        canvas.drawText("Nama: $name", 50f, 120f, paint)
                        canvas.drawText("Email: $email", 50f, 140f, paint)
                        canvas.drawText("Nomor Telepon: $phone", 50f, 160f, paint)

                        // Draw a line separator
                        canvas.drawLine(50f, 180f, 545f, 180f, paint)

                        // Draw service details
                        paint.isFakeBoldText = false
                        val services = listOf(
                            "Layanan: $nameOfService",
                            "Harga: $price",
                            "Antrian: $queue",
                            "Tambahan Produk: $products",
                            "Deskripsi: $description"
                        )
                        var yPos = 210f
                        val textXPosition = 50f
                        val valueXPosition = 350f
                        val maxWidth = 545f - valueXPosition // Max width for text drawing

                        for (service in services) {
                            val columns = service.split(": ")
                            canvas.drawText(columns[0], textXPosition, yPos, paint)

                            // If there's text after the colon, wrap it
                            if (columns.size > 1) {
                                val textLines = wrapText(columns[1], paint, maxWidth)
                                for (line in textLines) {
                                    canvas.drawText(line, valueXPosition, yPos, paint)
                                    yPos += 20f
                                }
                            } else {
                                yPos += 20f
                            }
                        }

                        // Line separator before total
                        canvas.drawLine(textXPosition, yPos, 545f, yPos, paint)
                        yPos += 30f

                        // Total price
                        paint.isFakeBoldText = true
                        canvas.drawText("Total:", 350f, yPos, paint)
                        canvas.drawText(price, 450f, yPos, paint)

                        pdfDocument.finishPage(page)

                        // save the pdf
                        val filePath = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            "reservasi_$name.pdf"
                        )

                        try {
                            pdfDocument.writeTo(FileOutputStream(filePath))
                            Toast.makeText(
                                context,
                                "PDF berhasil disimpan di ${filePath.absolutePath}",
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                            Log.d("ReservationDetail", "Error: $e")
                        }
                        pdfDocument.close()

                    }) {
                        Text("Cetak")
                    }

                    if (!isDisabledForm) {
                        TextButton(onClick = {
                            isDisabledForm = true
                        }) {
                            Text("Batal")
                        }
                    }

                    TextButton(onClick = {
                        if (isDisabledForm) {
                            isDisabledForm = false
                        } else {
                            val newReservation = Reservation(
                                id = reservation.id,
                                name = name,
                                email = email,
                                phone = phone,
                                service = nameOfService,
                                price = price,
                                queue = queue,
                                description = description,
                                date = reservation.date,
                                totalPrice = reservation.totalPrice
                            )

                            viewModel.editReservation(newReservation)
                                .observe(lifecycleOwner) { response ->
                                    when (response) {
                                        Response.Loading -> {
                                            Log.d("ReservationDetail", "Loading")
                                        }

                                        is Response.Success -> {
                                            Log.d("ReservationDetail", "Success")
                                        }

                                        is Response.Empty -> {
                                            Log.d("ReservationDetail", "Empty")
                                        }

                                        is Response.Failure -> {
                                            Log.d("ReservationDetail", "Failure")
                                        }
                                    }
                                }
                            isDisabledForm = true
                        }
                    }) {
                        if (isDisabledForm) Text("Ubah") else Text("Simpan")
                    }
                }
            }
        } else {
            Spacer(modifier = modifier.size(16.dp))
            Box { }
        }

        Spacer(modifier = modifier.size(8.dp))

        DefaultTextField(label = "Nama", value = name, isDisabled = isDisabledForm) {
            name = it
        }

        Spacer(modifier = modifier.size(16.dp))

        DefaultTextField(label = "Email", value = email, isDisabled = isDisabledForm) {
            email = it
        }

        Spacer(modifier = modifier.size(16.dp))

        DefaultTextField(label = "Nomor Telepon", value = phone, isDisabled = isDisabledForm) {
            phone = it
        }

        Spacer(modifier = modifier.size(16.dp))

        DefaultTextField(label = "Tanggal Reservasi", value = date, isDisabled = true) {
            date = it
        }

        Spacer(modifier = modifier.size(16.dp))

        DefaultTextField(label = "Layanan", value = nameOfService, isDisabled = true) {
            nameOfService = it
        }

        Spacer(modifier = modifier.size(16.dp))

        DefaultTextField(label = "Harga", value = price, isDisabled = true) {
            price = it
        }

        Spacer(modifier = modifier.size(16.dp))

        DefaultTextField(
            label = "Antrian",
            value = queue.toString(),
            isDisabled = true
        ) {
            queue = it.toInt()
        }

        if (products != "" && totalProductPrice != "") {
            Spacer(modifier = modifier.size(16.dp))
            DefaultTextField(
                label = "Tambahan Produk",
                value = products ?: "",
                isDisabled = true,
                dynamicLines = true,
                isSingleLine = false
            ) {
                products = it
            }

            Spacer(modifier = modifier.size(16.dp))
            DefaultTextField(
                label = "Total Harga Produk",
                value = totalProductPrice ?: "",
                isDisabled = true
            ) {
                totalProductPrice = it
            }
        }

        Spacer(modifier = modifier.size(16.dp))
        DefaultTextField(
            label = "Deskripsi",
            value = description,
            isDisabled = true,
            dynamicLines = true,
            isSingleLine = false
        ) {
            description = it
        }

        Spacer(modifier = modifier.size(16.dp))
        DefaultTextField(label = "Total Harga", value = totalPrice, isDisabled = true) {
            totalPrice = it
        }
    }
}

// Function to wrap text
fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
    val lines = mutableListOf<String>()
    val words = text.split(" ")
    var currentLine = ""

    for (word in words) {
        val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
        if (paint.measureText(testLine) <= maxWidth) {
            currentLine = testLine
        } else {
            lines.add(currentLine)
            currentLine = word
        }
    }
    if (currentLine.isNotEmpty()) {
        lines.add(currentLine)
    }
    return lines
}
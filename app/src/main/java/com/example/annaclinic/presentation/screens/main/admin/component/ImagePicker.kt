package com.example.annaclinic.presentation.screens.main.admin.component

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.annaclinic.core.theme.fontFamily
import com.example.annaclinic.core.utils.Response
import com.example.annaclinic.core.utils.encodeBase64
import com.example.annaclinic.domain.model.Banner
import com.example.annaclinic.presentation.screens.main.MainViewModel
import org.koin.compose.koinInject

@Composable
fun ImagePicker(viewModel: MainViewModel = koinInject()) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImageUri = it
            Log.d("ImagePicker", "Selected Image: $it")
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        if (selectedImageUri == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Rounded.AddPhotoAlternate,
                        contentDescription = "Tambahkan Banner",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Pilih Foto",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = fontFamily
                        )
                    )
                }
            }
        } else {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp)),
                model = selectedImageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = {
                if (selectedImageUri != null) {
                    val imageBase64 = encodeBase64(selectedImageUri!!, context)
                    val banner = Banner(imageBase64 = imageBase64, date = "2022-10-10")
                    Log.d("ImagePicker", "Image Base64: $imageBase64")
                    viewModel.uploadImage(banner).observe(lifecycleOwner) {
                        when (it) {
                            is Response.Loading -> {
                                Log.d("ImagePicker", "Loading")
                                Toast.makeText(
                                    context,
                                    "Mengunggah gambar...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            is Response.Success -> {
                                Log.d("ImagePicker", "Image Uploaded")
                                selectedImageUri = null
                                Toast.makeText(
                                    context,
                                    "Gambar berhasil di upload",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            is Response.Failure -> {
                                Log.d("ImagePicker", "Error: ${it.msg}")
                            }

                            is Response.Empty -> {}
                        }
                    }
                } else {
                    Log.d("ImagePicker", "Image Base64 is null")
                    Toast.makeText(
                        context,
                        "Pastikan kamu memilih gambar yang akan di upload",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }) {
            Text(text = "Tambahkan", style = TextStyle(fontSize = 18.sp, fontFamily = fontFamily))
        }


    }
}


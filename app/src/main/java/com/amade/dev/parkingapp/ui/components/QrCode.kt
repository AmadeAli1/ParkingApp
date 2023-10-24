package com.amade.dev.parkingapp.ui.components

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix


@Composable
fun QrCodeView(
    url: String,
    modifier: Modifier = Modifier,
) {
    var image by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(key1 = Unit) {
        image = generateQrCode(url)
    }
    if (image != null) {
        AndroidView(
            factory = { ImageView(it).apply { setImageBitmap(image) } },
            modifier = modifier
        )
    } else {
        CircularProgressIndicator(modifier)
    }
}


private fun generateQrCode(url: String): Bitmap? {
    val bitMatrix: BitMatrix
    val qrCodeSize = 500
    try {
        bitMatrix = MultiFormatWriter().encode(
            url,
            BarcodeFormat.QR_CODE,
            qrCodeSize, qrCodeSize,
            null
        )

    } catch (e: Exception) {
        return null
    }
    val bitMatrixWidth = bitMatrix.width

    val bitMatrixHeight = bitMatrix.height

    val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

    for (i in 0 until bitMatrixHeight) {
        val offset = i * bitMatrixWidth
        for (j in 0 until bitMatrixWidth) {
            val get = bitMatrix.get(j, i)
            pixels[offset + j] = if (get) Color.BLACK else Color.WHITE
        }
    }
    val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
    bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
    return bitmap
}